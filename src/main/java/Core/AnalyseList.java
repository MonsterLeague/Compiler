package Core;

import javafx.util.Pair;

import java.io.*;
import java.util.*;

public class AnalyseList{

    // 成员变量,产生式集，终结符集，非终结符集
    ArrayList<Production> productions;
    ArrayList<String> terminals;
    ArrayList<String> nonterminals;
    ArrayList<String> allText;
    ArrayList<Set<Item>> C;			//项集族
    HashMap<String, ArrayList<String>> firsts;
    HashMap<String, ArrayList<String>> follows;
    HashMap<Pair<Integer, String>, Pair<Integer, Integer>> actions;
    HashMap<Pair<Integer, String>, Integer> gotos;
    Map<String, Integer> operator;

    public void setOperator(){
        operator = new HashMap<>();
        operator.put(":=", 0);
        operator.put("<", 1);
        operator.put(">", 1);
        operator.put("^", 2);
        operator.put("+", 3);
        operator.put("-", 3);
        operator.put("*", 4);
        operator.put("/", 4);
    }

    public AnalyseList(){
        productions = new ArrayList<Production>();
        terminals = new ArrayList<>();
        nonterminals = new ArrayList<>();
        firsts = new HashMap<>();
        follows = new HashMap<>();
        actions = new HashMap<>();
        gotos = new HashMap<>();
        setOperator();

        setProductions();
        setNonTerminals();
        setTerminals();
        allText = new ArrayList<>();
        allText.addAll(terminals);
        allText.addAll(nonterminals);

        getFirst();
        getFollow();

        getActionAndGoto();
    }

    // 从文件中读取产生式
    public void setProductions(){
        try {
            File file = new File("grammar.txt");
            RandomAccessFile randomfile = new RandomAccessFile(file, "r");
            String line;
            String left;
            String right;
            Production production;
            while ((line=randomfile.readLine())!=null) {
                left = line.split("->")[0].trim();
                right = line.split("->")[1].trim();
                production = new Production(left, right.split(" "));
                productions.add(production);
            }
            randomfile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 获得非终结符集
    public void setNonTerminals(){
        try {
            File file = new File("grammar.txt");
            RandomAccessFile randomfile = new RandomAccessFile(file, "r");
            String line;
            String left;
            while ((line=randomfile.readLine())!=null) {
                left = line.split("->")[0].trim();
                if(nonterminals.contains(left)){
                    continue;
                }
                else {
                    nonterminals.add(left);
                }
            }
            randomfile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 获得终结符集,依赖于获得产生式函数
    public void setTerminals(){
        // 遍历所有的产生式
        String[] rights;
        Set<String> t = new HashSet<>();
        for (int i = 0; i < productions.size(); i++) {
            rights = productions.get(i).returnRights();
            // 从右侧寻找终结符
            for (int j = 0; j < rights.length; j++) {
                if(nonterminals.contains(rights[j])||rights[j].equals("epsilon")){
                    continue;
                }
                else {
                    t.add(rights[j]);
                }
            }
        }
        terminals.addAll(t);
    }

    // 获取First集
    public void getFirst(){
        // 终结符全部求出first集
        ArrayList<String> first;
        for (int i = 0; i < terminals.size(); i++) {
            first = new ArrayList<String>();
            first.add(terminals.get(i));
            firsts.put(terminals.get(i), first);
        }
        // 给所有非终结符注册一下
        for (int i = 0; i < nonterminals.size(); i++) {
            first = new ArrayList<String>();
            firsts.put(nonterminals.get(i), first);
        }

        boolean flag;
        while (true) {
            flag = true;
            String left;
            String right;
            String[] rights;
            for (int i = 0; i < productions.size(); i++) {
                left = productions.get(i).returnLeft();
                rights = productions.get(i).returnRights();
                for (int j = 0; j < rights.length; j++) {
                    right = rights[j];
                    // right是否存在，遇到空怎么办
                    if(!right.equals("epsilon")){
                        for (int l = 0; l < firsts.get(right).size(); l++) {
                            if(firsts.get(left).contains(firsts.get(right).get(l))){
                                continue;
                            }
                            else {
                                firsts.get(left).add(firsts.get(right).get(l));
                                flag=false;
                            }
                        }
                    }
                    if (isCanBeNull(right)) {
                        continue;
                    }
                    else {
                        break;
                    }
                }
            }
            if (flag==true) {
                break;
            }

        }
        // 非终结符的first集
    }

    // 获得Follow集
    public void getFollow(){
        // 所有非终结符的follow集初始化一下
        ArrayList<String> follow;
        for (int i = 0; i < nonterminals.size(); i++) {
            follow = new ArrayList<String>();
            follows.put(nonterminals.get(i), follow);
        }
        // 将#加入到follow(S)中
        follows.get("S'").add("$");
        boolean flag;
        boolean fab;
        while (true) {
            flag = true;
            // 循环
            for (int i = 0; i < productions.size(); i++) {
                String left;
                String right;
                String[] rights;
                rights = productions.get(i).returnRights();
                for (int j = 0; j < rights.length; j++) {
                    right = rights[j];

                    // 非终结符
                    if (nonterminals.contains(right)) {
                        fab = true;
                        for(int k=j+1;k<rights.length;k++){

                            // 查找first集
                            for(int v=0;v<firsts.get(rights[k]).size();v++){
                                // 将后一个元素的first集加入到前一个元素的follow集中
                                if(follows.get(right).contains(firsts.get(rights[k]).get(v))){
                                    continue;
                                }
                                else {
                                    follows.get(right).add(firsts.get(rights[k]).get(v));
                                    flag=false;
                                }
                            }
                            if (isCanBeNull(rights[k])) {
                                continue;
                            }
                            else {
                                fab = false;
                                break;
                            }
                        }
                        if(fab){
                            left = productions.get(i).returnLeft();
                            for (int p = 0; p < follows.get(left).size(); p++) {
                                if (follows.get(right).contains(follows.get(left).get(p))) {
                                    continue;
                                }
                                else {
                                    follows.get(right).add(follows.get(left).get(p));
                                    flag = false;
                                }
                            }
                        }
                    }

                }
            }
            if(flag==true){
                break;
            }
        }

    }

    public Set<Item> cloneSet(Set<Item> x){
        return new HashSet<>(x);
    }

    public Set<Item> CLOSURE(Set<Item> I){
        Set<Item> J = cloneSet(I);
        while (true){
            int l = J.size();
            for(Item i:J){
                if(i.getCurl() >= i.returnRights().length)
                    continue;
                String now = i.returnRights()[i.getCurl()];
                for(Production j:productions){
                    if(!j.returnLeft().equals(now))
                        continue;
                    I.add(new Item(j, 0));
                }
            }
            J = cloneSet(I);
            if(J.size() == l) break;
        }
        return J;
    }

    public Set<Item> GOTO(Set<Item> I, String X){
        Set<Item> J = new HashSet<>();
        for(Item i:I){
            if(i.getCurl() >= i.returnRights().length)
                continue;
            String now = i.returnRights()[i.getCurl()];
            if(now.equals(X))
                J.add(new Item(i.returnLeft(), i.returnRights(), i.getCurl()+1));
        }
        return CLOSURE(J);
    }

    public HashMap<Pair<Integer, String>, Integer> getC(){
        //将初始状态放入集合
        C = new ArrayList<>();												//初始化项集族
        Set<Item> begin = new HashSet<>();
        begin.add(new Item(productions.get(0), 0));
        C.add(CLOSURE(begin));
        HashMap<Pair<Integer, String>, Integer> tgoto = new HashMap<>();	//临时转移表
        //计算文法的项集族
        for(int i = 0;i < C.size();i++){									//遍历已经计算的项集族
            Set<Item> I = C.get(i);
            for(String j : allText){									//遍历所有非终结符
                Set<Item> J = GOTO(I, j);									//计算项目集闭包
                if(J.isEmpty())												//如果项目集闭包为空则跳过
                    continue;
                boolean flag = false;
                for(int k = 0;k < C.size();k++){
                    Set<Item> K = C.get(k);
                    if(K.equals(J)){												//如果该项目集闭包出现过
                        flag = true;
                        tgoto.put(new Pair<>(i, j), k);						//记录GOTO(i, X) = j
                        break;
                    }
                }
                if(!flag){
                    C.add(J);												//将项目集闭包放入项集族
                    tgoto.put(new Pair<>(i, j), C.size()-1);				//记录GOTO(i, X) = j
                }
            }
        }
        return tgoto;
    }

    public void check(){
        boolean flag = false;
        for(Set<Item> i:C){
            ArrayList<String> t = new ArrayList<>();
            for(Item j:i){
                if(j.getCurl() != j.returnRights().length){
                    t.add(j.returnRights()[j.getCurl()]);
                }
            }
            for(Item j:i){
                if(j.getCurl() == j.returnRights().length){
                    for(String K:follows.get(j.returnLeft())){
                        if(t.contains(K)){
                            flag = true;
                        }
                    }
                }
            }
        }
        for(Map.Entry<String, ArrayList<String>> i : follows.entrySet()){
            for(Map.Entry<String, ArrayList<String>> j : follows.entrySet()){
                if(i.getKey() == j.getKey())
                    continue;
                ArrayList<String> x = (ArrayList<String>) j.getValue().clone();
                x.retainAll(i.getValue());
                if(!x.isEmpty())
                    flag = true;
            }
        }
        if(flag)
            System.out.println("非SLR");
    }

    public void getActionAndGoto(){
        HashMap<Pair<Integer, String>, Integer> tgoto = getC();
        //计算Action表和Goto表
        for(int i = 0;i < C.size();i++){
            HashMap<String, Integer> R = new HashMap<>(), S = new HashMap<>();
            Set<Item> I = C.get(i);
            for(Item it : I){
                if(it.returnRights()[0].equals("epsilon") || it.getCurl() >= it.returnRights().length){				//如果 点 在最右边
                    if(it.returnRights()[0].equals("epsilon") || !it.returnRights()[it.getCurl()-1].equals("S")){		//如果 点 不在开始符号S的右边
                        int m = 0;											//找到对应的归约规则
                        for(int j = 0;j < productions.size();j++){
                            Production p = productions.get(j);
                            if(p.equals(it)){
                                m = j;
                                break;
                            }
                        }
                        for(String s : follows.get(it.returnLeft())){		//遍历归约规则m的follow集
                            R.put(s, m);
                            //actions.put(new Pair<>(i, s), new Pair<>(0, m));//0表示规约
                        }
                    }else													//如果 点 在开始符号S的右边
                        actions.put(new Pair<>(i, "$"), new Pair<>(-1, -1));//-1表示接受
                }else{														//如果 点 右边存在字符X
                    String X = it.returnRights()[it.getCurl()];
                    if(terminals.contains(X)){								//如果X是终结符
                        S.put(X, tgoto.get(new Pair<>(i, X)));
                        //actions.put(t, new Pair<>(1, tgoto.get(t)));		//1表示移进
                    }else{													//如果X是非终结符
                        gotos.put(new Pair<>(i, X), tgoto.get(new Pair<>(i, X)));
                    }
                }
            }
            for(String s : allText){
                ambiguity(i, I, S, R, s);
            }
            ambiguity(i, I, S, R, "$");
        }
        outputSVC();
    }

	/*
program test;
begin
y:=y+y^5-1
end.
	 */

    public void ambiguity(int i, Set<Item> I, Map<String, Integer> S, Map<String, Integer> R, String s){
        if(R.get(s) == null && S.get(s) == null)
            return;
        if(R.get(s) == null){
            actions.put(new Pair<>(i, s), new Pair<>(1, S.get(s)));
        }else if(S.get(s) == null){
            actions.put(new Pair<>(i, s), new Pair<>(0, R.get(s)));
        }else{
            if(s.equals("else"))
                actions.put(new Pair<>(i, s), new Pair<>(1, S.get(s)));
            else if(operator.get(s) != null){
                String t = null;
                for(Item it:I){
                    if(it.getCurl() >= 2 && it.getCurl() >= it.returnRights().length){
                        t = it.returnRights()[it.getCurl()-2];
                        break;
                    }
                }
                if(t == null) return;
                if(operator.get(s) <= operator.get(t)){
                    if(t.equals(":="))
                        actions.put(new Pair<>(i, s), new Pair<>(1, S.get(s)));
                    else
                        actions.put(new Pair<>(i, s), new Pair<>(0, R.get(s)));
                }else
                    actions.put(new Pair<>(i, s), new Pair<>(1, S.get(s)));
            }
        }
    }

    // 生成产生式
    public void outputSVC(){
        String line;
        try {
            File file = new File("action_goto.csv");
            OutputStreamWriter os = new OutputStreamWriter(new FileOutputStream(file), "gbk");
            line = "状态,action";
            for(int i = 0;i < terminals.size()+1;i++)
                line += ",";
            line += "goto";
            for(int i = 0;i < nonterminals.size();i++)
                line += ",";
            os.write(line+"\n");
            line = "字符,";
            for (String i:terminals){
                line += i+",";
            }
            line += "$,";
            for(String i:nonterminals){
                line += i+",";
            }
            os.write(line+"\n");
            for(int i = 0;i < C.size();i++){
                line = i +",";
                for (String j:terminals){
                    Pair<Integer, Integer> X = actions.get(new Pair<>(i, j));
                    String t = "";
                    if(X == null) t = ""/*"err"*/;
                    else if(X.getKey() == 0) t = "r"+X.getValue();
                    else t = "s"+X.getValue();
                    line += t+",";
                }
                if(actions.get(new Pair<>(i, "$")) != null){
                    Pair<Integer, Integer> X = actions.get(new Pair<>(i, "$"));
                    String t = "";
                    if(X.getKey() == -1) t = "acc";
                    else if(X.getKey() == 0) t = "r"+X.getValue();
                    else t = "s"+X.getValue();
                    line += t+",";
                }
                else
                    line += ",";
                for(String j:nonterminals){
                    Integer X = gotos.get(new Pair<>(i, j));
                    String t = "";
                    if(X == null) t = ""/*err*/;
                    else t = String.valueOf(X);
                    line += t+",";
                }
                os.write(line+"\n");
            }
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 判断是否产生epsilon
    public boolean isCanBeNull(String symbol){
        String[] rights;
        for (int i = 0; i < productions.size(); i++) {
            // 找到产生式
            if (productions.get(i).returnLeft().equals(symbol)) {
                rights = productions.get(i).returnRights();
                if (rights[0].equals("epsilon")) {
                    return true;
                }
            }
        }
        return false;
    }
}