package com.mycompany.pay.orders.controller;

import com.mycompany.pay.orders.dao.MetodosdePagoDAO;
import com.mycompany.pay.orders.model.MetodosdePago;

import java.sql.SQLException;
import java.util.List;

public class MetodosdePagoController {

    private final MetodosdePagoDAO metodosPagoDAO;

    public MetodosdePagoController(MetodosdePagoDAO metodosPagoDAO) {
        this.metodosPagoDAO = metodosPagoDAO;
    }

    public void agregarMetodo(MetodosdePago metodo) throws SQLException {
        if (metodo == null || metodo.getDescripcion() == null || metodo.getDescripcion().isBlank()) {
            throw new IllegalArgumentException("La descripción del método de pago es obligatoria");
        }
        metodo.setActivo(true); 
        metodosPagoDAO.agregarMetododePago(metodo);
    }

    public MetodosdePago obtenerMetodoPorId(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido");
        }
        return metodosPagoDAO.obtenerMetododePagoPorId(id);
    }

    public List<MetodosdePago> listarMetodos() throws SQLException {
        return metodosPagoDAO.obtenerTodoslosMetodosdePago();
    }

    public void actualizarMetodo(MetodosdePago metodo) throws SQLException {
        if (metodo == null || metodo.getId() <= 0) {
            throw new IllegalArgumentException("Método de pago inválido para actualización");
        }
        if (metodo.getDescripcion() == null || metodo.getDescripcion().isBlank()) {
            throw new IllegalArgumentException("La descripción del método es obligatoria");
        }
        metodosPagoDAO.actualizarMetododePago(metodo);
    }

    public void eliminarMetodo(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido para eliminar");
        }
        metodosPagoDAO.eliminarMetododePago(id);
    }
}
