package com.company.reiziger;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.Date;
import java.util.List;

public class ReizigerDAOHibernate implements ReizigerDAO{

    private Session session;

    public ReizigerDAOHibernate(Session session) {
        this.session = session;
    }

    /**
     * deze methode slaat reizigers op in de database
     */
    @Override
    public boolean save(Reiziger reiziger) {
        try {
            session.beginTransaction();
            session.persist(reiziger);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * deze methode veranderd de data van een reiziger in de database
     */
    @Override
    public boolean update(Reiziger reiziger) {
        try {
            session.beginTransaction();
            session.update(reiziger);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * deze methode verwijderd de reiziger uit de database
     */
    @Override
    public boolean delete(Reiziger reiziger) {
        try {
            session.beginTransaction();
            session.delete(reiziger);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * deze methode zoekt een reiziger in de database via zijn id
     */
    @Override
    public Reiziger findById(int id) {
        try {

            return session.find(Reiziger.class, id);

        } catch (Exception exception) {
            exception.printStackTrace();

        }
        return null;
    }

    /**
     * deze methode zoekt een reiziger in de database via zijn geboortedatum
     */
    @Override
    public List<Reiziger> findByGbdatum(String datum) {
        try {

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Reiziger> criteria = builder.createQuery(Reiziger.class);
            Root<Reiziger> p = criteria.from(Reiziger.class);
            criteria.select(p);
            criteria.where(builder.equal(p.get("geboortedatum"), Date.valueOf(datum)));

            return session.createQuery(criteria).getResultList();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    /**
     * deze methode geeft alle reizigers in de database
     */
    @Override
    public List<Reiziger> findAll() {

        try {

            return session.createQuery("from Reiziger",Reiziger.class).getResultList();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }
    }
