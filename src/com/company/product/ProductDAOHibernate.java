package com.company.product;

import com.company.OVChipkaart.OVChipkaart;

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
public class ProductDAOHibernate implements ProductDAO{

    private Session session;

    public ProductDAOHibernate(Session session){
        this.session = session;
    }

    @Override
    public boolean save(Product product) {
        try {
            session.beginTransaction();

            session.save(product);

            session.getTransaction().commit();

        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean update(Product product) {
        try {
            session.beginTransaction();

            session.update(product);

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(Product product) {
        try {
            session.beginTransaction();

            session.delete(product);

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<Product> findByOVChipkaart(OVChipkaart ovChipkaart) {
        try {
            OVChipkaart ovChipkaart1 = session.find(OVChipkaart.class, ovChipkaart.getNummer());

            return ovChipkaart1.getProducten();

        } catch (Exception exception) {
            exception.printStackTrace();

        }
        return null;
    }

    @Override
    public List<Product> findAll() {
        List<Product> producten = new ArrayList<>();
        try {

            return session.createQuery("from Product", Product.class).getResultList();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return producten;
    }


}
