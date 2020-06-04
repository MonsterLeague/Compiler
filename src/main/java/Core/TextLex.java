package Core;

import javafx.util.Pair;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.HashMap;


public class TextLex{

    private String text;
    private String originText;
    private DefaultTableModel tbmodel_lex_result;
    private DefaultTableModel tbmodel_lex_error;
    private DefaultTableModel tbmodel_lex_table;
    private ArrayList<String> lex_result_stack;
    private ArrayList<String> lex_table_stack;
    private ArrayList<HashMap<String, String>> lex_error_stack;
    private int text_length;
    private int row_number=1;
    private static final String[] Key = {"program", "var", "integer", "cardinal", "shortint", "smallint", "longint", "int64", "byte",
            "word", "longword", "array", "of", "and", "begin", "end", "case", "const", "div", "do", "downto",
            "if", "else", "file", "for", "function", "goto", "in", "label", "mod", "nil", "not", "or", "packed",
            "procedure", "recode", "repeat", "set", "then", "to", "type", "until", "while", "with", "boolean", "true", "false"};

    public TextLex(String text, DefaultTableModel tb_lex_result, DefaultTableModel tb_lex_error, DefaultTableModel tb_lex_table){
        lex_result_stack = new ArrayList<>();
        lex_error_stack = new ArrayList<>();
        this.originText = text;
        this.text = text.toLowerCase();
        this.tbmodel_lex_result = tb_lex_result;
        this.tbmodel_lex_error = tb_lex_error;
        this.tbmodel_lex_table = tb_lex_table;
        text_length = text.length();
    }

    public int isAlpha(char c){
        if(((c<='z')&&(c>='a')) || ((c<='Z')&&(c>='A')) || (c=='_'))
            return 1;
        else
            return 0;
    }

    public int isNumber(char c){
        if(c>='0' && c<='9')
            return 1;
        else
            return 0;
    }

    public int isKey(String t){
        for(int i=0;i<Key.length;i++){
            if (t.equals(Key[i])) {
                return 1;
            }
        }
        // 只是普通的标识符
        return 0;
    }

    // 处理整个字符串
    public void scannerAll(){
        int i=0;
        char c;
        text = text+'\0';
        originText = originText+'\0';
        while(i<text_length){
            c = text.charAt(i);
            if(c==' '||c=='\t')
                i++;
            else if (c=='\n') {
                printResult("ENTER", null, "<ENTER>");
                row_number++;
                i++;
            }
            else
                i=scannerPart(i);
        }
    }

    public int scannerPart(int arg0){
        int i=arg0;
        char ch = text.charAt(i);
        String s="";
        // 第一个输入的字符是字母
        if (isAlpha(ch)==1) {
            s = ""+ch;
            return handleFirstAlpha(i, s);
        }
        // 第一个是数字的话
        else if (isNumber(ch)==1) {
            s = ""+ch;
            return handleFirstNum(i, s);

        }
        // 既不是既不是数字也不是字母
        else {
            s = ""+ch;
            switch (ch) {
                case ' ':
                case '\n':
                case '\r':
                case '\t':
                    return ++i;
                case '\'':
                    return handleChar(ch, i, s);
                case '\"':
                    // 判定字符串
                    return handleString(ch, i, s);
                case '+':
                case '-':
                case '*':
                case '/':
                    if(text.charAt(i+1) == '='){
                        ch = text.charAt(++i);
                        s = s+ch;
                        printResult(s, null, "<"+s+">");
                        return ++i;
                    }else if(ch == '/' && text.charAt(i+1)=='/'){
                        //单行注释
                        return handleSingleLineNote(i,s);
                    }else {
                        printResult(s, null, "<"+s+">");
                        return ++i;
                    }
                case '{':
                    //多行注释
                    return handleNote(i, s);
                case '<':
                    if(text.charAt(i+1) == '<'){
                        ch = text.charAt(++i);
                        s = s+ch;
                        printResult(s, null, "<"+s+">");
                        return ++i;
                    }
                case '>':
                    if(text.charAt(i+1) == '>'){
                        ch = text.charAt(++i);
                        s = s+ch;
                        printResult(s, null, "<"+s+">");
                        return ++i;
                    }
                case ':':
                    if(text.charAt(i+1) == '='){
                        ch = text.charAt(++i);
                        s = s+ch;
                        printResult(s, null, "<"+s+">");
                        return ++i;
                    }
                case '=':
                case '[':
                case ']':
                case '(':
                case ')':
                case ',':
                case '.':
                case ';':
                case '!':
                case '@':
                case '#':
                case '$':
                case '^':
                case '&':
                    printResult(s, null, "<"+s+">");
                    return ++i;
                case '}':
                    printError(row_number, s, "注释没有闭合");
                    return ++i;
                default:
                    // 输出暂时无法识别的字符,制表符也被当成了有问题的字符
                    printError(row_number, s, "非法字符");
                    return ++i;
            }
        }
    }

    public int handleFirstAlpha(int arg, String arg0){
        int i=arg;
        String s = arg0;
        char ch=text.charAt(++i);
        while(isAlpha(ch)==1 || isNumber(ch)==1){
            s = s+ch;
            ch=text.charAt(++i);
        }
        if(s.length()==1){
            printResult(s, "id", "<id, "+s+">");
            return i;
        }
        // 到了结尾
        if(isKey(s)==1){
            // 输出key
            printResult(s, null, "<"+s+">");
            return i;

        }
        else {
            // 输出普通的标识符
            printResult(s, "id", "<id, "+s+">");
            return i;
        }
    }

    public int ScientificNum(int arg, String arg0){
        int i = arg;
        char ch = text.charAt(++i);
        String s = arg0;
        if (ch == '-' || ch == '+' || isNumber(ch)==1) {
            if(ch == '-' || ch == '+'){
                s = s+ch;
                ch = text.charAt(++i);
            }
            while (isNumber(ch)==1) {
                s = s+ch;
                ch = text.charAt(++i);
            }
            if(isNumber(text.charAt(i-1)) == 1){
                printResult(s, "num", "<num, "+s+">");
                return i;
            }
            else {
                printError(row_number, s, "浮点数错误");
                return i-1;
            }
        }
        else {
            printError(row_number, s, "科学计数法错误");
            return i;
        }
    }

    public int wordToNum(char s){
        if(s >= '0' && s <= '9')
            return s-'0';
        return s-'a'+10;
    }

    public String changeNumToTen(int begin, int lim, String num){
        int l = num.length();
        long res = 0;
        for(int i = begin;i < l;i++){
            res += wordToNum(num.charAt(i))*Math.pow(lim, l-i-1);
            if(res >= 2147483648l){
                return "Nan";
            }
        }
        return String.valueOf(res);
    }

    public int customizeNum(int begin, int lim, int arg, String arg0){
        int i = arg;
        char ch = text.charAt(++i);
        String s = arg0;
        int maxt = 0;
        while((ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'z')){
            int t = wordToNum(ch);
            s = s+ch;
            maxt = Math.max(maxt, t);
            ch = text.charAt(++i);
        }
        if(s.equals("0x")){
            printError(row_number, s, "未知符号");
            return i;
        }
        if(lim > 0){
            if(maxt >= lim){
                printError(row_number, s, "整数进制溢出");
                return i;
            }
            maxt = lim;
        } else if(maxt < 2) maxt = 2;
        else if(maxt < 8) maxt = 8;
        else if(maxt < 10) maxt = 10;
        else maxt = 16;
        String tens = changeNumToTen(begin, maxt, s);
        if(tens.equals("Nan"))
            printError(row_number, s, "整数越界");
        else
            printResult(tens, "num", "<num, "+tens+">");
        return i;
    }

    public int handleFirstNum(int arg, String arg0){
        int i = arg;
        char ch = text.charAt(i);
        String s = arg0;
        if(ch == '0' && text.charAt(i+1) != '.'){
            //自定义整数
            ch = text.charAt(++i);
            if(ch == '0'){
                //自适应
                s = s+ch;
                return customizeNum(2, -1, i, s);
            } else if(ch == 'x'){
                //十六进制
                s = s+ch;
                return customizeNum(2, 16, i, s);
            }else{
                //八进制
                return customizeNum(1, 8, i-1, s);
            }
        }
        ch = text.charAt(++i);
        while(isNumber(ch)==1){
            s = s+ch;
            ch = text.charAt(++i);
        }
        if (ch=='e') {
            return ScientificNum(i, s+ch);
        }
        // 浮点数判断
        else if (text.charAt(i)=='.'&&(isNumber(text.charAt(i+1))==1)) {
            s = s +'.';
            ch = text.charAt(++i);
            while (isNumber(ch)==1) {
                s = s+ch;
                ch = text.charAt(++i);
            }
            if (ch=='e') {
                return ScientificNum(i, s+ch);
            }
            else if(isAlpha(ch) == 1){
                while(isAlpha(ch) == 1){
                    s = s+ch;
                    ch = text.charAt(++i);
                }
                printError(row_number, s, "不是合法语句");
                return i;
            } else{
                printResult(s, "num", "<num, "+s+">");
                return i;
            }
        }else if(isAlpha(ch) == 1){
            while(isAlpha(ch) == 1){
                s = s+ch;
                ch = text.charAt(++i);
            }
            printError(row_number, s, "不是合法语句");
            return i;
        }
        else {
            // 到了结尾，输出数字
            String chk = changeNumToTen(0, 10, s);
            if(chk.equals("Nan"))
                printError(row_number, s, "整数越界");
            else
                printResult(s, "num", "<num, "+s+">");
            return i;
        }
    }
    public int handleChar(char begin, int arg, String arg0){
        String s = arg0;
        int i = arg;
        char ch = originText.charAt(++i);
        while(ch!='\''){
            if(ch == '\\')
                ch = originText.charAt(++i);
            if(ch=='\n'||ch=='\0'){
                printError(row_number, s, "单字符错误");
                return i;
            }
            s = s+ch;
            ch = originText.charAt(++i);
        }
        s = s+ch;
        if(s.length() < 3) {
            printError(row_number, s, "单字符错误");
            return ++i;
        }
        if(s.length() > 3) return handleString(begin, arg, arg0);
        printResult(s, "char", "<char, "+s+">");
        return ++i;
    }

    // 单行注释处理
    public int handleSingleLineNote(int arg, String arg0){
        String s = arg0;
        int i = arg;
        char ch = originText.charAt(++i);
        while (ch!='\n'&&ch!='\0') {
            s = s+ch;
            ch = originText.charAt(++i);
        }
        //printResult(s, "单行注释");
        return i;
    }

    // 字符串处理
    public int handleString(char begin, int arg, String arg0){
        String s = arg0;
        int i=arg;
        char ch = originText.charAt(++i);
        while(ch!=begin){
            if(ch == '\\'){
                ch = originText.charAt(++i);
            }
            if(ch=='\n'||ch=='\0'){
                printError(row_number, s, "字符串没有闭合");
                return i;
            }
            s = s+ch;
            ch = originText.charAt(++i);
        }
        s = s+ch;
        printResult(s, "string", "<string, "+s+">");
        return ++i;
    }

    // 处理注释,没有考虑不闭合的情况
    public int handleNote(int arg, String arg0){
        int i = arg;
        char ch=originText.charAt(++i);
        String s = arg0+ch;
        while (ch!='}') {
            s = s+ch;
            if (ch=='\n') {
                printResult("ENTER", null, "<ENTER>");
                row_number++;
            }
            else if (ch=='\0') {
                printError(row_number, s, "注释没有闭合");
                return i;
            }
            ch = originText.charAt(++i);
        }
        s = s+"}";
        //printResult(s, "注释");
        return ++i;
    }

    public void printResult(String rs_value, String type, String rs_name){
        if (!rs_value.equals("ENTER")) {
            tbmodel_lex_result.addRow(new String[]{rs_name});
        }
        if(type != null) {
            lex_result_stack.add(type);
            lex_table_stack.add(rs_value);
            tbmodel_lex_table.addRow(new String[]{type, rs_value});
            //System.out.println(type + ',' + rs_value);
        }else
            lex_result_stack.add(rs_value);
    }

    public void printError(int row_num, String rs_value, String rs_name) {
        HashMap<String, String> t = new HashMap<>();
        t.put("row_num", row_num+"");
        t.put("rs_value", rs_value+"");
        t.put("rs_name", rs_name+"");
        lex_error_stack.add(t);
        tbmodel_lex_error.addRow(new String[]{row_num+"", rs_value, rs_name});
    }

    public ArrayList<String> get_Lex_Result() {
        return lex_result_stack;
    }

    public ArrayList<HashMap<String, String>> get_Lex_Error() {
        return lex_error_stack;
    }
}