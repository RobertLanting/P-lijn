package com.company.adres;

import com.company.reiziger.Reiziger;

import java.sql.SQLException;
import java.util.List;

public interface AdresDAO {

    boolean save(Adres adres);
    boolean update(Adres adres);
    boolean delete(Adres adres);
    Adres findByReiziger(Reiziger reiziger);
    List<Adres> findAll() throws SQLException;
}
