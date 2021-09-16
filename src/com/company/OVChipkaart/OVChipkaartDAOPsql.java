package com.company.OVChipkaart;

import com.company.adres.Adres;
import com.company.adres.AdresDAO;
import com.company.reiziger.Reiziger;
import com.company.reiziger.ReizigerDAO;
import com.company.reiziger.ReizigerDAOPsql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO{

    private Connection conn;
    private ReizigerDAO rDAO;



    public OVChipkaartDAOPsql(Connection connection){
        this.conn = connection;
    }

    public OVChipkaartDAOPsql(Connection connection, ReizigerDAOPsql rDAO) {
        this.conn = connection;
        this.rDAO = rDAO;
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

            PreparedStatement statement = conn.prepareStatement("DELETE FROM ov_chipkaart WHERE kaart_nummer = ?");

            statement.setInt(1,ovChipkaart.getNummer());

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

                kaarten.add(new OVChipkaart(
                        result.getInt("kaart_nummer"),
                        result.getDate("geldig_tot"),
                        result.getInt("klasse"),
                        result.getDouble("saldo"),
                        rDAO.findById(result.getInt("reiziger_id"))));

            }


        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return kaarten;
    }


}
