package com.company;

import com.company.reiziger.Reiziger;
import com.company.reiziger.ReizigerDAO;
import com.company.reiziger.ReizigerDAOPsql;

import java.sql.*;
import java.util.List;

public class Main {

    private static Connection connection;

    public static void main(String[] args) throws SQLException {
        getConnection();
        ReizigerDAO rDAO = new ReizigerDAOPsql();
        testReizigerDAO(rDAO);
        closeConnection();

    }

    /**
     * deze methode zet de connectie op
     *
     * @throws SQLException
     */
    private static void getConnection() throws SQLException {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost/ovchip?user=postgres&password=moro3645");
    }

    /**
     * Deze methode sluit de connectie
     *
     * @throws SQLException
     */
    private static void closeConnection() throws SQLException {
        connection.close();
    }

    /**
     * P2. Reiziger DAO: persistentie van een klasse
     *
     * Deze methode test de CRUD-functionaliteit van de Reiziger DAO
     *
     * @throws SQLException
     */
    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        // verander de reiziger in de database
        System.out.println("[Test] eerst:\n" + sietske.getVoorletters() + "\n" +
                sietske.getTussenvoegsel() + "\n" +
                sietske.getAchternaam() + "\n" +
                sietske.getGeboortedatum() + "\n");
        sietske.setVoorletters("P");
        sietske.setTussenvoegsel("van");
        sietske.setAchternaam("Boeren");
        sietske.setGeboortedatum(java.sql.Date.valueOf("1987-04-24"));
        rdao.update(sietske);
        sietske = rdao.findById(77);
        System.out.println("na update:\n" + sietske.getVoorletters() + "\n" +
                sietske.getTussenvoegsel() + "\n" +
                sietske.getAchternaam() + "\n" +
                sietske.getGeboortedatum() + "\n");

        // zoek de reiziger met zijn id
        sietske = rdao.findById(77);
        System.out.println("[Test] ReizigerDAO.findById() geeft de volgende reiziger:");
        System.out.println(sietske.getNaam() + "\n");

        // zoek de reiziger met zijn geboortedatum
        System.out.println("[Test] ReizigerDAO.findByGbdatum() geeft de volgende reizigers:");
        System.out.println(rdao.findByGbdatum("1987-04-24") + "\n");

        // delete reiziger uit de database
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.delete() ");
        rdao.delete(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");


        // Voeg aanvullende tests van de ontbrekende CRUD-operaties in.

    }
    }