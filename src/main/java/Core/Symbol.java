package Core;

public class Symbol {
    private String first;
    private String second;
    private String addr;
    private int trueList;
    private int falseList;
    private int instr;
    private int nextList;

    public Symbol() { }

    public Symbol(String first, String second, String addr, int trueList, int falseList, int instr, int nextList){
        this.first = first;
        this.second = second;
        this.addr = addr;
        this.trueList = trueList;
        this.falseList = falseList;
        this.instr = instr;
        this.nextList = nextList;
    }

    public Symbol(String first, String second, String addr, int trueList, int falseList, int instr){
        this(first, second, addr, trueList, falseList, instr, -1);
    }

    public Symbol(String first, String second, String addr, int trueList, int falseList) {
        this(first, second, addr, trueList, falseList, -1, -1);
    }

    public Symbol(String first, String second, String addr, int trueList){
        this(first, second, addr, trueList, -1, -1, -1);
    }

    public Symbol(String first, String second, String addr){
        this(first, second, addr, -1, -1, -1, -1);
    }

    public String getFirst() {
        return first;
    }

    public Symbol setFirst(String first) {
        this.first = first;
        return this;
    }

    public String getSecond() {
        return second;
    }

    public Symbol setSecond(String second) {
        this.second = second;
        return this;
    }

    public String getAddr() {
        return addr;
    }

    public Symbol setAddr(String addr) {
        this.addr = addr;
        return this;
    }

    public int getTrueList() {
        return trueList;
    }

    public Symbol setTrueList(int trueList) {
        this.trueList = trueList;
        return this;
    }

    public int getFalseList() {
        return falseList;
    }

    public Symbol setFalseList(int falseList) {
        this.falseList = falseList;
        return this;
    }

    public int getInstr() {
        return instr;
    }

    public Symbol setInstr(int instr) {
        this.instr = instr;
        return this;
    }

    public int getNextList() {
        return nextList;
    }

    public Symbol setNextList(int nextList) {
        this.nextList = nextList;
        return this;
    }
}
