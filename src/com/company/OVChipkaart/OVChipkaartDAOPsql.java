package com.company.OVChipkaart;

import com.company.product.Product;
import com.company.product.ProductDAO;
import com.company.product.ProductDAOPsql;
import com.company.reiziger.Reiziger;
import com.company.reiziger.ReizigerDAO;
import com.company.reiziger.ReizigerDAOPsql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO{

    private Connection conn;
    private ReizigerDAO rDAO;
    private ProductDAO pDAO;



    public OVChipkaartDAOPsql(Connection connection){
        this.conn = connection;
    }

    public OVChipkaartDAOPsql(Connection connection, ReizigerDAOPsql rDAO, ProductDAOPsql pDAO) {
        this.conn = connection;
        this.rDAO = rDAO;
        this.pDAO = pDAO;
        rDAO.setOVChipkaartDao(this);
    }

    public void setReizigerDAO(ReizigerDAOPsql rDAO) {
        this.rDAO = rDAO;
    }

    /**
     * deze methode slaat een OVChipkaart op in de database
     */
    @Override
    public boolean save(OVChipkaart ovChipkaart) {
        try {
            if (ovChipkaart.getProducten() != null) {
                for (Product p : ovChipkaart.getProducten()) {

                    PreparedStatement statement = conn.prepareStatement("INSERT INTO ov_chipkaart_product(kaart_nummer, product_nummer)" +
                            "VALUES(?,?)");

                    statement.setInt(1,ovChipkaart.getNummer());
                    statement.setInt(2,p.getProduct_nummer());

                    statement.executeUpdate();
                }
            }

            PreparedStatement statement = conn.prepareStatement("INSERT INTO ov_chipkaart(kaart_nummer, geldig_tot, klasse, saldo, reiziger_id) " +
                    "VALUES(?,?,?,?,?)");

            statement.setInt(1, ovChipkaart.getNummer());
            statement.setDate(2, (Date) ovChipkaart.getGeldigTot());
            statement.setInt(3,ovChipkaart.getKlasse());
            statement.setDouble(4,ovChipkaart.getSaldo());
            statement.setInt(5, ovChipkaart.getReiziger().getId());

            statement.executeUpdate();

        } catch (Exception exception) {
            exception.printStackTrace();
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


            PreparedStatement statement = conn.prepareStatement("UPDATE ov_chipkaart " +
                    "SET geldig_tot = ?," +
                    " klasse = ?," +
                    " saldo = ?"
                    + "WHERE kaart_nummer = ?");

            statement.setDate(1, (Date) ovChipkaart.getGeldigTot());
            statement.setInt(2, ovChipkaart.getKlasse());
            statement.setDouble(3, ovChipkaart.getSaldo());
            statement.setInt(4, ovChipkaart.getNummer());

            statement.executeUpdate();

        } catch (Exception exception) {
            exception.printStackTrace();
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

            PreparedStatement statement1 = conn.prepareStatement("DELETE FROM ov_chipkaart_product WHERE kaart_nummer = ?");
            PreparedStatement statement = conn.prepareStatement("DELETE FROM ov_chipkaart WHERE kaart_nummer = ?");

            statement1.setInt(1,ovChipkaart.getNummer());
            statement.setInt(1,ovChipkaart.getNummer());

            statement1.executeUpdate();
            statement.executeUpdate();

        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * deze methode zoekt een OVChipkaart in de database via een reiziger
     */
    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) {
        List<OVChipkaart> OVkaarten = new ArrayList<>();
        try {

            PreparedStatement statement = conn.prepareStatement("SELECT * FROM ov_chipkaart WHERE reiziger_id = ?");

            statement.setInt(1, reiziger.getId());

            ResultSet result = statement.executeQuery();

            while (result.next()) {

                OVkaarten.add( new OVChipkaart(result.getInt("kaart_nummer"),
                                       result.getDate("geldig_tot"),
                                       result.getInt("klasse"),
                                       result.getDouble("saldo"),
                                        reiziger));

            }


        } catch (Exception exception) {
            exception.printStackTrace();

        }
        return OVkaarten;
    }

    /**
     * deze methode geeft alle OVChipkaarten in de database
     */
    @Override
    public List<OVChipkaart> findAll() {
        List<OVChipkaart> kaarten = new ArrayList<>();
        try {

            PreparedStatement statement = conn.prepareStatement("select * from ov_chipkaart");

            ResultSet result = statement.executeQuery();

            while (result.next()) {

                OVChipkaart ovChipkaart = new OVChipkaart(
                        result.getInt("kaart_nummer"),
                        result.getDate("geldig_tot"),
                        result.getInt("klasse"),
                        result.getDouble("saldo"),
                        rDAO.findById(result.getInt("reiziger_id")));
                for (Product p : pDAO.findByOVChipkaart(ovChipkaart)) {
                    for (OVChipkaart o : this.findByProduct(p)) {
                        p.addOVChipkaart(o);
                    }
                    ovChipkaart.addProduct(p);
                }
                kaarten.add(ovChipkaart);
            }


        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return kaarten;
    }

    @Override
    public List<OVChipkaart> findByProduct(Product product) {
        List<OVChipkaart> ovChipkaarten = new ArrayList<>();
        try {

            PreparedStatement statement = conn.prepareStatement("SELECT * FROM ov_chipkaart " +
                            "JOIN ov_chipkaart_product ocp on ov_chipkaart.kaart_nummer = ocp.kaart_nummer " +
                            "WHERE product_nummer = ?");

            statement.setInt(1, product.getProduct_nummer());

            ResultSet result = statement.executeQuery();

            while (result.next()) {

                ovChipkaarten.add(new OVChipkaart(result.getInt("kaart_nummer"),
                        result.getDate("geldig_tot"),
                        result.getInt("klasse"),
                        result.getDouble("saldo"), null));


            }

        } catch (Exception exception) {
            exception.printStackTrace();

        }
        return ovChipkaarten;
    }


}
