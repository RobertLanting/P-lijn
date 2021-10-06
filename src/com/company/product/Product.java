package com.company.product;

import com.company.OVChipkaart.OVChipkaart;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "product")
public class Product {

    @Id
    @Column (name = "product_nummer")
    private int product_nummer;
    private String naam;
    private String beschrijving;
    private double prijs;

    @ManyToMany
    @JoinTable (name = "ov_chipkaart_product", joinColumns = {@JoinColumn (name = "product_nummer") },
            inverseJoinColumns = { @JoinColumn (name = "kaart_nummer")} )
    private List<OVChipkaart> ovChipkaarten = new ArrayList<>();


    public Product(int product_nummer, String naam, String beschrijving, double prijs) {
        this.product_nummer = product_nummer;
        this.naam = naam;
        this.beschrijving = beschrijving;
        this.prijs = prijs;
    }

    public Product() {

    }

    public void addOVChipkaart(OVChipkaart ovChipkaart) {
        ovChipkaarten.add(ovChipkaart);
    }

    public void removeOVChipkaart(OVChipkaart ovChipkaart) {
        ovChipkaarten.remove(ovChipkaart);
    }

    public int getProduct_nummer() {
        return product_nummer;
    }

    public void setProduct_nummer(int product_nummer) {
        this.product_nummer = product_nummer;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }

    public double getPrijs() {
        return prijs;
    }

    public void setPrijs(double prijs) {
        this.prijs = prijs;
    }

    public List<OVChipkaart> getOvChipkaarten() {
        return ovChipkaarten;
    }

    public List<Integer> getKaartNummers() {
        List<Integer> result = new ArrayList<>();
        for (OVChipkaart o : ovChipkaarten) {
            result.add(o.getNummer());
        }
        return result;
    }

    @Override
    public String toString() {
        return "Product{" +
                "product_nummer=" + product_nummer +
                ", naam='" + naam + '\'' +
                ", beschrijving='" + beschrijving + '\'' +
                ", prijs=" + prijs +
                ", ovChipkaarten=" + this.getKaartNummers() +
                '}';
    }
}
