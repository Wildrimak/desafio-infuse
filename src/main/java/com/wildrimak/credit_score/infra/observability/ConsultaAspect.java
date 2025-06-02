package com.wildrimak.credit_score.infra.observability;

import com.wildrimak.credit_score.infra.kafka.dtos.ConsultaLog;
import com.wildrimak.credit_score.infra.kafka.publisher.ConsultaKafkaPublisher;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Aspect
@Component
public class ConsultaAspect {

    private final ConsultaKafkaPublisher publisher;

    public ConsultaAspect(ConsultaKafkaPublisher publisher) {
        this.publisher = publisher;
    }

    @Around("@annotation(getMapping)")
    public Object logConsulta(ProceedingJoinPoint joinPoint, GetMapping getMapping) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String fullPath = buildFullPath(joinPoint, getMapping);
        String metodo = signature.toShortString();
        Map<String, Object> argsMap = buildArgsMap(signature, joinPoint.getArgs());
        String timestamp = LocalDateTime.now().toString();

        try {
            Object result = joinPoint.proceed();
            publisher.publish(ConsultaLog.sucesso(timestamp, fullPath, metodo, argsMap));
            return result;
        } catch (Exception ex) {
            String erro = ex.getClass().getSimpleName() + ": " + ex.getMessage();
            publisher.publish(ConsultaLog.erro(timestamp, fullPath, metodo, argsMap, erro));
            throw ex;
        }
    }

    private String buildFullPath(JoinPoint joinPoint, GetMapping getMapping) {
        Class<?> controllerClass = joinPoint.getTarget().getClass();
        String classPath = Optional.ofNullable(controllerClass.getAnnotation(RequestMapping.class))
                .map(r -> r.value().length > 0 ? r.value()[0] : "")
                .orElse("");

        String methodPath = getMapping.value().length > 0 ? getMapping.value()[0] : "";
        return classPath + methodPath;
    }

    private Map<String, Object> buildArgsMap(MethodSignature signature, Object[] values) {
        String[] names = signature.getParameterNames();
        Map<String, Object> argsMap = new LinkedHashMap<>();
        for (int i = 0; i < names.length; i++) {
            argsMap.put(names[i], values[i]);
        }
        return argsMap;
    }

}

