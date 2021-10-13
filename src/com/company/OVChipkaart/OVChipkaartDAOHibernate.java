package com.company.OVChipkaart;

import com.company.product.Product;
import com.company.reiziger.Reiziger;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


@Transactional
public class OVChipkaartDAOHibernate implements OVChipkaartDAO{

    private Session session;




    public OVChipkaartDAOHibernate(Session session){
        this.session = session;
    }



    /**
     * deze methode slaat een OVChipkaart op in de database
     */
    @Override
    public boolean save(OVChipkaart ovChipkaart) {
        try {
            session.beginTransaction();

            session.save(ovChipkaart);

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * deze methode veranderd de data van een OVChipkaart in de database
     */
    @Override
    public boolean update(OVChipkaart ovChipkaart) {
        try {
            session.beginTransaction();

            session.update(ovChipkaart);

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * deze methode verwijderd de OVChipkaart uit de database
     */
    @Override
    public boolean delete(OVChipkaart ovChipkaart) {
        try {
            session.beginTransaction();

            session.delete(ovChipkaart);

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * deze methode zoekt een OVChipkaart in de database via een reiziger
     */
    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) {
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<OVChipkaart> criteria = criteriaBuilder.createQuery(OVChipkaart.class);
            Root<OVChipkaart> p = criteria.from(OVChipkaart.class);
            criteria.select(p);
            criteria.where(criteriaBuilder.equal(p.get("reiziger"), reiziger));

            return session.createQuery(criteria).getResultList();


        } catch (Exception exception) {
            exception.printStackTrace();

        }
        return null;
    }

    /**
     * deze methode geeft alle OVChipkaarten in de database
     */
    @Override
    public List<OVChipkaart> findAll() {
        List<OVChipkaart> kaarten = new ArrayList<>();
        try {

            return session.createQuery("from OVChipkaart", OVChipkaart.class).getResultList();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return kaarten;
    }

    @Override
    public List<OVChipkaart> findByProduct(Product product) {
        List<OVChipkaart> ovChipkaarten = new ArrayList<>();
        try {
            Product product1 = session.find(Product.class, product.getProduct_nummer());

            return product1.getOvChipkaarten();


        } catch (Exception exception) {
            exception.printStackTrace();

        }
        return ovChipkaarten;
    }


}
