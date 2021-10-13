package com.company.adres;

import com.company.reiziger.Reiziger;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;


public class AdresDAOHibernate implements AdresDAO {


    private Session session ;

    public AdresDAOHibernate(Session session){
        this.session = session;
    }


    /**
     * deze methode slaat een adres op in de database
     */

    @Override
    public boolean save(Adres adres) {
        try {
            session.beginTransaction();
            session.save(adres);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * deze methode veranderd de data van een adres in de database
     */

    @Override
    public boolean update(Adres adres) {
        try {

            session.beginTransaction();
            session.update(adres);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
            return false;
        }
        return true;
    }



    /**
     * deze methode verwijderd de adres uit de database
     */


    @Override
    public boolean delete(Adres adres) {
        try {
            session.beginTransaction();
            session.delete(adres);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
            return false;
        }
        return true;
    }



    /**
     * deze methode zoekt een adres in de database via een reiziger
     */
    @Override
    public Adres findByReiziger(Reiziger reiziger) {
        try {

            return session.find(Adres.class,reiziger.getId());

        } catch (Exception exception) {
            exception.printStackTrace();

        }
        return null;
    }



    /**
     * deze methode geeft alle adressen in de database
     */
    @Override
    public List<Adres> findAll() {
        try {

            return session.createQuery("from Adres",Adres.class).getResultList();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ArrayList<>();
    }


}
