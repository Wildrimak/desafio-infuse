package com.wildrimak.credit_score.api.mappers;

import com.wildrimak.credit_score.api.dtos.CreditoDto;
import com.wildrimak.credit_score.domain.models.Credito;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CreditoMapper {

    CreditoDto fromCredito(Credito credito);

    List<CreditoDto> fromCreditoList(List<Credito> creditos);

}
