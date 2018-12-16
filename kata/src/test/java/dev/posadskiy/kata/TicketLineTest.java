package dev.posadskiy.kata;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TicketLineTest {
    @Test
    public void test1() {
        assertEquals("NO", TicketLine.Tickets(new int[] {25, 25, 50, 50, 100}));
    }
    @Test
    public void test2() {
        assertEquals("NO", TicketLine.Tickets(new int []{25, 25, 25, 25, 25, 100, 100}));
    }
    @Test
    public void test3() {
        assertEquals("NO", TicketLine.Tickets(new int []{100, 50, 25, 25}));
    }
    @Test
    public void test4() {
        assertEquals("YES", TicketLine.Tickets(new int []{25, 25, 50}));
    }
    @Test
    public void test5() {
        assertEquals("NO", TicketLine.Tickets(new int []{25, 100}));
    }
    @Test
    public void test6() {
        assertEquals("NO", TicketLine.Tickets(new int []{25, 100}));
    }
    @Test
    public void test7() {
        assertEquals("YES", TicketLine.Tickets(new int []{25, 25, 25, 25, 50, 100, 50}));
    }

}