package com.company.product;

import com.company.OVChipkaart.OVChipkaart;
import com.company.OVChipkaart.OVChipkaartDAO;
import com.company.OVChipkaart.OVChipkaartDAOPsql;
import com.company.reiziger.ReizigerDAO;
import com.company.reiziger.ReizigerDAOPsql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOPsql implements ProductDAO{

    private Connection conn;
    private OVChipkaartDAO oDAO;

    public ProductDAOPsql(Connection connection){
        this.conn = connection;
    }

    public ProductDAOPsql(Connection connection, OVChipkaartDAOPsql oDAO) {
        this.oDAO = oDAO;
        this.conn = connection;
    }

    @Override
    public boolean save(Product product) {
        try {
            if (product.getOvChipkaarten() != null) {
                for (OVChipkaart o : product.getOvChipkaarten()) {

                    PreparedStatement statement = conn.prepareStatement("INSERT INTO ov_chipkaart_product(kaart_nummer, product_nummer)" +
                            "VALUES(?,?)");

                    statement.setInt(1,o.getNummer());
                    statement.setInt(2,product.getProduct_nummer());

                    statement.executeUpdate();
                }
            }

            PreparedStatement statement = conn.prepareStatement("INSERT INTO product(product_nummer, naam, beschrijving, prijs)" +
                    "VALUES(?,?,?,?)");

            statement.setInt(1,product.getProduct_nummer());
            statement.setString(2,product.getNaam());
            statement.setString(3,product.getBeschrijving());
            statement.setDouble(4,product.getPrijs());

            statement.executeUpdate();

        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean update(Product product) {
        try {
            if (product.getOvChipkaarten() != null) {
                for (OVChipkaart o : product.getOvChipkaarten()) {

                    PreparedStatement statement = conn.prepareStatement("INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer) " +
                            "VALUES (?,?)");

                    statement.setInt(1, o.getNummer());
                    statement.setInt(2, product.getProduct_nummer());

                    statement.executeUpdate();
                }
            }

                PreparedStatement statement = conn.prepareStatement("UPDATE product " +
                        "SET naam = ?," +
                        " beschrijving = ?," +
                        " prijs = ?" +
                        "WHERE product_nummer = ?");

                statement.setString(1, product.getNaam());
                statement.setString(2, product.getBeschrijving());
                statement.setDouble(3, product.getPrijs());
                statement.setInt(4, product.getProduct_nummer());

                statement.executeUpdate();
            }
         catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(Product product) {
        try {


            PreparedStatement statement = conn.prepareStatement("DELETE FROM ov_chipkaart_product WHERE product_nummer = ?");
            PreparedStatement statement1 = conn.prepareStatement("DELETE FROM product WHERE product_nummer = ?");

            statement.setInt(1,product.getProduct_nummer());
            statement1.setInt(1,product.getProduct_nummer());

            statement.executeUpdate();
            statement1.executeUpdate();

        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<Product> findByOVChipkaart(OVChipkaart ovChipkaart) {
        List<Product> producten = new ArrayList<>();
        try {

            PreparedStatement statement = conn.prepareStatement("SELECT * FROM product " +
                    "JOIN ov_chipkaart_product ocp on product.product_nummer = ocp.product_nummer " +
                    "WHERE ocp.kaart_nummer = ?");

            statement.setInt(1, ovChipkaart.getNummer());

            ResultSet result = statement.executeQuery();

            while (result.next()) {

                producten.add(new Product(result.getInt("product_nummer"),
                        result.getString("naam"),
                        result.getString("beschrijving"),
                        result.getDouble("prijs")));


            }

        } catch (Exception exception) {
            exception.printStackTrace();

        }
        return producten;
    }

    @Override
    public List<Product> findAll() {
        List<Product> producten = new ArrayList<>();
        try {

            PreparedStatement statement = conn.prepareStatement("select * from product");

            ResultSet result = statement.executeQuery();

            while (result.next()) {

                Product product = new Product(result.getInt("product_nummer"),
                        result.getString("naam"),
                        result.getString("beschrijving"),
                        result.getDouble("prijs"));
                        for(OVChipkaart o : oDAO.findByProduct(product)) {
                            product.addOVChipkaart(o);
                        }
                producten.add(product);
            }


        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return producten;
    }


}
