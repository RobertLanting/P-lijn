package com.company.OVChipkaart;

import com.company.reiziger.Reiziger;

import java.util.Date;

public class OVChipkaart {

    private int nummer;
    private Date geldigTot;
    private int Klasse;
    private double saldo;
    private Reiziger reiziger;


    public OVChipkaart(int nummer, Date geldigTot, int klasse, double saldo, Reiziger reiziger) {
        this.nummer = nummer;
        this.geldigTot = geldigTot;
        this.Klasse = klasse;
        this.saldo = saldo;
        this.reiziger = reiziger;
    }

    public int getNummer() {
        return nummer;
    }

    public void setNummer(int nummer) {
        this.nummer = nummer;
    }

    public Date getGeldigTot() {
        return geldigTot;
    }

    public void setGeldigTot(Date geldigTot) {
        this.geldigTot = geldigTot;
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

    @Override
    public String toString() {
        return "OVChipkaart{" +
                "nummer=" + nummer +
                ", geldigTot=" + geldigTot +
                ", Klasse=" + Klasse +
                ", saldo=" + saldo +
                ", reiziger=" + reiziger.getNaam() +
                '}';
    }
}
