package com.wildrimak.credit_score.api.dtos;

import java.time.LocalDateTime;

public record ErrorHandlerDto(
        Integer status,
        String error,
        String message,
        LocalDateTime dateTime

) {
}
