package com.wildrimak.credit_score.api.handlers;

import com.wildrimak.credit_score.api.dtos.ErrorHandlerDto;
import com.wildrimak.credit_score.domain.exceptions.NumeroCreditoNaoEncontradoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ApiExceptionHandlerTest {

    @Test
    @DisplayName("Deve retornar ErrorHandlerDto com status 404 e mensagem esperada")
    void deveRetornarErro404ParaNumeroCreditoNaoEncontrado() {

        // Arrange
        String numeroCredito = "CRED999";
        NumeroCreditoNaoEncontradoException exception =
                new NumeroCreditoNaoEncontradoException(numeroCredito);
        ApiExceptionHandler handler = new ApiExceptionHandler();

        // Act
        ErrorHandlerDto response = handler.handleNumeroCreditoNaoEncontrado(exception);

        // Assert
        assertNotNull(response);
        assertEquals(404, response.status());
        assertEquals("Not Found", response.error());
        assertEquals("Número de crédito 'CRED999' não encontrado.", response.message());
        assertNotNull(response.dateTime());

    }

}
