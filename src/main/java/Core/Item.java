package Core;

import java.util.Objects;

public class Item extends Production {
    int curl;

    public Item(String left, String[] right, int curl) {
        super(left, right);
        this.curl = curl;
    }

    public Item(Production production, int curl){
        super(production.left, production.right);
        this.curl = curl;
    }

    public int getCurl() {
        return curl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Item item = (Item) o;
        return curl == item.curl;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), curl);
    }
}
