package com.mycompany.pay.orders.model;

public enum TipoMovimiento {
    ENTRADA,
    SALIDA,
    AJUSTE;

    public static TipoMovimiento fromString(String tipo) throws IllegalArgumentException {
        if (tipo == null) throw new IllegalArgumentException("Tipo de movimiento no puede ser nulo");
        switch (tipo.trim().toLowerCase()) {
            case "entrada":
            case "ingreso":
                return ENTRADA;
            case "salida":
            case "egreso":
                return SALIDA;
            case "ajuste":
                return AJUSTE;
            default:
                throw new IllegalArgumentException("Tipo de movimiento inválido: " + tipo);
        }
    }
}
