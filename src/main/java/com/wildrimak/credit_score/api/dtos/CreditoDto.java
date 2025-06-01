package com.wildrimak.credit_score.api.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreditoDto(
        String numeroCredito,
        String numeroNfse,
        LocalDate dataConstituicao,
        BigDecimal valorIssqn,
        String tipoCredito,
        boolean simplesNacional,
        BigDecimal aliquota,
        BigDecimal valorFaturado,
        BigDecimal valorDeducao,
        BigDecimal baseCalculo
) {
}
