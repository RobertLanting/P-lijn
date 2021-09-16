package com.company.adres;

import com.company.reiziger.Reiziger;
import com.company.reiziger.ReizigerDAO;
import com.company.reiziger.ReizigerDAOPsql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class AdresDAOPsql implements AdresDAO{


    private Connection conn;
    private ReizigerDAO rDAO;



    public AdresDAOPsql(Connection connection){
        this.conn = connection;
    }

    public AdresDAOPsql(Connection connection, ReizigerDAOPsql rDAO) {
        this.conn = connection;
        this.rDAO = rDAO;
        rDAO.setAdresDao(this);

    }

    public void setReizigerDAO(ReizigerDAOPsql rDAO) {
        this.rDAO = rDAO;
    }

    /**
     * deze methode slaat een adres op in de database
     */
    @Override
    public boolean save(Adres adres) {
        try {

            PreparedStatement statement = conn.prepareStatement("INSERT INTO adres(adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id)" +
                    "VALUES(?,?,?,?,?,?)");

            statement.setInt(1,adres.getId());
            statement.setString(2,adres.getPostcode());
            statement.setString(3,adres.getHuisnummer());
            statement.setString(4,adres.getStraat());
            statement.setString(5, adres.getWoonplaats());
            statement.setInt(6, adres.getReiziger().getId());

            statement.executeUpdate();

        } catch (Exception exception) {
            exception.printStackTrace();
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

            PreparedStatement statement = conn.prepareStatement("UPDATE adres " +
                    "SET postcode = ?," +
                    " huisnummer = ?," +
                    " straat = ?," +
                    " woonplaats = ?"
                    + "WHERE adres_id = ?");

            statement.setString(1,adres.getPostcode());
            statement.setString(2, adres.getHuisnummer());
            statement.setString(3,adres.getStraat());
            statement.setString(4, adres.getWoonplaats());
            statement.setInt(5,adres.getId());

            statement.executeUpdate();

        } catch (Exception exception) {
            exception.printStackTrace();
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

            PreparedStatement statement = conn.prepareStatement("DELETE FROM adres WHERE adres_id = ?");

            statement.setInt(1,adres.getId());

            statement.executeUpdate();

        } catch (Exception exception) {
            exception.printStackTrace();
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

            PreparedStatement statement = conn.prepareStatement("SELECT * FROM adres WHERE reiziger_id = ?");

            statement.setInt(1, reiziger.getId());

            ResultSet result = statement.executeQuery();

            while (result.next()) {

                return new Adres(result.getInt("adres_id"),
                        result.getString("postcode"),
                        result.getString("huisnummer"),
                        result.getString("straat"),
                        result.getString("woonplaats"),
                        reiziger);

            }


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
        List<Adres> adressen = new ArrayList<>();
        try {

            PreparedStatement statement = conn.prepareStatement("select * from adres");

            ResultSet result = statement.executeQuery();

            while (result.next()) {

                adressen.add(new Adres(
                        result.getInt("adres_id"),
                        result.getString("postcode"),
                        result.getString("huisnummer"),
                        result.getString("straat"),
                        result.getString("woonplaats"),
                        rDAO.findById(result.getInt("reiziger_id"))));

            }


        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return adressen;
    }


}
