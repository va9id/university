package org.example;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import javax.persistence.*;
import java.util.List;
public class PersistenceTest {

    private AddressBook a;
    private BuddyInfo b1;
    private BuddyInfo b2;

    @Before
    public void setUp() throws Exception {
        a = new AddressBook();
        b1 = new BuddyInfo("Yusuf", "416");
        b2 = new BuddyInfo("Sami", "42");
        a.addBuddy(b1);
        a.addBuddy(b2);
    }

    @After
    public void tearDown() throws Exception {
        a.emptyAddressBook();
    }
    @Test
    public void TestPersistence() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-test");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();
        //em.persist(b1);
        //em.persist(b2);
        em.persist(a);
        tx.commit();

        Query q1 = em.createQuery("SELECT a FROM AddressBook a");
        Query q2 = em.createQuery("SELECT b FROM BuddyInfo b");

        @SuppressWarnings("unchecked")
        List<AddressBook> res1 = q1.getResultList();

        @SuppressWarnings("unchecked")
        List<BuddyInfo> res2 = q2.getResultList();

        System.out.println("Printing address book result from query");
        for(AddressBook a1: res1) {
            System.out.println(a1.getBuddies());
        }
        assertEquals(a, res1.get(0));

        System.out.println("Printing BuddyInfo result from query");
        for(BuddyInfo b: res2) {
            System.out.println(b);
        }
        em.close();
        emf.close();
    }
}
