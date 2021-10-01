package com.company.product;

import com.company.OVChipkaart.OVChipkaart;

import java.util.List;

public interface ProductDAO {

    boolean save(Product product);
    boolean update(Product product);
    boolean delete(Product product);
    List<Product> findByOVChipkaart(OVChipkaart ovChipkaart);
    List<Product> findAll();

}
