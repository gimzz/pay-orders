package com.mycompany.pay.orders.dao;

import com.mycompany.pay.orders.model.MateriaPrima;
import java.sql.SQLException;
import java.util.List;

public interface MateriaPrimaDAO {
    void agregarMateriaPrima(MateriaPrima materiaPrima) throws SQLException;
    MateriaPrima obtenerMateriaPrimaPorId(int id) throws SQLException;
    List<MateriaPrima> obtenerTodasMateriasPrimas() throws SQLException;
    void actualizarMateriaPrima(MateriaPrima materiaPrima) throws SQLException;
    void eliminarMateriaPrima(int id) throws SQLException;
    public int obtenerStockActual(int idMateriaPrima) throws SQLException;
}
