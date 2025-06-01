package com.wildrimak.credit_score.domain.services;

import com.wildrimak.credit_score.domain.models.Credito;

import java.util.List;

public interface CreditoService {

    List<Credito> getCreditos(String numeroNfse);

    Credito getCredito(String numeroCredito);

}
