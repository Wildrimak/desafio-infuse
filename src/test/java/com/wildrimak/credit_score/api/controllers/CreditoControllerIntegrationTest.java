package com.wildrimak.credit_score.api.controllers;

import com.wildrimak.credit_score.api.dtos.CreditoDto;
import com.wildrimak.credit_score.config.TestKafkaConfiguration;
import com.wildrimak.credit_score.domain.repositories.CreditoRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.wildrimak.credit_score.utils.CreditoTestData.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import(TestKafkaConfiguration.class)
@ActiveProfiles("test")
public class CreditoControllerIntegrationTest {

    private final CreditoRepository creditoRepository;

    public CreditoControllerIntegrationTest(@Autowired CreditoRepository creditoRepository) {
        this.creditoRepository = creditoRepository;
    }

    @BeforeEach
    void setUp(@LocalServerPort Integer injectedPort) {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = injectedPort;
        creditoRepository.deleteAll();
        setupTestData();
    }

    @AfterEach
    void tearDown() {
        creditoRepository.deleteAll();
    }

    void setupTestData() {
        creditoRepository.save(creditoC001());
        creditoRepository.save(creditoC002());
        creditoRepository.save(creditoC003());
    }

    @Test
    @DisplayName("GET /api/creditos/{numeroNfse} - deve retornar 200 com a lista de créditos da NFSE existente")
    void getCreditosFromNfse_deveRetornarOkComListaDeCreditos_quandoNfsePossuiCreditos() {
        List<CreditoDto> creditos = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/creditos/{numeroNfse}", "NFSE001")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .extract()
                .body()
                .jsonPath()
                .getList(".", CreditoDto.class);

        assertThat(creditos).hasSize(2);
        assertThat(creditos).extracting(CreditoDto::numeroCredito).containsExactlyInAnyOrder("C001", "C002");

        CreditoDto dto1 = creditos.stream()
                .filter(c -> c.numeroCredito().equals("C001"))
                .findFirst()
                .orElseThrow();

        assertThat(dto1.valorIssqn()).isEqualByComparingTo("100.50");
        assertThat(dto1.dataConstituicao()).isEqualTo(creditoC001().getDataConstituicao());
    }

    @Test
    @DisplayName("GET /api/creditos/{numeroNfse} - deve retornar 200 com lista vazia quando NFSE não possui créditos")
    void getCreditosFromNfse_deveRetornarOkComListaVazia_quandoNfseNaoPossuiCreditos() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/creditos/{numeroNfse}", "NFSE_INEXISTENTE")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("$", empty());
    }

    @Test
    @DisplayName("GET /api/creditos/credito/{numeroCredito} - deve retornar 200 com os dados do crédito existente")
    void getCreditosFromNumeroCredito_deveRetornarOkComCreditoDto_quandoNumeroCreditoExiste() {
        CreditoDto creditoDto = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/creditos/credito/{numeroCredito}", "C001")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .extract()
                .as(CreditoDto.class);

        assertThat(creditoDto.numeroCredito()).isEqualTo("C001");
        assertThat(creditoDto.numeroNfse()).isEqualTo("NFSE001");
        assertThat(creditoDto.valorIssqn()).isEqualByComparingTo("100.50");
        assertThat(creditoDto.dataConstituicao()).isEqualTo(creditoC001().getDataConstituicao());

    }

    @Test
    @DisplayName("GET /api/creditos/credito/{numeroCredito} - deve retornar 404 quando o número do crédito não existir")
    void getCreditosFromNumeroCredito_deveRetornarNotFound_quandoNumeroCreditoNaoExiste() {
        String numeroCreditoNaoExistente = "C999";
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/creditos/credito/{numeroCredito}", numeroCreditoNaoExistente)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(ContentType.JSON)
                .body("message", containsString("Número de crédito '" + numeroCreditoNaoExistente + "' não encontrado."));
    }

}
