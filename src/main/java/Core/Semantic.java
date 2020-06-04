package Core;

import java.util.ArrayList;
import java.util.Stack;

public class Semantic {
    private Stack<Symbol> symbols;
    private ArrayList<String> table;
    private ArrayList<Code> codes;
    private AnalyseList analyseList;
    private int tempNumber;

    public Semantic() { }

    public String getTemp(){
        return "t"+(++tempNumber);
    }

    public void add(String first, String second){
        symbols.push(new Symbol(first, second, "#"));
    }

    public void pop(){
        symbols.pop();
    }

    public void analyse(int res, int l){
        String left = analyseList.productions.get(res).returnLeft();
        if(res == 0){
            // factor -> id | number
            String tmp = symbols.peek().getSecond();
            for(int i = 0;i < l;i++)
                symbols.pop();
            symbols.push(new Symbol(left, "#", tmp));
        }else if(res == 1){
            // term -> factor
            // expr -> term
            String tmp = symbols.peek().getAddr();
            for(int i = 0;i < l;i++)
                symbols.pop();
            symbols.push(new Symbol(left, "#", tmp));
        } else if(res == 2){
            // 四则运算
            String factor = symbols.pop().getAddr();
            String op = symbols.pop().getFirst();
            String term1 = symbols.pop().getAddr();
            String term = getTemp();
            symbols.push(new Symbol(left, "#", term));
            codes.add(new Code(op, term1, factor, term));
        } else if(res == 3){
            // stmt -> id := expr
            String expr = symbols.pop().getAddr();
            symbols.pop();
            String id = symbols.pop().getSecond();
            symbols.pop();
            symbols.push(new Symbol(left, "#", "#"));
            codes.add(new Code("=", expr, "#", id));
        } else if(res == 4){
            // bool -> expr rel expr
            String expr2 = symbols.pop().getAddr();
            String op = symbols.pop().getFirst();
            String expr1 = symbols.pop().getAddr();
            symbols.push(new Symbol(left, "#", "#", codes.size(), codes.size()+1));
            codes.add(new Code(op, expr1, expr2, "goto _"));
            codes.add(new Code(op, "#", "#", "goto _"));
        } else if(res == 5){
            // M -> epsilon
            symbols.push(new Symbol(left, "#", "#", -1, -1, codes.size()));
        } else if(res == 6){
            // N -> epsilon
            symbols.push(new Symbol(left, "#", "#", -1, -1, -1, codes.size()+1));
            codes.add(new Code("goto", "#", "#", "goto _"));
        } else if(res == 7){
            // stmt -> if bool then M stmt
            int stmt1 = symbols.pop().getNextList();
            int M = symbols.pop().getInstr();
            symbols.pop();
            int trueList = symbols.peek().getTrueList();
            int falseList = symbols.pop().getFalseList();
            symbols.pop();
            codes.get(trueList).setResult(String.valueOf(M+100));
            int stmt;
            if(stmt1 == -1){
                stmt = falseList;
            }else {
                codes.get(falseList).setResult(String.valueOf(stmt1+100));
                stmt = stmt1;
            }
            symbols.push(new Symbol(left, "#", "#", -1, -1, -1, stmt));
        } else if(res == 10){
            // stmt -> if bool then M stmt N else M stmt
            int stmt2 = symbols.pop().getNextList();
            int M2 = symbols.pop().getInstr();
            symbols.pop();
            int N = symbols.pop().getNextList();
            int stmt1 = symbols.pop().getNextList();
            int M1 = symbols.pop().getInstr();
            symbols.pop();
            int trueList = symbols.peek().getTrueList();
            int falseList = symbols.pop().getFalseList();
            symbols.pop();
            codes.get(trueList).setResult(String.valueOf(M1 + 100));
            codes.get(falseList).setResult(String.valueOf(M2 + 100));
            if(stmt1 != -1)
                codes.get(stmt1).setResult(String.valueOf(N + 100));
            if(stmt2 != -1){
                codes.get(stmt2).setResult(String.valueOf(stmt2+100));
                symbols.push(new Symbol(left, "#", "#", -1, -1, -1, stmt2));
            } else {
                symbols.push(new Symbol(left, "#", "#", -1, -1, -1, N));
            }
        } else if(res == 8){
            // stmts -> stmt M
            int M = symbols.pop().getInstr();
            int stmt = symbols.pop().getNextList();
            if(stmt != -1)
                codes.get(stmt).setResult(String.valueOf(M+100));
            symbols.push(new Symbol(left, "#", "#"));
        } else if(res == 9){
            // stmts -> stmts ; M stmt M
            int M2 = symbols.pop().getInstr();
            int stmt = symbols.pop().getNextList();
            symbols.pop();
            if(stmt != -1)
                codes.get(stmt).setResult(String.valueOf(M2 + 100));
            int M1 = symbols.pop().getInstr();
            symbols.pop();
            int stmts = symbols.pop().getNextList();
            if(stmt != -1)
                codes.get(stmt).setResult(String.valueOf(M1 + 100));
            symbols.push(new Symbol(left, "#", "#"));
        }  else if(res == 11){
            // stmt -> while M bool do M stmt
            int stmt1 = symbols.pop().getNextList();
            int M2 = symbols.pop().getInstr();
            symbols.pop();
            int trueList = symbols.peek().getTrueList();
            int falseList = symbols.pop().getFalseList();
            int M1 = symbols.pop().getInstr();
            symbols.pop();
            if(stmt1 != -1)
                codes.get(stmt1).setResult(String.valueOf(M1 + 100));
            codes.get(trueList).setResult(String.valueOf(M2 + 100));
            symbols.push(new Symbol(left, "#", "#", -1, -1, -1, falseList));
            codes.add(new Code("goto", "#", "#", String.valueOf(M1 + 100)));
        } else if(res == 12){
            // stmt -> repeat M stmts until bool
            // TODO 我们没有这个文法
            int trueList = symbols.peek().getTrueList();
            int falseList = symbols.pop().getFalseList();
            symbols.pop();
            symbols.pop();
            int M = symbols.pop().getInstr();
            symbols.pop();
            codes.get(falseList).setResult(String.valueOf(M + 100));
            symbols.push(new Symbol(left, "#", "#", -1, -1, -1, trueList));
        } else if(res == 13){
            // stmt -> for id := expr to expr do stmt
            // stmt -> for id := expr downto expr do stmt
            // TODO 需要设计翻译方案
            int stmt = symbols.pop().getNextList();
            symbols.pop();
            String expr2 = symbols.pop().getAddr();
            String texOp = symbols.pop().getFirst();
            String expr1 = symbols.pop().getAddr();
            symbols.pop();
            String id = symbols.pop().getSecond();
            symbols.pop();
            codes.add(new Code("=", expr1, "#", id));
            int begin = codes.size();
            String op = (texOp.equals("to") ? "<" : ">");
            codes.add(new Code(op, expr1, expr2, String.valueOf(codes.size()+2)));
            codes.add(new Code("goto", "#", "#", String.valueOf(stmt + 100)));

        } else {
            for(int i = 0;i < l;i++)
                symbols.pop();
            symbols.push(new Symbol(left, "#", "#"));
        }
    }

    public void print(){
        for(int i = 0;i < codes.size();i++){
            StringBuilder s = new StringBuilder();
            Code x = codes.get(i);
            int addr = i+100;
            s.append(addr).append(": ");
            if(x.getOp().equals("+") || x.getOp().equals("-") || x.getOp().equals("*") || x.getOp().equals("/")){
                s.append(x.getResult()).
                        append(" = ").
                        append(x.getArg1()).
                        append(" ").
                        append(x.getOp()).
                        append(" ").append(x.getArg2()).append(";");
            } else if(x.getOp().equals("=")){
                s.append(x.getResult()).append(" = ").append(x.getArg1()).append(";");
            } else if(x.getOp().equals("<") || x.getOp().equals(">")){
                s.append("if ").
                        append(x.getArg1()).
                        append(" ").
                        append(x.getOp()).
                        append(" ").
                        append(x.getArg2()).
                        append(" goto ").append(x.getResult()).append(";");
             }else if(x.getOp().equals("goto")){
                s.append("goto ").append(x.getResult()).append(";");
            }else
                continue;
            System.out.println(s);
        }
        System.out.println(codes.size()+100+": END ;");
    }
}
