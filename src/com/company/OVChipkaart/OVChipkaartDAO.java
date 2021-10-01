package com.company.OVChipkaart;

import com.company.product.Product;
import com.company.reiziger.Reiziger;
import java.sql.SQLException;
import java.util.List;

public interface OVChipkaartDAO {

    boolean save(OVChipkaart ovChipkaart);
    boolean update(OVChipkaart ovChipkaart);
    boolean delete(OVChipkaart ovChipkaart);
    List<OVChipkaart> findByReiziger(Reiziger reiziger);
    List<OVChipkaart> findAll() throws SQLException;
    List<OVChipkaart> findByProduct(Product product);
}
