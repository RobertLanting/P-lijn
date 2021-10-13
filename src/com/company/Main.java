package com.company;

import com.company.OVChipkaart.OVChipkaart;
import com.company.OVChipkaart.OVChipkaartDAOHibernate;
import com.company.adres.Adres;
import com.company.adres.AdresDAOHibernate;
import com.company.product.Product;
import com.company.product.ProductDAOHibernate;
import com.company.reiziger.Reiziger;
import com.company.reiziger.ReizigerDAOHibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Testklasse - deze klasse test alle andere klassen in deze package.
 *
 * System.out.println() is alleen in deze klasse toegestaan (behalve voor exceptions).
 *
 * @author tijmen.muller@hu.nl
 */
public class Main {
    // Creëer een factory voor Hibernate sessions.
    private static final SessionFactory factory;

    static {
        try {
            // Create a Hibernate session factory
            factory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Retouneer een Hibernate session.
     *
     * @return Hibernate session
     * @throws HibernateException
     */
    private static Session getSession() throws HibernateException {
        return factory.openSession();
    }

    public static void main(String[] args) throws SQLException {
        test();
    }

    /**
     * P6. Haal alle (geannoteerde) entiteiten uit de database.
     */
    private static void testFetchAll() {
        Session session = getSession();
        try {
            Metamodel metamodel = session.getSessionFactory().getMetamodel();
            for (EntityType<?> entityType : metamodel.getEntities()) {
                Query query = session.createQuery("from " + entityType.getName());

                System.out.println("[Test] Alle objecten van type " + entityType.getName() + " uit database:");
                for (Object o : query.list()) {
                    System.out.println("  " + o);
                }
                System.out.println();
            }
        } finally {
            session.close();
        }
    }

    private static void testDAOHibernate() {
        Session session = getSession();

        try {

            AdresDAOHibernate adresDAOHibernate = new AdresDAOHibernate(session);
            ReizigerDAOHibernate reizigerDAOHibernate = new ReizigerDAOHibernate(session);
            ProductDAOHibernate productDAOHibernate = new ProductDAOHibernate(session);
            OVChipkaartDAOHibernate ovChipkaartDAOHibernate = new OVChipkaartDAOHibernate(session);
            System.out.println("\n---------- Test ReizigerDAO -------------");

            // Haal alle reizigers op uit de database
            List<Reiziger> reizigers = reizigerDAOHibernate.findAll();
            System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
            for (Reiziger r : reizigers) {
                System.out.println(r);
            }
            System.out.println();

            // Maak een nieuwe reiziger aan en persisteer deze in de database
            String gbdatum = "1981-03-14";
            Reiziger sietske = new Reiziger(77, "S", "", "Boers", Date.valueOf(gbdatum));
            System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
            reizigerDAOHibernate.save(sietske);
            reizigers = reizigerDAOHibernate.findAll();
            System.out.println(reizigers.size() + " reizigers\n");

            // verander de reiziger in de database
            System.out.println("[Test] eerst:\n" + sietske.getVoorletters() + "\n" +
                    sietske.getTussenvoegsel() + "\n" +
                    sietske.getAchternaam() + "\n" +
                    sietske.getGeboortedatum() + "\n");
            sietske.setVoorletters("P");
            sietske.setTussenvoegsel("van");
            sietske.setAchternaam("Boeren");
            sietske.setGeboortedatum(Date.valueOf("1987-04-24"));
            reizigerDAOHibernate.update(sietske);
            sietske = reizigerDAOHibernate.findById(77);
            System.out.println("na update:\n" + sietske.getVoorletters() + "\n" +
                    sietske.getTussenvoegsel() + "\n" +
                    sietske.getAchternaam() + "\n" +
                    sietske.getGeboortedatum() + "\n");

            // zoek de reiziger met zijn id
            sietske = reizigerDAOHibernate.findById(77);
            System.out.println("[Test] ReizigerDAO.findById() geeft de volgende reiziger:");
            System.out.println(sietske.getNaam() + "\n");

            // zoek de reiziger met zijn geboortedatum
            System.out.println("[Test] ReizigerDAO.findByGbdatum() geeft de volgende reizigers:");
            System.out.println(reizigerDAOHibernate.findByGbdatum("1987-04-24") + "\n");

            // delete reiziger uit de database
            System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.delete() ");
            reizigerDAOHibernate.delete(sietske);
            reizigers = reizigerDAOHibernate.findAll();
            System.out.println(reizigers.size() + " reizigers\n");


            System.out.println("\n---------- Test adresDAO -------------");

            //haal alle adressen op uit de database
            List<Adres> adressen = adresDAOHibernate.findAll();
            System.out.println("[Test] AdresDAO.findAll() geeft de volgende Adressen:");
            for (Adres a : adressen) {
                System.out.println(a);
            }
            System.out.println();

            // verander een adres in de database
            Adres adres = adresDAOHibernate.findByReiziger(reizigerDAOHibernate.findById(1));
            System.out.println("[Test] eerst:\n" + adres.getPostcode() + "\n" +
                    adres.getHuisnummer() + "\n" +
                    adres.getStraat() + "\n" +
                    adres.getWoonplaats() + "\n");
            adres.setPostcode("3645");
            adres.setHuisnummer("66");
            adres.setStraat("pijlstaartlaan");
            adres.setWoonplaats("vinkeveen");
            adresDAOHibernate.update(adres);
            adres = adresDAOHibernate.findByReiziger(reizigerDAOHibernate.findById(1));
            System.out.println("na update:\n" + adres.getPostcode() + "\n" +
                    adres.getHuisnummer() + "\n" +
                    adres.getStraat() + "\n" +
                    adres.getWoonplaats() + "\n");

            // zoek een adres met een reiziger
            adres = adresDAOHibernate.findByReiziger(reizigerDAOHibernate.findById(1));
            System.out.println("[Test] AdresDAO.findByReiziger() geeft het volgende adres:");
            System.out.println(adres.toString() + "\n");

            // delete een adres uit de database
            System.out.print("[Test] Eerst " + adressen.size() + " Adressen, na AdresDAO.delete() ");
            adresDAOHibernate.delete(adres);
            adressen = adresDAOHibernate.findAll();
            System.out.println(adressen.size() + " Adressen\n");

            // Maak een nieuw adres aan en persisteer deze in de database

            System.out.print("[Test] Eerst " + adressen.size() + " adressen, na AdresDAO.save() ");
            adresDAOHibernate.save(adres);
            adressen = adresDAOHibernate.findAll();
            System.out.println(adressen.size() + " Adressen\n");

            System.out.println("\n---------- Test OVChipkaartDAO -------------");

            //haal alle OVChipkaarten op uit de database
            List<OVChipkaart> OVkaarten = ovChipkaartDAOHibernate.findAll();
            System.out.println("[Test] OVChipkaartDAO.findAll() geeft de volgende kaarten:");
            for (OVChipkaart o : OVkaarten) {
                System.out.println(o);
            }
            System.out.println();

            // verander een OVChipkaart in de database
            OVChipkaart ovChipkaart = new OVChipkaart(90537, Date.valueOf("2019-12-31"), 2, 20, reizigerDAOHibernate.findById(5));
            System.out.println("[Test] eerst:\n" + ovChipkaart.getGeldigTot() + "\n" +
                    ovChipkaart.getKlasse() + "\n" +
                    ovChipkaart.getSaldo() + "\n");
            ovChipkaart.setGeldigTot(Date.valueOf("2020-06-16"));
            ovChipkaart.setKlasse(1);
            ovChipkaart.setSaldo(15);
            ovChipkaartDAOHibernate.update(ovChipkaart);
            ovChipkaart = ovChipkaartDAOHibernate.findByReiziger(reizigerDAOHibernate.findById(5)).get(1);
            System.out.println("na update:\n" + ovChipkaart.getGeldigTot() + "\n" +
                    ovChipkaart.getKlasse() + "\n" +
                    ovChipkaart.getSaldo() + "\n");
            ovChipkaart.setGeldigTot(Date.valueOf("2019-12-31"));
            ovChipkaart.setKlasse(2);
            ovChipkaart.setSaldo(20);
            ovChipkaartDAOHibernate.update(ovChipkaart);

            // zoek OVKaarten met een reiziger
            List<OVChipkaart> ovChipkaarten = ovChipkaartDAOHibernate.findByReiziger(reizigerDAOHibernate.findById(2));
            System.out.println("[Test] OVkaartenDAO.findByReiziger() geeft de volgende OVChipkaarten:");
            for (OVChipkaart o : ovChipkaarten) {
                System.out.println(o.toString() + "\n");
            }

            // Maak een nieuw OVChipkaart aan en persisteer deze in de database
            ovChipkaarten = ovChipkaartDAOHibernate.findAll();
            System.out.print("[Test] Eerst " + ovChipkaarten.size() + " OVKaarten, na OVkaartDAO.save() ");
            ovChipkaart = new OVChipkaart(11111, Date.valueOf("2020-02-17"), 2, 30, reizigerDAOHibernate.findById(1));
            ovChipkaartDAOHibernate.save(ovChipkaart);
            ovChipkaarten = ovChipkaartDAOHibernate.findAll();
            System.out.println(ovChipkaarten.size() + " OVKaarten\n");

            // delete een OVChipkaart uit de database
            ovChipkaarten = ovChipkaartDAOHibernate.findAll();
            System.out.print("[Test] Eerst " + ovChipkaarten.size() + " OVKaarten, na OVKaartDAO.delete() ");

            ovChipkaartDAOHibernate.delete(ovChipkaart);
            ovChipkaarten = ovChipkaartDAOHibernate.findAll();
            System.out.println(ovChipkaarten.size() + " OVKaarten\n");

            System.out.println("\n---------- Test ProductDAO -------------");

            //haal alle OVChipkaarten op uit de database
            List<Product> producten = productDAOHibernate.findAll();
            System.out.println("[Test] ProductDAO.findAll() geeft de volgende kaarten:");
            for (Product p : producten) {
                System.out.println(p);
            }
            System.out.println();

            // verander een OVChipkaart in de database
            Product product = new Product(1, "Dagkaart 2e klas", "Een hele dag onbeperkt reizen met de trein", 50.60);
            System.out.println("[Test] eerst:\n" + product.getNaam() + "\n" +
                    product.getBeschrijving() + "\n" +
                    product.getPrijs() + "\n");
            product.setNaam("Weekkaart 1e klas");
            product.setBeschrijving("De hele week gratis reizen 1e klas");
            product.setPrijs(40.50);
            productDAOHibernate.update(product);
            product = productDAOHibernate.findAll().stream().filter(p -> p.getProduct_nummer() == 1).findAny().get();
            System.out.println("nu :\n" + product.getNaam() + "\n" +
                    product.getBeschrijving() + "\n" +
                    product.getPrijs() + "\n");
            product.setNaam("Dagkaart 2e klas");
            product.setBeschrijving("Een hele dag onbeperkt reizen met de trein");
            product.setPrijs(50.60);
            productDAOHibernate.update(product);

            // zoek OVKaarten met een reiziger
            List<Product> producten2 = productDAOHibernate.findByOVChipkaart(ovChipkaartDAOHibernate.findAll().get(0));
            System.out.println("[Test] ProductDAO.findByOVKaart() geeft de volgende producten:");
            for (Product p : producten2) {
                System.out.println(p.toString() + "\n");
            }

            // Maak een nieuw OVChipkaart aan en persisteer deze in de database
            producten = productDAOHibernate.findAll();
            System.out.print("[Test] Eerst " + producten.size() + " producten, na ProductDAO.save() ");
            product = new Product(7, "Maandpas 2e klas", "Voor de hele maand gratis met de trein", 100);
            productDAOHibernate.save(product);
            producten = productDAOHibernate.findAll();
            System.out.println(producten.size() + " producten\n");

            // delete een OVChipkaart uit de database
            producten = productDAOHibernate.findAll();
            System.out.print("[Test] Eerst " + producten.size() + " producten, na productDAO.delete() ");

            productDAOHibernate.delete(product);
            producten = productDAOHibernate.findAll();
            System.out.println(producten.size() + " producten\n");

        } finally {
            session.close();
        }
    }

    public static void test() {
        Session session = getSession();
        try {
            AdresDAOHibernate adresDAOHibernate = new AdresDAOHibernate(session);
            ReizigerDAOHibernate reizigerDAOHibernate = new ReizigerDAOHibernate(session);
            ProductDAOHibernate productDAOHibernate = new ProductDAOHibernate(session);
            OVChipkaartDAOHibernate ovChipkaartDAOHibernate = new OVChipkaartDAOHibernate(session);
            System.out.println("\n---------- Test ReizigerDAO -------------");

            // Haal alle reizigers op uit de database
            List<Reiziger> reizigers = reizigerDAOHibernate.findAll();
            System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
            for (Reiziger r : reizigers) {
                System.out.println(r);
            }
            System.out.println();

            // Maak een nieuwe reiziger aan en persisteer deze in de database
            String gbdatum = "1981-03-14";
            Reiziger sietske = new Reiziger(77, "S", "", "Boers", Date.valueOf(gbdatum));
            System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
            reizigerDAOHibernate.save(sietske);
            reizigers = reizigerDAOHibernate.findAll();
            System.out.println(reizigers.size() + " reizigers\n");

            // verander de reiziger in de database
            System.out.println("[Test] eerst:\n" + sietske.getVoorletters() + "\n" +
                    sietske.getTussenvoegsel() + "\n" +
                    sietske.getAchternaam() + "\n" +
                    sietske.getGeboortedatum() + "\n");
            sietske.setVoorletters("P");
            sietske.setTussenvoegsel("van");
            sietske.setAchternaam("Boeren");
            sietske.setGeboortedatum(Date.valueOf("1987-04-24"));
            reizigerDAOHibernate.update(sietske);
            sietske = reizigerDAOHibernate.findById(77);
            System.out.println("na update:\n" + sietske.getVoorletters() + "\n" +
                    sietske.getTussenvoegsel() + "\n" +
                    sietske.getAchternaam() + "\n" +
                    sietske.getGeboortedatum() + "\n");

            // zoek de reiziger met zijn id
            sietske = reizigerDAOHibernate.findById(77);
            System.out.println("[Test] ReizigerDAO.findById() geeft de volgende reiziger:");
            System.out.println(sietske.getNaam() + "\n");

            // zoek de reiziger met zijn geboortedatum
            System.out.println("[Test] ReizigerDAO.findByGbdatum() geeft de volgende reizigers:");
            System.out.println(reizigerDAOHibernate.findByGbdatum("1987-04-24") + "\n");

            // delete reiziger uit de database
            System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.delete() ");
            reizigerDAOHibernate.delete(sietske);
            reizigers = reizigerDAOHibernate.findAll();
            System.out.println(reizigers.size() + " reizigers\n");

            System.out.println("\n---------- Test adresDAO -------------");

            //haal alle adressen op uit de database
            List<Adres> adressen = adresDAOHibernate.findAll();
            System.out.println("[Test] AdresDAO.findAll() geeft de volgende Adressen:");
            for (Adres a : adressen) {
                System.out.println(a);
            }
            System.out.println();

            // Maak een nieuw adres aan en persisteer deze in de database

            System.out.print("[Test] Eerst " + adressen.size() + " adressen, na AdresDAO.save() ");
            Adres adres = new Adres(5,"3645","66","pijlstaartlaan","vinkeveen", reizigerDAOHibernate.findById(5));
            adresDAOHibernate.save(adres);
            adressen = adresDAOHibernate.findAll();
            System.out.println(adressen.size() + " Adressen\n");

            // verander een adres in de database
            Adres adres1 = adresDAOHibernate.findByReiziger(reizigerDAOHibernate.findById(4));
            System.out.println("[Test] eerst:\n" + adres1.getPostcode() + "\n" +
                    adres1.getHuisnummer() + "\n" +
                    adres1.getStraat() + "\n" +
                    adres1.getWoonplaats() + "\n");
            adres1.setPostcode("0000");
            adres1.setHuisnummer("100");
            adres1.setStraat("straatlaan");
            adres1.setWoonplaats("stad");
            adresDAOHibernate.update(adres1);
            Adres adres2 = adresDAOHibernate.findByReiziger(reizigerDAOHibernate.findById(4));
            System.out.println("na update:\n" + adres2.getPostcode() + "\n" +
                    adres2.getHuisnummer() + "\n" +
                    adres2.getStraat() + "\n" +
                    adres2.getWoonplaats() + "\n");
            adres2.setPostcode("4444");
            adres2.setHuisnummer("80");
            adres2.setStraat("laanlaan");
            adres2.setWoonplaats("dorp");
            adresDAOHibernate.update(adres2);

            // zoek een adres met een reiziger
            Adres adres3 = adresDAOHibernate.findByReiziger(reizigerDAOHibernate.findById(4));
            System.out.println("[Test] AdresDAO.findByReiziger() geeft het volgende adres:");
            System.out.println(adres3.toString() + "\n");

            // delete een adres uit de database
            Adres adres4 = adresDAOHibernate.findByReiziger(reizigerDAOHibernate.findById(5));
            System.out.print("[Test] Eerst " + adressen.size() + " Adressen, na AdresDAO.delete() ");
            adresDAOHibernate.delete(adres4);
            adressen = adresDAOHibernate.findAll();
            System.out.println(adressen.size() + " Adressen\n");

            System.out.println("\n---------- Test OVChipkaartDAO -------------");

            //haal alle OVChipkaarten op uit de database
            List<OVChipkaart> OVkaarten = ovChipkaartDAOHibernate.findAll();
            System.out.println("[Test] OVChipkaartDAO.findAll() geeft de volgende kaarten:");
            for (OVChipkaart o : OVkaarten) {
                System.out.println(o);
            }
            System.out.println();

            // Maak een nieuw OVChipkaart aan en persisteer deze in de database
            OVChipkaart ovChipkaart = new OVChipkaart(39201,Date.valueOf("2022-01-01"),1,80, reizigerDAOHibernate.findById(3));
            System.out.print("[Test] Eerst " + ovChipkaartDAOHibernate.findAll().size() + " OVKaarten, na OVkaartDAO.save() ");
            ovChipkaartDAOHibernate.save(ovChipkaart);
            System.out.println(ovChipkaartDAOHibernate.findAll().size() + " OVKaarten\n");

            // verander een OVChipkaart in de database
            OVChipkaart ovChipkaart1 = ovChipkaartDAOHibernate.findByReiziger(reizigerDAOHibernate.findById(3)).get(0);
            System.out.println("[Test] eerst:\n" + ovChipkaart1.getGeldigTot() + "\n" +
                    ovChipkaart1.getKlasse() + "\n" +
                    ovChipkaart1.getSaldo() + "\n");
            ovChipkaart1.setGeldigTot(Date.valueOf("2020-06-16"));
            ovChipkaart1.setKlasse(1);
            ovChipkaart1.setSaldo(15);
            ovChipkaartDAOHibernate.update(ovChipkaart1);
            OVChipkaart ovChipkaart2 = ovChipkaartDAOHibernate.findByReiziger(reizigerDAOHibernate.findById(3)).get(0);
            System.out.println("na update:\n" + ovChipkaart2.getGeldigTot() + "\n" +
                    ovChipkaart2.getKlasse() + "\n" +
                    ovChipkaart2.getSaldo() + "\n");
            ovChipkaart2.setGeldigTot(Date.valueOf("2019-12-31"));
            ovChipkaart2.setKlasse(3);
            ovChipkaart2.setSaldo(40);
            ovChipkaartDAOHibernate.update(ovChipkaart2);

            // zoek OVKaarten met een reiziger
            List<OVChipkaart> ovChipkaarten = ovChipkaartDAOHibernate.findByReiziger(reizigerDAOHibernate.findById(2));
            System.out.println("[Test] OVkaartenDAO.findByReiziger() geeft de volgende OVChipkaarten:");
            for (OVChipkaart o : ovChipkaarten) {
                System.out.println(o.toString() + "\n");
            }

            // find by product
            List<OVChipkaart> ovChipkaarten1 = ovChipkaartDAOHibernate.findByProduct(productDAOHibernate.findAll().get(0));
            System.out.println("[Test] OVkaartenDAO.findByProduct() geeft de volgende OVChipkaarten:");
            for (OVChipkaart o : ovChipkaarten1) {
                System.out.println(o.toString() + "\n");
            }

            // delete een OVChipkaart uit de database
            OVChipkaart ovChipkaart3 = ovChipkaartDAOHibernate.findByReiziger(reizigerDAOHibernate.findById(3)).get(0);
            ovChipkaarten = ovChipkaartDAOHibernate.findAll();
            System.out.print("[Test] Eerst " + ovChipkaarten.size() + " OVKaarten, na OVKaartDAO.delete() ");
            ovChipkaartDAOHibernate.delete(ovChipkaart3);
            ovChipkaarten = ovChipkaartDAOHibernate.findAll();
            System.out.println(ovChipkaarten.size() + " OVKaarten\n");

            System.out.println("\n---------- Test ProductDAO -------------");

            //haal alle OVChipkaarten op uit de database
            List<Product> producten = productDAOHibernate.findAll();
            System.out.println("[Test] ProductDAO.findAll() geeft de volgende kaarten:");
            for (Product p : producten) {
                System.out.println(p);
            }
            System.out.println();

            // Maak een nieuw OVChipkaart aan en persisteer deze in de database
            producten = productDAOHibernate.findAll();
            System.out.print("[Test] Eerst " + producten.size() + " producten, na ProductDAO.save() ");
            Product product = new Product(7, "Maandpas 2e klas", "Voor de hele maand gratis met de trein", 100);
            productDAOHibernate.save(product);
            producten = productDAOHibernate.findAll();
            System.out.println(producten.size() + " producten\n");

            // verander een OVChipkaart in de database
            Product product1 = productDAOHibernate.findAll().get(productDAOHibernate.findAll().size() - 1);
            System.out.println("[Test] eerst:\n" + product1.getNaam() + "\n" +
                    product1.getBeschrijving() + "\n" +
                    product1.getPrijs() + "\n");
            product1.setNaam("Weekkaart 1e klas");
            product1.setBeschrijving("De hele week gratis reizen 1e klas");
            product1.setPrijs(40.50);
            productDAOHibernate.update(product);
            Product product2 = productDAOHibernate.findAll().get(productDAOHibernate.findAll().size() - 1);
            System.out.println("nu :\n" + product2.getNaam() + "\n" +
                    product2.getBeschrijving() + "\n" +
                    product2.getPrijs() + "\n");
            product2.setNaam("Dagkaart 2e klas");
            product2.setBeschrijving("Een hele dag onbeperkt reizen met de trein");
            product2.setPrijs(50.60);
            productDAOHibernate.update(product2);

            // zoek OVKaarten met een reiziger
            List<Product> producten2 = productDAOHibernate.findByOVChipkaart(ovChipkaartDAOHibernate.findAll().get(0));
            System.out.println("[Test] ProductDAO.findByOVKaart() geeft de volgende producten:");
            for (Product p : producten2) {
                System.out.println(p.toString() + "\n");
            }


            // delete een OVChipkaart uit de database
            List<Product> producten3 = productDAOHibernate.findAll();
            System.out.print("[Test] Eerst " + producten3.size() + " producten, na productDAO.delete() ");
            Product product3 = productDAOHibernate.findAll().get(productDAOHibernate.findAll().size() - 1);
            productDAOHibernate.delete(product3);
            producten3 = productDAOHibernate.findAll();
            System.out.println(producten3.size() + " producten\n");
            

        }finally {
            session.close();
        }
    }
}
