package com.soltelec.consolaentrada.models.Dtos;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class InfoHojaPruebas {
    private Integer idHojaPrueba;
    private String placa;
    private boolean preventiva;
    private Integer numeroIntentos;
    private LocalDateTime fechaIngreso;
}
