package com.wildrimak.credit_score.domain.services;

import com.wildrimak.credit_score.domain.exceptions.NumeroCreditoNaoEncontrado;
import com.wildrimak.credit_score.domain.models.Credito;
import com.wildrimak.credit_score.domain.repositories.CreditoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreditoServiceImpl implements CreditoService {

    private final CreditoRepository creditoRepository;

    public CreditoServiceImpl(CreditoRepository creditoRepository) {
        this.creditoRepository = creditoRepository;
    }

    @Override
    public List<Credito> getCreditos(String numeroNfse) {
        return creditoRepository.findByNumeroNfse(numeroNfse);
    }

    @Override
    public Credito getCredito(String numeroCredito) {
        return creditoRepository
                .findByNumeroCredito(numeroCredito)
                .orElseThrow(() -> new NumeroCreditoNaoEncontrado(numeroCredito));
    }

}
