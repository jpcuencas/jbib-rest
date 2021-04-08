package org.expertojava.jbibrest.persistencia;

import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.Assert.assertNotNull;

public class TestEmf {

    @Test
    public void createEntityManagerTest() {
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("biblioteca-local");
        EntityManager em = emf.createEntityManager();
        assertNotNull(em);
        em.close();
    }
}