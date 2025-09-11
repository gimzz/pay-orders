package com.mycompany.pay.orders.dao;

import com.mycompany.pay.orders.model.TasadeCambio;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface TasadeCambioDAO {
    void registrarTasaCambio(TasadeCambio tasa) throws SQLException;
    TasadeCambio obtenerTasaCambioPorFecha(LocalDate fecha) throws SQLException;
    TasadeCambio obtenerUltimaTasaCambio() throws SQLException;
    List<TasadeCambio> obtenerTodasLasTasasCambio() throws SQLException;
    void eliminarTasaCambio(int id) throws SQLException;

}
