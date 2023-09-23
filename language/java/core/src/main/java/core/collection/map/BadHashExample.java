package core.collection.map;

public class BadHashExample {
    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public String toString() {
        return "BadHashExample{}";
    }
}
