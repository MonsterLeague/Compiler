package Core;

public class Code {
    private String op, arg1, arg2, result;
    public Code() { }
    public Code(String op, String arg1, String arg2, String result){
        this.op = op;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.result = result;
    }

    public String getOp() {
        return op;
    }

    public Code setOp(String op) {
        this.op = op;
        return this;
    }

    public String getArg1() {
        return arg1;
    }

    public Code setArg1(String arg1) {
        this.arg1 = arg1;
        return this;
    }

    public String getArg2() {
        return arg2;
    }

    public Code setArg2(String arg2) {
        this.arg2 = arg2;
        return this;
    }

    public String getResult() {
        return result;
    }

    public Code setResult(String result) {
        this.result = result;
        return this;
    }
}
