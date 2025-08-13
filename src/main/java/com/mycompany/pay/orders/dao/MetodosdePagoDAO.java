package com.mycompany.pay.orders.dao;

import com.mycompany.pay.orders.model.MetodosdePago;
import java.sql.SQLException;
import java.util.List;

public interface MetodosdePagoDAO {

    void agregarMetododePago(MetodosdePago metodo) throws SQLException;

    MetodosdePago obtenerMetododePagoPorId(int id) throws SQLException;

    List<MetodosdePago> obtenerTodoslosMetodosdePago() throws SQLException;

    void actualizarMetododePago(MetodosdePago metodo) throws SQLException;

    void eliminarMetododePago(int id) throws SQLException;
}
