package dev.dposadsky.spring.performers;

public class Juggler implements Performer {
    private int beanBags = 3;

    public Juggler() {}

    public Juggler(int beanBags) {
        this.beanBags = beanBags;
    }

    public void perform() throws IllegalArgumentException {
        System.out.println("JUGGLING " + beanBags + " BEANBAGS");
    }
}
