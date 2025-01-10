package org.example;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AddressBookTest {

    private AddressBook a;
    @Before
    public void setUp() throws Exception {
        BuddyInfo b1 = new BuddyInfo("Messi", "10");
        BuddyInfo b2 = new BuddyInfo("Bellingham", "5");

        a = new AddressBook();
        a.addBuddy(b1);
        a.addBuddy(b2);
    }

    @After
    public void tearDown() throws Exception {
        a.emptyAddressBook();
        a = null;
    }

    @Test
    public void testAddBuddy() {
        BuddyInfo b = new BuddyInfo("Salah", "11");
        a.addBuddy(b);
        assertEquals(3, a.getBuddies().size());
        assertEquals(b, a.getBuddy(a.getBuddies().size() - 1));
    }

    @Test
    public void testRemoveBuddy(){
        BuddyInfo b = a.removeBuddy(0);
        assertEquals("Messi", b.getName());
    }
}