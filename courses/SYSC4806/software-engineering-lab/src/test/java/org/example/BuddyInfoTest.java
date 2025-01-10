package org.example;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BuddyInfoTest {

    private BuddyInfo b1;
    private BuddyInfo b2;
    @Before
    public void setUp() throws Exception {
        b1 = new BuddyInfo("Messi", "10");
        b2 = new BuddyInfo("Bellingham", "5");
    }

    @After
    public void tearDown() throws Exception {
        b1 = null;
        b2 = null;
    }

    @Test
    public void testGetName(){
        assertEquals("Messi", b1.getName());
        assertEquals("Bellingham", b2.getName());
    }

    @Test
    public void testGetPhone(){
        assertEquals("10", b1.getPhone());
        assertEquals("5", b2.getPhone());
    }
}