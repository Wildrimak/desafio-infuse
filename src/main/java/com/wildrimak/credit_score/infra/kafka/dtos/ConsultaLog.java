package com.wildrimak.credit_score.infra.kafka.dtos;

import java.util.Map;

public record ConsultaLog(
        String timestamp,
        String url,
        String metodo,
        Map<String, Object> parametros,
        String erro
) {
    public static ConsultaLog sucesso(String timestamp, String url, String metodo, Map<String, Object> parametros) {
        return new ConsultaLog(timestamp, url, metodo, parametros, null);
    }

    public static ConsultaLog erro(String timestamp, String url, String metodo, Map<String, Object> parametros,
                                   String erro) {
        return new ConsultaLog(timestamp, url, metodo + " [EXCEPTION]", parametros, erro);
    }
}


