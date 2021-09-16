package com.company;

import com.company.OVChipkaart.OVChipkaart;
import com.company.OVChipkaart.OVChipkaartDAO;
import com.company.OVChipkaart.OVChipkaartDAOPsql;
import com.company.adres.Adres;
import com.company.adres.AdresDAO;
import com.company.adres.AdresDAOPsql;
import com.company.reiziger.Reiziger;
import com.company.reiziger.ReizigerDAO;
import com.company.reiziger.ReizigerDAOPsql;

import java.sql.*;
import java.util.List;

public class Main {

    private static Connection connection;

    public static void main(String[] args) throws SQLException {
        getConnection();
        AdresDAO aDAO = new AdresDAOPsql(connection, new ReizigerDAOPsql(connection, new OVChipkaartDAOPsql(connection)));
        ReizigerDAO rDAO = new ReizigerDAOPsql(connection, new AdresDAOPsql(connection), new OVChipkaartDAOPsql(connection));
        OVChipkaartDAO oDAO = new OVChipkaartDAOPsql(connection, new ReizigerDAOPsql(connection, new AdresDAOPsql(connection)));
        testReizigerDAO(rDAO);
        testAdresDAO(aDAO,rDAO);
        testOVChipkaartDAO(oDAO,rDAO);
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

    private static void testAdresDAO(AdresDAO aDAO, ReizigerDAO rDAO) throws SQLException {
        System.out.println("\n---------- Test adresDAO -------------");

        //haal alle adressen op uit de database
        List<Adres> adressen = aDAO.findAll();
        System.out.println("[Test] AdresDAO.findAll() geeft de volgende Adressen:");
        for (Adres a : adressen) {
            System.out.println(a);
        }
        System.out.println();

        // verander een adres in de database
        Adres adres = new Adres(1, "3511LX", "37", "Visschersplein", "Utrecht", rDAO.findById(1));
        System.out.println("[Test] eerst:\n" + adres.getPostcode() + "\n" +
                adres.getHuisnummer() + "\n" +
                adres.getStraat() + "\n" +
                adres.getWoonplaats() + "\n");
        adres.setPostcode("3645");
        adres.setHuisnummer("66");
        adres.setStraat("pijlstaartlaan");
        adres.setWoonplaats("vinkeveen");
        aDAO.update(adres);
        adres = aDAO.findByReiziger(rDAO.findById(1));
        System.out.println("na update:\n" + adres.getPostcode() + "\n" +
                adres.getHuisnummer() + "\n" +
                adres.getStraat() + "\n" +
                adres.getWoonplaats() + "\n");

        // zoek een adres met een reiziger
        adres = aDAO.findByReiziger(rDAO.findById(1));
        System.out.println("[Test] AdresDAO.findByReiziger() geeft het volgende adres:");
        System.out.println(adres.toString() + "\n");

        // delete een adres uit de database
        System.out.print("[Test] Eerst " + adressen.size() + " Adressen, na AdresDAO.delete() ");

        aDAO.delete(adres);
        adressen = aDAO.findAll();
        System.out.println(adressen.size() + " Adressen\n");

        // Maak een nieuw adres aan en persisteer deze in de database

        System.out.print("[Test] Eerst " + adressen.size() + " adressen, na AdresDAO.save() ");
        adres = new Adres(1, "3511LX", "37", "Visschersplein", "Utrecht", rDAO.findById(1));
        aDAO.save(adres);
        adressen = aDAO.findAll();
        System.out.println(adressen.size() + " Adressen\n");
    }

    private static void testOVChipkaartDAO(OVChipkaartDAO oDAO, ReizigerDAO rDAO) throws SQLException {
        System.out.println("\n---------- Test OVChipkaartDAO -------------");

        //haal alle OVChipkaarten op uit de database
        List<OVChipkaart> OVkaarten = oDAO.findAll();
        System.out.println("[Test] OVChipkaartDAO.findAll() geeft de volgende kaarten:");
        for (OVChipkaart o : OVkaarten) {
            System.out.println(o);
        }
        System.out.println();

        // verander een OVChipkaart in de database
        OVChipkaart ovChipkaart = new OVChipkaart(90537, java.sql.Date.valueOf("2019-12-31"), 2, 20, rDAO.findById(5));
        System.out.println("[Test] eerst:\n" + ovChipkaart.getGeldigTot() + "\n" +
                ovChipkaart.getKlasse() + "\n" +
                ovChipkaart.getSaldo() + "\n");
        ovChipkaart.setGeldigTot(java.sql.Date.valueOf("2020-06-16"));
        ovChipkaart.setKlasse(1);
        ovChipkaart.setSaldo(15);
        oDAO.update(ovChipkaart);
        ovChipkaart = oDAO.findByReiziger(rDAO.findById(5)).get(1);
        System.out.println("na update:\n" + ovChipkaart.getGeldigTot() + "\n" +
                ovChipkaart.getKlasse() + "\n" +
                ovChipkaart.getSaldo() + "\n");
        ovChipkaart.setGeldigTot(java.sql.Date.valueOf("2019-12-31"));
        ovChipkaart.setKlasse(2);
        ovChipkaart.setSaldo(20);
        oDAO.update(ovChipkaart);

        // zoek OVKaarten met een reiziger
        List<OVChipkaart> ovChipkaarten = oDAO.findByReiziger(rDAO.findById(2));
        System.out.println("[Test] OVkaartenDAO.findByReiziger() geeft de volgende OVChipkaarten:");
        for (OVChipkaart o : ovChipkaarten) {
            System.out.println(o.toString() + "\n");
        }

        // Maak een nieuw OVChipkaart aan en persisteer deze in de database
        ovChipkaarten = oDAO.findAll();
        System.out.print("[Test] Eerst " + ovChipkaarten.size() + " OVKaarten, na OVkaartDAO.save() ");
        ovChipkaart = new OVChipkaart(11111, java.sql.Date.valueOf("2020-02-17"), 2, 30, rDAO.findById(1));
        oDAO.save(ovChipkaart);
        ovChipkaarten = oDAO.findAll();
        System.out.println(ovChipkaarten.size() + " OVKaarten\n");

        // delete een OVChipkaart uit de database
        ovChipkaarten = oDAO.findAll();
        System.out.print("[Test] Eerst " + ovChipkaarten.size() + " OVKaarten, na OVKaartDAO.delete() ");

        oDAO.delete(ovChipkaart);
        ovChipkaarten = oDAO.findAll();
        System.out.println(ovChipkaarten.size() + " OVKaarten\n");





    }

    }
