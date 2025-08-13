package com.mycompany.pay.orders.dao;

import com.mycompany.pay.orders.model.TasadeCambio;
import java.time.LocalDate;
import java.sql.SQLException;
import java.util.List;

public interface TasadeCambioDAO {

    void registrarTasaCambio(TasadeCambio tasa) throws SQLException;

    TasadeCambio obtenerTasaCambioPorFecha(LocalDate fecha) throws SQLException;

    TasadeCambio obtenerUltimaTasaCambio() throws SQLException;

    List<TasadeCambio> obtenerTodasLasTasasCambio() throws SQLException;

}
