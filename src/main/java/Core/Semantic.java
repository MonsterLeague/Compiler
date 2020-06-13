package Core;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Stack;

public class Semantic {
    private Stack<Symbol> symbols;
    private ArrayList<Code> codes;
    private AnalyseList analyseList;
    private int tempNumber;
    private DefaultTableModel tbmodel_expanded_stack;
    private DefaultTableModel tbmodel_addr_code;

    public Semantic() {
        this.symbols = new Stack<>();
        this.codes = new ArrayList<>();
        tempNumber = 0;
    }

    public Semantic(AnalyseList analyseList, DefaultTableModel tbmodel_expanded_stack,
                    DefaultTableModel tbmodel_addr_code){
        this();
        this.analyseList = analyseList;
        this.tbmodel_addr_code = tbmodel_addr_code;
        this.tbmodel_expanded_stack = tbmodel_expanded_stack;
    }

    public String getTemp(){
        return "t"+(++tempNumber);
    }

    public void add(String first, String second){
        symbols.push(new Symbol(first, second, "null"));
        Symbol t = symbols.peek();
        tbmodel_expanded_stack.addRow(new String[]{t.getFirst(), t.getSecond(), t.getAddr(),
                printList(t.getTrueList()), printList(t.getFalseList()), printList(t.getNextList())});
    }

    public void pop(){
        symbols.pop();
    }

    public void analyse(int res, int l){
        String left = analyseList.productions.get(res).returnLeft();
        ArrayList<Symbol> out = new ArrayList<>();
        if(res == 26 || res == 27){
            // factor -> id | number
            String tmp = symbols.peek().getSecond();
            for(int i = 0;i < l;i++)
                symbols.pop();
            symbols.push(new Symbol(left, "null", tmp));
        }else if(res == 25){
            // term -> factor
            // expr -> term
            String tmp = symbols.peek().getAddr();
            for(int i = 0;i < l;i++)
                symbols.pop();
            symbols.push(new Symbol(left, "null", tmp));
        } else if(res >= 20 && res <= 24){
            // 四则运算
            String factor = symbols.pop().getAddr();
            String op = symbols.pop().getFirst();
            String term1 = symbols.pop().getAddr();
            String term = getTemp();
            symbols.push(new Symbol(left, "null", term));
            codes.add(new Code(op, term1, factor, term));
        } else if(res == 6){
            // init -> id := expr
            String expr = symbols.pop().getAddr();
            symbols.pop();
            String id = symbols.pop().getSecond();
            symbols.push(new Symbol(left, "null", id));
            codes.add(new Code("=", expr, "null", id));
        } else if(res == 18 || res == 19){
            // bool -> expr rel expr
            String expr2 = symbols.pop().getAddr();
            String op = symbols.pop().getFirst();
            String expr1 = symbols.pop().getAddr();
            symbols.push(new Symbol(left, "null", "null", codes.size(), codes.size()+1));
            codes.add(new Code(op, expr1, expr2, "goto _"));
            codes.add(new Code("goto", "null", "null", "goto _"));
        } else if(res == 29){
            // M -> epsilon
            symbols.push(new Symbol(left, "null", "null", -1, -1, codes.size()));
        } else if(res == 30){
            // N -> epsilon
            symbols.push(new Symbol(left, "null", "null", -1, -1, -1, codes.size()));
            codes.add(new Code("goto", "null", "null", "goto _"));
        } else if(res == 12){
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
            symbols.push(new Symbol(left, "null", "null", -1, -1, -1, stmt));
        } else if(res == 13){
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
                codes.get(N).setResult(String.valueOf(stmt2+100));
                symbols.push(new Symbol(left, "null", "null", -1, -1, -1, stmt2));
            } else {
                symbols.push(new Symbol(left, "null", "null", -1, -1, -1, N));
            }
        } else if (res == 2) {
            // compound_stmt -> begin stmts M end
            symbols.pop();
            int M = symbols.pop().getInstr();
            int stmts = symbols.pop().getNextList();
            symbols.pop();
            if(stmts != -1)
                codes.get(stmts).setResult(String.valueOf(M + 100));
            symbols.push(new Symbol(left, "null", "null"));
        } else if(res == 3){
            // stmts -> stmt
            symbols.peek().setFirst(left);
        } else if(res == 4){
            // stmts -> stmts M ; stmt
            int stmt = symbols.pop().getNextList();
            symbols.pop();
            int M = symbols.pop().getInstr();
            int stmts = symbols.pop().getNextList();
            if(stmts != -1)
                codes.get(stmts).setResult(String.valueOf(M + 100));
            symbols.push(new Symbol(left, "null", "null", -1, -1, -1, stmt));
        }  else if(res == 10){
            // stmt -> while M bool do M stmt
            int stmt = symbols.pop().getNextList();
            int M2 = symbols.pop().getInstr();
            symbols.pop();
            int trueList = symbols.peek().getTrueList();
            int falseList = symbols.pop().getFalseList();
            int M1 = symbols.pop().getInstr();
            symbols.pop();
            if(stmt != -1)
                codes.get(stmt).setResult(String.valueOf(M1 + 100));
            codes.get(trueList).setResult(String.valueOf(M2 + 100));
            symbols.push(new Symbol(left, "null", "null", -1, -1, -1, falseList));
            codes.add(new Code("goto", "null", "null", String.valueOf(M1 + 100)));
        } else if(res == 14 || res == 15){
            // stmt -> for inc do M stmt M
            // stmt -> for dec do M stmt M
            int M3 = symbols.pop().getInstr();
            int stmt = symbols.pop().getNextList();
            int M2 = symbols.pop().getInstr();
            symbols.pop();
            String op = symbols.peek().getSecond();
            String term = symbols.peek().getAddr();
            int trueList = symbols.peek().getTrueList();
            int falseList = symbols.peek().getFalseList();
            int M1 = symbols.pop().getInstr();
            symbols.pop();
            if(stmt != -1)
                codes.get(stmt).setResult(String.valueOf(M3 + 100));
            codes.get(trueList).setResult(String.valueOf(M2 + 100));
            symbols.push(new Symbol(left, "null", "null", -1, -1, -1, falseList));
            String temp = getTemp();
            codes.add(new Code(op, term, "1", temp));
            codes.add(new Code("=", temp, "null", term));
            codes.add(new Code("goto", "null", "null", String.valueOf(M1 + 100)));
        } else if(res == 16 || res == 17){
            // inc -> init to M expr
            // dec -> init downto M expr
            String expr = symbols.pop().getAddr();
            int M = symbols.pop().getInstr();
            String op = (symbols.pop().getFirst().equals("to") ? "<=" : ">=");
            String init = symbols.pop().getAddr();
            symbols.push(new Symbol(left, (op.equals("<=") ? "+" : "-"), init, codes.size(), codes.size()+1, M));
            codes.add(new Code(op, init, expr, "goto _"));
            codes.add(new Code("goto", "null", "null", "goto _"));
        } else if(res == 5 || (res >= 7 && res <= 9)){
            // stmt -> init | compound_stmt | if_stmt | for_stmt
            symbols.peek().setFirst(left);
        } else if(res == 28){
            // factor -> ( expr )
            symbols.pop();
            String expr = symbols.pop().getAddr();
            symbols.pop();
            symbols.push(new Symbol(left, "null", expr));
        } else {
            for(int i = 0;i < l;i++){
                symbols.pop();
            }
            symbols.push(new Symbol(left, "null", "null"));
        }
        Symbol t = symbols.peek();
        tbmodel_expanded_stack.addRow(new String[]{t.getFirst(), t.getSecond(), t.getAddr(), 
                printList(t.getTrueList()), printList(t.getFalseList()), printList(t.getNextList())});
    }

    public String printList(int list){
        return list == -1 ? "null":list+"";
    }

    public void print(){
        for(int i = 0;i < codes.size();i++){
            StringBuilder s = new StringBuilder();
            Code x = codes.get(i);
            int addr = i+100;
            if(x.getOp().equals("+") || x.getOp().equals("-") || x.getOp().equals("*") || x.getOp().equals("/") || x.getOp().equals("^")){
                s.append(x.getResult()).
                        append(" = ").
                        append(x.getArg1()).
                        append(" ").
                        append(x.getOp()).
                        append(" ").append(x.getArg2());
            } else if(x.getOp().equals("=")){
                s.append(x.getResult()).append(" = ").append(x.getArg1());
            } else if(x.getOp().equals("<") || x.getOp().equals(">") || x.getOp().equals("<=") || x.getOp().equals(">=")){
                s.append("if ").
                        append(x.getArg1()).
                        append(" ").
                        append(x.getOp()).
                        append(" ").
                        append(x.getArg2()).
                        append(" goto ").append(x.getResult());
             }else if(x.getOp().equals("goto")){
                s.append("goto ").append(x.getResult());
            }else
                continue;
            //System.out.println(String.valueOf(addr)+": "+s);
            tbmodel_addr_code.addRow(new String[]{addr+": ", String.valueOf(s)});
        }
        tbmodel_addr_code.addRow(new String[]{(codes.size()+100)+": ", " "});
        //System.out.println(codes.size()+100+": ");
    }

    public int size() {
        return symbols.size();
    }
}
