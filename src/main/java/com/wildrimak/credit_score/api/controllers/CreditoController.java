package com.wildrimak.credit_score.api.controllers;

import com.wildrimak.credit_score.api.dtos.CreditoDto;
import com.wildrimak.credit_score.api.mappers.CreditoMapper;
import com.wildrimak.credit_score.domain.models.Credito;
import com.wildrimak.credit_score.domain.services.CreditoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/creditos")
public class CreditoController {

    private final CreditoService creditoService;
    private final CreditoMapper creditoMapper;

    CreditoController(CreditoService creditoService, CreditoMapper creditoMapper){
        this.creditoService = creditoService;
        this.creditoMapper = creditoMapper;
    }

    @GetMapping("/{numeroNfse}")
    public ResponseEntity<List<CreditoDto>> getCreditosFromNfse(@PathVariable String numeroNfse) {
        List<Credito> creditos = creditoService.getCreditos(numeroNfse);
        List<CreditoDto> creditoDtos = creditoMapper.fromCreditoList(creditos);
        return ResponseEntity.ok(creditoDtos);
    }

    @GetMapping("/credito/{numeroCredito}")
    public ResponseEntity<CreditoDto> getCreditosFromNumeroCredito(@PathVariable String numeroCredito) {
        Credito credito = creditoService.getCredito(numeroCredito);
        CreditoDto creditoDto = creditoMapper.fromCredito(credito);
        return ResponseEntity.ok(creditoDto);
    }

}
