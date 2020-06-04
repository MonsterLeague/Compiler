package Core;

import java.util.Arrays;
import java.util.Objects;

// 产生式类
public class Production{
    String left;
    String[] right;

    public Production(String left, String[] right){
        this.left = left;
        this.right = right;
    }

    public String[] returnRights(){
        return right;
    }

    public String returnLeft(){
        return left;
    }

    public boolean equals(Production t){
        return (left == t.left && right == t.right);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Production that = (Production) o;
        return Objects.equals(left, that.left) &&
                Arrays.equals(right, that.right);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(left);
        result = 31 * result + Arrays.hashCode(right);
        return result;
    }

    @Override
    public String toString() {
        String rep = "";
        for(String i:right){
            rep = rep+i+" ";
        }
        return left + " -> " + rep;
    }
}
