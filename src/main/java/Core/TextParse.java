package Core;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Stack;

import javax.swing.table.DefaultTableModel;


public class TextParse{
    Stack<Integer> status;
    Stack<String> chars, input;
    Stack<Pair<String, String>> table;
    AnalyseList analyseList;
    ArrayList<String> input_cache;
    ArrayList<String> table_cache;
    DefaultTableModel tbmodel_parse_result;
    DefaultTableModel tbmodel_parse_error;
    Semantic semantic;
    int row_number = 1;

    public TextParse(AnalyseList analyseList, ArrayList<String> input_cache, ArrayList<String> table_cache,
                     DefaultTableModel tbmodel_parse_error, DefaultTableModel tbmodel_parse_result,
                     DefaultTableModel tbmodel_expanded_stack, DefaultTableModel tbmodel_addr_code){
        this.input_cache = input_cache;
        this.table_cache = table_cache;
        this.tbmodel_parse_result = tbmodel_parse_result;
        this.tbmodel_parse_error = tbmodel_parse_error;
        this.status = new Stack<>();
        this.chars = new Stack<>();
        this.input = new Stack<>();
        this.table = new Stack<>();
        this.analyseList = analyseList;
        this.semantic = new Semantic(analyseList, tbmodel_expanded_stack, tbmodel_addr_code);
    }

    public void output(String t){
        Stack<Integer> tmpI = new Stack<>();
        Stack<String> tmpS = new Stack<>();
        String out = "状态栈:", s1 = new String(), s2 = new String(), s3 = new String();
        while (!status.isEmpty()){
            tmpI.push(status.pop());
        }
        while (!tmpI.isEmpty()){
            out = out + tmpI.peek() + " ";
            s1 = s1 + tmpI.peek() + " ";
            status.push(tmpI.pop());
        }
        out = out+"符号栈:";
        while (!chars.isEmpty()){
            tmpS.push(chars.pop());
        }
        s2 = s2 + "$ ";
        while (!tmpS.isEmpty()){
            out = out + tmpS.peek() + " ";
            s2 = s2 + tmpS.peek() + " ";
            chars.push(tmpS.pop());
        }
        out = out+"输入缓冲区:";
        while (!input.isEmpty()){
            if(input.peek() != "ENTER") {
                out = out + input.peek() + " ";
                s3 = s3 + input.peek() + " ";
            }
            tmpS.push(input.pop());
        }
        while (!tmpS.isEmpty()){
            input.push(tmpS.pop());
        }
        out = out + "动作:" + t;
        System.out.println(out);
        tbmodel_parse_result.addRow(new String[]{s1, s2, s3, t});
    }

    // 语法分析
    public void Parsing(){
        status.push(0);
        input.push("$");
        table.push(new Pair<>("$", "$"));
        for(int i = input_cache.size()-1, j = table_cache.size()-1;i >= 0;i--){
            String s = input_cache.get(i);
            input.push(s);
            if(s.equals("num") || s.equals("id")){
                table.push(new Pair<>(s, table_cache.get(j--)));
            } else {
                table.push(new Pair<>(s, s));
            }
        }
        while (!input.empty()){
            int t = status.peek();
            String s = input.peek();
            if(s.equals("ENTER")){
                input.pop();
                table.pop();
                row_number++;
                continue;
            }
            Pair<Integer, Integer> res = analyseList.actions.get(new Pair<>(t, s));
            if(res == null){
                //错误处理
                output("Error");
                String S = "";
                status.pop();
                while (!chars.isEmpty() && !status.isEmpty() &&
                        analyseList.gotos.get(new Pair<>(status.peek(), chars.peek())) == null){
                    status.pop();
                    semantic.pop();
                    S = chars.pop()+" "+S;
                }
                if(status.isEmpty()) status.push(0);
                else status.push(analyseList.gotos.get(new Pair<>(status.peek(), chars.peek())));
                int tmpRow = row_number;
                while (!input.isEmpty() && analyseList.actions.get(new Pair<>(status.peek(), input.peek())) == null) {
                    String tmp = input.pop();
                    table.pop();
                    if (tmp.equals("ENTER")) {
                        row_number++;
                    }
                    else {
                        S = S + " " + tmp;
                    }
                }
                tbmodel_parse_error.addRow(new String[]{String.valueOf(tmpRow),
                        "Undefined Reducible Rule. Last token \""+s+"\". Delete \""+S+"\""});
                continue;
            }
            if(res.getKey() == -1) {
                output("Accept");
                break;
            } else if(res.getKey() == 1){
                output("Shift");
                chars.push(input.pop());
                status.push(res.getValue());
                semantic.add(table.peek().getKey(), table.pop().getValue());
            } else{
                Production pro = analyseList.productions.get(res.getValue());
                output("Reduce/"+pro.toString());
                int l = pro.returnRights().length;
                if(pro.returnRights()[0].equals("epsilon"))
                    l = 0;
                for(int i = 0;i < l;i++){
                    status.pop();
                    chars.pop();
                }
                status.push(analyseList.gotos.get(new Pair<>(status.peek(), pro.returnLeft())));
                chars.push(pro.returnLeft());
                semantic.analyse(res.getValue(), l);
            }
        }
        semantic.print();
    }
}