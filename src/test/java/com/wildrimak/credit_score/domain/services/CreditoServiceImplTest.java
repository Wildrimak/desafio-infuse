package com.wildrimak.credit_score.domain.services;

import com.wildrimak.credit_score.domain.exceptions.NumeroCreditoNaoEncontrado;
import com.wildrimak.credit_score.domain.models.Credito;
import com.wildrimak.credit_score.domain.repositories.CreditoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreditoServiceImplTest {

    private CreditoRepository creditoRepository;
    private CreditoServiceImpl creditoService;

    @BeforeEach
    void setUp() {
        this.creditoRepository = mock(CreditoRepository.class);
        this.creditoService = new CreditoServiceImpl(creditoRepository);
    }

    @Test
    @DisplayName("Deve retornar uma lista de créditos quando o número da NFS-e existir")
    void getCreditos_quandoNumeroNfseExistir_deveRetornarListaDeCreditos() {

        String numeroNfseExistente = "NFSE123";
        Credito credito1 = new Credito();
        credito1.setNumeroNfse(numeroNfseExistente);
        Credito credito2 = new Credito();
        credito2.setNumeroNfse(numeroNfseExistente);
        List<Credito> creditosEsperados = List.of(credito1, credito2);

        when(creditoRepository.findByNumeroNfse(numeroNfseExistente)).thenReturn(creditosEsperados);

        List<Credito> creditosAtuais = creditoService.getCreditos(numeroNfseExistente);

        assertNotNull(creditosAtuais);
        assertEquals(2, creditosAtuais.size());
        assertTrue(creditosAtuais.stream()
                .allMatch(credito -> numeroNfseExistente.equals(credito.getNumeroNfse())));
        verify(creditoRepository, times(1)).findByNumeroNfse(numeroNfseExistente);

    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando o número da NFS-e não tiver créditos associados")
    void getCreditos_quandoNumeroNfseNaoTiverCreditos_deveRetornarListaVazia() {

        String numeroNfseSemCreditos = "NFSE456";
        when(creditoRepository.findByNumeroNfse(numeroNfseSemCreditos)).thenReturn(Collections.emptyList());

        List<Credito> creditosAtuais = creditoService.getCreditos(numeroNfseSemCreditos);

        assertNotNull(creditosAtuais);
        assertTrue(creditosAtuais.isEmpty());
        verify(creditoRepository, times(1)).findByNumeroNfse(numeroNfseSemCreditos);

    }

    @Test
    @DisplayName("Deve retornar um crédito quando o número do crédito existir")
    void getCredito_quandoNumeroCreditoExistir_deveRetornarCredito() {

        String numeroCreditoExistente = "CRED789";
        Credito creditoEsperado = new Credito();
        creditoEsperado.setNumeroCredito(numeroCreditoExistente);
        when(creditoRepository.findByNumeroCredito(numeroCreditoExistente)).thenReturn(Optional.of(creditoEsperado));

        Credito creditoAtual = creditoService.getCredito(numeroCreditoExistente);

        assertNotNull(creditoAtual);
        assertEquals(creditoEsperado.getNumeroCredito(), creditoAtual.getNumeroCredito());
        verify(creditoRepository, times(1)).findByNumeroCredito(numeroCreditoExistente);

    }

    @Test
    @DisplayName("Deve lançar NumeroCreditoNaoEncontrado quando o número do crédito não existir")
    void getCredito_quandoNumeroCreditoNaoExistir_deveLancarExcecao() {

        String numeroCreditoInexistente = "CRED000";
        String mensagemEsperada = "Número de crédito '" + numeroCreditoInexistente + "' não encontrado.";

        when(creditoRepository.findByNumeroCredito(numeroCreditoInexistente)).thenReturn(Optional.empty());

        NumeroCreditoNaoEncontrado exception = assertThrows(NumeroCreditoNaoEncontrado.class, () -> {
            creditoService.getCredito(numeroCreditoInexistente);
        });

        assertEquals(mensagemEsperada, exception.getMessage());
        assertEquals(numeroCreditoInexistente, exception.getNumeroCredito());
        verify(creditoRepository, times(1)).findByNumeroCredito(numeroCreditoInexistente);

    }

}
