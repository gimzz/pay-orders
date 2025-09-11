package com.mycompany.pay.orders.controller;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.mycompany.pay.orders.dao.TasadeCambioDAO;
import com.mycompany.pay.orders.model.TasadeCambio;

/**
 * Controlador para manejar la lógica de negocio de Tasas de Cambio.
 * Interactúa con la persistencia a través de TasadeCambioDAO.
 */
public class TasadeCambioController {

    private final TasadeCambioDAO tasaCambioDAO;

    public TasadeCambioController(TasadeCambioDAO tasaCambioDAO) {
        this.tasaCambioDAO = tasaCambioDAO;
    }

  public void registrarTasaCambio(TasadeCambio tasa) throws SQLException {
    if (tasa == null)
        throw new IllegalArgumentException("El objeto tasa no puede ser null");
    if (tasa.getFechaTasaCambio() == null)
        throw new IllegalArgumentException("La fecha de la tasa es obligatoria");
    if (tasa.getValor() == null || tasa.getValor().compareTo(BigDecimal.ZERO) <= 0)
        throw new IllegalArgumentException("Valor debe ser mayor que cero");

    tasaCambioDAO.registrarTasaCambio(tasa);
}


    public void eliminarTasaCambio(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de tasa inválido para eliminar");
        }
        tasaCambioDAO.eliminarTasaCambio(id);
    }

    public TasadeCambio obtenerTasaCambioPorFecha(LocalDate fecha) throws SQLException {
        if (fecha == null)
            throw new IllegalArgumentException("La fecha no puede ser null");
        return tasaCambioDAO.obtenerTasaCambioPorFecha(fecha);
    }

    public TasadeCambio obtenerUltimaTasaCambio() throws SQLException {
        return tasaCambioDAO.obtenerUltimaTasaCambio();
    }

    public List<TasadeCambio> obtenerTodasLasTasasCambio() throws SQLException {
        return tasaCambioDAO.obtenerTodasLasTasasCambio();
    }
}
