package com.wildrimak.credit_score.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

import com.wildrimak.credit_score.utils.CreditoTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.wildrimak.credit_score.api.dtos.CreditoDto;
import com.wildrimak.credit_score.domain.models.Credito;
import com.wildrimak.credit_score.api.mappers.CreditoMapper;

class CreditoMapperTest {

    private CreditoMapper mapper;

    @BeforeEach
    void setup() {
        mapper = Mappers.getMapper(CreditoMapper.class);
    }

    @Test
    @DisplayName("fromCredito: Deve mapear todas as propriedades corretamente")
    void fromCredito_deveMapearTodasPropriedadesCorretamente() {

        // given
        Credito credito = CreditoTestData.creditoC001();
        credito.setId(1L);
        credito.setTipoCredito("Tipo A");
        credito.setSimplesNacional(true);
        credito.setAliquota(new BigDecimal("0.05"));
        credito.setValorFaturado(new BigDecimal("1000.00"));
        credito.setValorDeducao(new BigDecimal("50.00"));
        credito.setBaseCalculo(new BigDecimal("950.00"));

        // when
        CreditoDto dto = mapper.fromCredito(credito);

        // then
        assertThat(dto).isNotNull();
        assertThat(dto.numeroCredito()).isEqualTo(credito.getNumeroCredito());
        assertThat(dto.numeroNfse()).isEqualTo(credito.getNumeroNfse());
        assertThat(dto.dataConstituicao()).isEqualTo(credito.getDataConstituicao());
        assertThat(dto.valorIssqn()).isEqualByComparingTo(credito.getValorIssqn());
        assertThat(dto.tipoCredito()).isEqualTo(credito.getTipoCredito());
        assertThat(dto.simplesNacional()).isEqualTo(credito.isSimplesNacional());
        assertThat(dto.aliquota()).isEqualByComparingTo(credito.getAliquota());
        assertThat(dto.valorFaturado()).isEqualByComparingTo(credito.getValorFaturado());
        assertThat(dto.valorDeducao()).isEqualByComparingTo(credito.getValorDeducao());
        assertThat(dto.baseCalculo()).isEqualByComparingTo(credito.getBaseCalculo());

    }

    @Test
    @DisplayName("fromCreditoList: Deve mapear lista de Credito para lista de CreditoDto")
    void fromCreditoList_deveMapearListaDeCreditosParaListaDeDtos() {

        // given
        Credito credito1 = CreditoTestData.creditoC001();
        Credito credito2 = CreditoTestData.creditoC002();

        List<Credito> creditos = List.of(credito1, credito2);

        // when
        List<CreditoDto> dtos = mapper.fromCreditoList(creditos);

        // then
        assertThat(dtos).hasSize(2);

        assertThat(dtos).extracting(CreditoDto::numeroCredito)
                .containsExactlyInAnyOrder(credito1.getNumeroCredito(), credito2.getNumeroCredito());

        assertThat(dtos.get(0).valorIssqn()).isEqualByComparingTo(credito1.getValorIssqn());
        assertThat(dtos.get(1).valorIssqn()).isEqualByComparingTo(credito2.getValorIssqn());

    }

}
