package com.wildrimak.credit_score.api.handlers;

import com.wildrimak.credit_score.api.dtos.ErrorHandlerDto;
import com.wildrimak.credit_score.domain.exceptions.NumeroCreditoNaoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(NumeroCreditoNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorHandlerDto handleNumeroCreditoNaoEncontrado(NumeroCreditoNaoEncontradoException exception){
        return new ErrorHandlerDto(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                exception.getMessage(),
                LocalDateTime.now()
        );
    }

}
