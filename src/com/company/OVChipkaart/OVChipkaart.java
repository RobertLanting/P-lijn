package com.company.OVChipkaart;

import com.company.product.Product;
import com.company.reiziger.Reiziger;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table (name = "ov_chipkaart")
public class OVChipkaart {

    @Id
    @Column (name = "kaart_nummer")
    private int nummer;
    private Date geldig_Tot;
    private int Klasse;
    private double saldo;

    @ManyToOne
    @JoinColumn (name = "reiziger_id")
    private Reiziger reiziger;

    @ManyToMany
    @JoinTable (name = "ov_chipkaart_product", joinColumns = { @JoinColumn (name = "kaart_nummer")},
            inverseJoinColumns = { @JoinColumn (name = "product_nummer")} )
    private List<Product> producten = new ArrayList<>();


    public OVChipkaart(int nummer, Date geldigTot, int klasse, double saldo, Reiziger reiziger) {
        this.nummer = nummer;
        this.geldig_Tot = geldigTot;
        this.Klasse = klasse;
        this.saldo = saldo;
        this.reiziger = reiziger;
    }

    public OVChipkaart() {

    }

    public void addProduct(Product product) {
        producten.add(product);
        product.addOVChipkaart(this);
    }

    public void removeProduct(Product product) {
        producten.remove(product);
        product.removeOVChipkaart(this);
    }

    public int getNummer() {
        return nummer;
    }

    public void setNummer(int nummer) {
        this.nummer = nummer;
    }

    public Date getGeldigTot() {
        return geldig_Tot;
    }

    public void setGeldigTot(Date geldigTot) {
        this.geldig_Tot = geldigTot;
    }

    public int getKlasse() {
        return Klasse;
    }

    public void setKlasse(int klasse) {
        Klasse = klasse;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public Reiziger getReiziger() {
        return reiziger;
    }

    public void setReiziger(Reiziger reiziger) {
        this.reiziger = reiziger;
    }

    public List<Product> getProducten() {
        return producten;
    }

    @Override
    public String toString() {
        return "OVChipkaart{" +
                "nummer=" + nummer +
                ", geldigTot=" + geldig_Tot +
                ", Klasse=" + Klasse +
                ", saldo=" + saldo +
                ", reiziger=" + reiziger.getNaam() +
                ", producten=" + producten +
                '}';
    }
}
