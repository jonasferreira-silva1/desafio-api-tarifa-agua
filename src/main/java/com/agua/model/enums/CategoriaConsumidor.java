package com.agua.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CategoriaConsumidor {
    COMERCIAL,
    INDUSTRIAL,
    PARTICULAR,
    PUBLICO;

    @JsonValue
    public String toValue() {
        return switch (this) {
            case PUBLICO -> "PÚBLICO";
            default -> this.name();
        };
    }

    @JsonCreator
    public static CategoriaConsumidor fromValue(String value) {
        if (value == null) {
            return null;
        }
        // Aceita tanto "PUBLICO" quanto "PÚBLICO" na entrada
        if (value.equalsIgnoreCase("PUBLICO") || value.equalsIgnoreCase("PÚBLICO")) {
            return PUBLICO;
        }
        // Para outras categorias, usa o nome padrão
        try {
            return valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}

