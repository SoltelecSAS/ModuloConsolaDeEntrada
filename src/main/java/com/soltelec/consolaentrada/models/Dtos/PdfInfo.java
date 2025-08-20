package com.soltelec.consolaentrada.models.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PdfInfo {
    private String placa;
    private String nitCda;
    private byte[] pdfBytes;
}
