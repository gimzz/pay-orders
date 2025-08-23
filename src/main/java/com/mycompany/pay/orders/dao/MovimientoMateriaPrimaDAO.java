package com.mycompany.pay.orders.dao;

import com.mycompany.pay.orders.model.MovimientoMateriaPrima;
import java.sql.SQLException;
import java.util.List;

public interface MovimientoMateriaPrimaDAO {
    void agregarMovimiento(MovimientoMateriaPrima movimiento) throws SQLException;
    MovimientoMateriaPrima obtenerMovimientoPorId(int id) throws SQLException;
    List<MovimientoMateriaPrima> obtenerMovimientosPorMateriaPrima(int idMateriaPrima) throws SQLException;
    List<MovimientoMateriaPrima> obtenerTodosMovimientos() throws SQLException;
}
