package com.company.reiziger;

import com.company.adres.AdresDAO;
import com.company.adres.AdresDAOPsql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO{

    private Connection conn;
    private AdresDAO aDAO;

    public ReizigerDAOPsql(Connection connection) throws SQLException {
        this.conn = connection;
    }

    /**
     * deze methode slaat reizigers op in de database
     */
    @Override
    public boolean save(Reiziger reiziger) {
        try {

            PreparedStatement statement = conn.prepareStatement("INSERT INTO reiziger(reiziger_id,voorletters,tussenvoegsel,achternaam,geboortedatum)" +
                    "VALUES(?,?,?,?,?)");

            statement.setInt(1,reiziger.getId());
            statement.setString(2,reiziger.getVoorletters());
            statement.setString(3,reiziger.getTussenvoegsel());
            statement.setString(4,reiziger.getAchternaam());
            statement.setDate(5, (Date) reiziger.getGeboortedatum());

            statement.executeUpdate();

        } catch (Exception exception) {
            exception.printStackTrace();
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

            PreparedStatement statement = conn.prepareStatement("UPDATE reiziger " +
                    "SET voorletters = ?," +
                    " tussenvoegsel = ?," +
                    " achternaam = ?," +
                    " geboortedatum = ?"
                    + "WHERE reiziger_id = ?");

            statement.setString(1,reiziger.getVoorletters());
            statement.setString(2,reiziger.getTussenvoegsel());
            statement.setString(3,reiziger.getAchternaam());
            statement.setDate(4, (Date) reiziger.getGeboortedatum());
            statement.setInt(5,reiziger.getId());

            statement.executeUpdate();

        } catch (Exception exception) {
            exception.printStackTrace();
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

            PreparedStatement statement = conn.prepareStatement("DELETE FROM reiziger WHERE reiziger_id = ?");

            statement.setInt(1,reiziger.getId());

            statement.executeUpdate();

        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * deze methode zoekt een reiziger in de database via zijn id
     */
    @Override
    public Reiziger findById(int id) throws SQLException {
        this.aDAO = new AdresDAOPsql(conn);
        try {

            PreparedStatement statement = conn.prepareStatement("SELECT * FROM reiziger WHERE reiziger_id = ?");

            statement.setInt(1,id);

            ResultSet result = statement.executeQuery();

            while (result.next()) {

                Reiziger r = new Reiziger(result.getInt("reiziger_id"),
                        result.getString("voorletters"),
                        result.getString("tussenvoegsel"),
                        result.getString("achternaam"),
                        result.getDate("geboortedatum"));
                r.setAdres(aDAO.findByReiziger(r));
                return r;

            }


        } catch (Exception exception) {
            exception.printStackTrace();

        }
        return null;
    }

    /**
     * deze methode zoekt een reiziger in de database via zijn geboortedatum
     */
    @Override
    public List<Reiziger> findByGbdatum(String datum) throws SQLException {
        this.aDAO = new AdresDAOPsql(conn);
        List<Reiziger> reizigers = new ArrayList<>();
        try {

            PreparedStatement statement = conn.prepareStatement("SELECT * FROM reiziger WHERE geboortedatum = ?");

            statement.setDate(1,java.sql.Date.valueOf(datum));

            ResultSet result = statement.executeQuery();

            while (result.next()) {

                Reiziger r = new Reiziger(
                        result.getInt("reiziger_id"),
                        result.getString("voorletters"),
                        result.getString("tussenvoegsel"),
                        result.getString("achternaam"),
                        result.getDate("geboortedatum"));
                r.setAdres(aDAO.findByReiziger(r));
                reizigers.add(r);
            }


        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return reizigers;
    }

    /**
     * deze methode geeft alle reizigers in de database
     */
    @Override
    public List<Reiziger> findAll() throws SQLException {
        this.aDAO = new AdresDAOPsql(conn);
        List<Reiziger> reizigers = new ArrayList<>();
        try {

            PreparedStatement statement = conn.prepareStatement("select * from reiziger");

            ResultSet result = statement.executeQuery();

            while (result.next()) {

                Reiziger r = new Reiziger(
                        result.getInt("reiziger_id"),
                        result.getString("voorletters"),
                        result.getString("tussenvoegsel"),
                        result.getString("achternaam"),
                        result.getDate("geboortedatum"));
                r.setAdres(aDAO.findByReiziger(r));
                reizigers.add(r);

            }


        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return reizigers;
    }
    }
