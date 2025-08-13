package com.mycompany.pay.orders.controller;

import com.mycompany.pay.orders.dao.TasadeCambioDAO;
import com.mycompany.pay.orders.model.TasadeCambio;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;

/**
 * Controlador para manejar la lógica de negocio de Tasas de Cambio.
 * Interactúa con la persistencia a través de TasadeCambioDAO.
 * 
 * @author gimz
 */
public class TasadeCambioController {

    private final TasadeCambioDAO tasaCambioDAO;

    public TasadeCambioController(TasadeCambioDAO tasaCambioDAO) {
        this.tasaCambioDAO = tasaCambioDAO;
    }

    public void registrarTasaCambio(TasadeCambio tasa) throws SQLException {
        if (tasa == null) {
            throw new IllegalArgumentException("El objeto tasa no puede ser null");
        }
        if (tasa.getFechaTasaCambio() == null) {
            throw new IllegalArgumentException("La fecha de la tasa es obligatoria");
        }
        if (tasa.getMonedaOrigen() == null || tasa.getMonedaOrigen().isBlank()) {
            throw new IllegalArgumentException("Moneda origen es obligatoria");
        }
        if (tasa.getMonedaDestino() == null || tasa.getMonedaDestino().isBlank()) {
            throw new IllegalArgumentException("Moneda destino es obligatoria");
        }
        if (tasa.getValor() == null || tasa.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor debe ser mayor que cero");
        }
        tasaCambioDAO.registrarTasaCambio(tasa);
    }

 
    public TasadeCambio obtenerTasaCambioPorFecha(LocalDate fecha) throws SQLException {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser null");
        }
        return tasaCambioDAO.obtenerTasaCambioPorFecha(fecha);
    }


    public TasadeCambio obtenerUltimaTasaCambio() throws SQLException {
        return tasaCambioDAO.obtenerUltimaTasaCambio();
    }

    public List<TasadeCambio> obtenerTodasLasTasasCambio() throws SQLException {
        return tasaCambioDAO.obtenerTodasLasTasasCambio();
    }
}
