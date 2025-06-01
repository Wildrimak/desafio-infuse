package com.wildrimak.credit_score.api.controllers;

import com.wildrimak.credit_score.api.dtos.CreditoDto;
import com.wildrimak.credit_score.api.mappers.CreditoMapper;
import com.wildrimak.credit_score.domain.exceptions.NumeroCreditoNaoEncontradoException;
import com.wildrimak.credit_score.domain.models.Credito;
import com.wildrimak.credit_score.domain.services.CreditoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreditoControllerTest {

    private CreditoService creditoService;
    private CreditoMapper creditoMapper;
    private CreditoController creditoController;

    @BeforeEach
    void setUp() {
        creditoService = Mockito.mock(CreditoService.class);
        creditoMapper = Mockito.mock(CreditoMapper.class);
        creditoController = new CreditoController(creditoService, creditoMapper);
    }

    @Test
    @DisplayName("Dado o número NFS-e, quando houver créditos associados, então deve retornar uma lista de CreditoDto")
    void dadoNumeroNfse_quandoCreditosExistem_entaoRetornaListaDeCreditoDto() {

        // Arrange
        String numeroNfse = "NFSE123";

        Credito credito1 = createCreditoWith(numeroNfse, "CRED789");
        Credito credito2 = createCreditoWith(numeroNfse, "CRED790");
        List<Credito> creditos = List.of(credito1, credito2);

        CreditoDto dto1 = createCreditoDtoWith(numeroNfse, "CRED789");
        CreditoDto dto2 = createCreditoDtoWith(numeroNfse, "CRED790");
        List<CreditoDto> creditosDto = List.of(dto1, dto2);

        when(creditoService.getCreditos(numeroNfse)).thenReturn(creditos);
        when(creditoMapper.fromCreditoList(creditos)).thenReturn(creditosDto);

        // Act
        ResponseEntity<List<CreditoDto>> response = creditoController.getCreditosFromNfse(numeroNfse);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(creditosDto, response.getBody());

        verify(creditoService).getCreditos(numeroNfse);
        verify(creditoMapper).fromCreditoList(creditos);

    }

    @Test
    @DisplayName("Dado o número da NFS-e, quando não houver créditos, então deve retornar lista vazia")
    void getCreditosFromNfse_quandoNaoExistemCreditos_deveRetornarListaVazia() {

        // Arrange
        String numeroNfse = "NFSE456";
        when(creditoService.getCreditos(numeroNfse)).thenReturn(Collections.emptyList());
        when(creditoMapper.fromCreditoList(Collections.emptyList())).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<CreditoDto>> response = creditoController.getCreditosFromNfse(numeroNfse);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(creditoService).getCreditos(numeroNfse);
        verify(creditoMapper).fromCreditoList(Collections.emptyList());

    }

    @Test
    @DisplayName("Dado o número crédito, quando houver crédito associado, então deve retornar um CreditoDto")
    void getCreditosFromNumeroCredito_quandoCreditoExiste_deveRetornarCreditoDto() {

        // Arrange
        String numeroCredito = "CRED789";
        String numeroNfse = "NFSE789";
        Credito creditoMock = createCreditoWith(numeroNfse, numeroCredito);
        CreditoDto creditoDtoMock = createCreditoDtoWith(numeroNfse, numeroCredito);

        when(creditoService.getCredito(numeroCredito)).thenReturn(creditoMock);
        when(creditoMapper.fromCredito(creditoMock)).thenReturn(creditoDtoMock);

        // Act
        ResponseEntity<CreditoDto> response = creditoController.getCreditosFromNumeroCredito(numeroCredito);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(creditoDtoMock, response.getBody());

        verify(creditoService).getCredito(numeroCredito);
        verify(creditoMapper).fromCredito(creditoMock);

    }

    @Test
    @DisplayName("Dado o número crédito, quando ele não existir, então deve retornar status 404 com mensagem de erro")
    void getCreditosFromNumeroCredito_quandoCreditoNaoExiste_deveLancarNumeroCreditoNaoEncontradoException() {

        // Arrange
        String numeroCreditoInexistente = "CRED_INEXISTENTE";
        when(creditoService.getCredito(numeroCreditoInexistente))
                .thenThrow(new NumeroCreditoNaoEncontradoException(numeroCreditoInexistente));


        // Act and Assert
        NumeroCreditoNaoEncontradoException ex = assertThrows(
                NumeroCreditoNaoEncontradoException.class,
                () -> creditoController.getCreditosFromNumeroCredito(numeroCreditoInexistente)
        );

        assertEquals("Número de crédito 'CRED_INEXISTENTE' não encontrado.", ex.getMessage());

        verify(creditoService).getCredito(numeroCreditoInexistente);
        verify(creditoMapper, never()).fromCredito(any(Credito.class));

    }

    private CreditoDto createCreditoDtoWith(String numeroNfse, String numeroCredito) {
        return new CreditoDto(
                numeroCredito,
                numeroNfse,
                LocalDate.of(2025, 1, 1),
                new BigDecimal("100.00"),
                "NORMAL",
                false,
                new BigDecimal("0.05"),
                new BigDecimal("2000.00"),
                new BigDecimal("50.00"),
                new BigDecimal("1950.00")
        );
    }

    private Credito createCreditoWith(String numeroNfse, String numeroCredito) {

        Credito credito = new Credito();
        credito.setId(1L);
        credito.setNumeroCredito(numeroCredito);
        credito.setNumeroNfse(numeroNfse);
        credito.setDataConstituicao(LocalDate.of(2025, 1, 1));
        credito.setValorIssqn(new BigDecimal("100.00"));
        credito.setTipoCredito("NORMAL");
        credito.setSimplesNacional(false);
        credito.setAliquota(new BigDecimal("0.05"));
        credito.setValorFaturado(new BigDecimal("2000.00"));
        credito.setValorDeducao(new BigDecimal("50.00"));
        credito.setBaseCalculo(new BigDecimal("1950.00"));

        return credito;

    }

}