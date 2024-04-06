package br.sp.nsrodrigo.rest.tests;

import br.sp.nsrodrigo.rest.core.BaseTest;
import io.restassured.specification.FilterableRequestSpecification;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.HashMap;
import java.util.Map;

import static br.sp.nsrodrigo.rest.utils.DateUtils.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BarrigaTest extends BaseTest {

    private static String CONTA_NAME = "Conta" + System.nanoTime();
    private static Integer CONTA_ID;
    private static Integer MOV_ID;

    @BeforeClass
    public static void accessToken() {
        Map<String, String> login = new HashMap<>();
        login.put("email", "rodrigorest@assured");
        login.put("senha", "123456");

        String token = given()
                .body(login)
                .when()
                .post("/signin")
                .then()
                .statusCode(200)
                .extract().path("token");

        requestSpecification.header("Authorization", "JWT " + token);
    }

    @Test
    public void tc01_deveIncluirContaComSucesso() {
        CONTA_ID = given()
                .body("{ \"nome\": \""+CONTA_NAME+"\" }")
                .when()
                .post("/contas")
                .then()
                .log().all()
                .statusCode(201)
                .body("id", notNullValue())
                .body("nome", is(CONTA_NAME))
                .body("visivel", equalTo(true))
                .body("usuario_id", notNullValue())
                .extract().path("id");
    }


    @Test
    public void tc02_naoDeveInserirContaComMesmoNome() {
        given()
                .body("{ \"nome\": \""+CONTA_NAME+"\" }")
                .when()
                .post("/contas")
                .then()
                .statusCode(400)
                .body("error", is("Já existe uma conta com esse nome!"));
    }

    @Test
    public void tc03_deveAlterarContaComSucesso() {
        given()
                .pathParam("id", CONTA_ID)
                .body("{ \"nome\": \""+CONTA_NAME+" Alterada\" }")
                .when()
                .put("/contas/{id}")
                .then()
                .statusCode(200)
                .body("nome", is(CONTA_NAME+" Alterada"));
    }

    @Test
    public void tc04_deveIncluirMovimentacaoComSucesso() {
        Movimentacao mov = getMovimentacaoValida();
        MOV_ID = given()
                .body(mov)
                .when()
                .post("/transacoes")
                .then()
                .statusCode(201)
                .extract().path("id");
    }

    @Test
    public void tc05_deveValidarCamposObrigatoriosNaMovimentacao() {
        given()
                .body("{}")
                .when()
                .post("/transacoes")
                .then()
                .statusCode(400)
                .body("$", hasSize(8))
                .body("msg", hasItems("Data da Movimentação é obrigatório",
                "Data do pagamento é obrigatório",
                "Descrição é obrigatório",
                "Interessado é obrigatório",
                "Valor é obrigatório",
                "Valor deve ser um número",
                "Conta é obrigatório",
                "Situação é obrigatório"));
    }

    @Test
    public void tc06_naoDeveCadastrarMovimentacaoFutura() {
        Movimentacao mov = getMovimentacaoValida();
        mov.setData_transacao(getDataDiferencaDeDias(2));

        given()
                .body(mov)
                .when()
                .post("/transacoes")
                .then()
                .statusCode(400)
                .body("param", hasItem("data_transacao"))
                .body("msg", hasItem("Data da Movimentação deve ser menor ou igual à data atual"))
                .body("value", hasItem(getDataDiferencaDeDias(2)))
        ;
    }

    @Test
    public void tc07_naoDeveRemoverContaComMovimentacao() {
        given()
                .pathParam("id", CONTA_ID)
                .when()
                .delete("/contas/{id}")
                .then()
                .statusCode(500)
                .body("constraint", is("transacoes_conta_id_foreign"));
    }

    @Test
    public void tc08_deveCalcularSaldoDasContas() {
        given()
                .when()
                .get("/saldo")
                .then()
                .statusCode(200)
                .body("find{it.conta_id == "+CONTA_ID+"}.saldo", is("100.00"));
    }

    @Test
    public void tc09_deveRemoverMovimentacao() {
        given()
                .pathParam("id", MOV_ID)
                .when()
                .delete("/transacoes/{id}")
                .then()
                .statusCode(204);
    }

    @Test
    public void tc10_naoDeveAcessarAPISemToken() {
        FilterableRequestSpecification req = (FilterableRequestSpecification) requestSpecification;
        req.removeHeader("Authorization");

        given()
                .when()
                .get("/contas")
                .then()
                .statusCode(401)
        ;
    }

    private Movimentacao getMovimentacaoValida() {
        Movimentacao mov = new Movimentacao();
        mov.setConta_id(CONTA_ID);
        mov.setDescricao("Teste Movimentacao para conta "+CONTA_ID+"");
        mov.setEnvolvido("Nataly");
        mov.setTipo("REC");
        mov.setData_transacao(getDataDiferencaDeDias(-1));
        mov.setData_pagamento(getDataDiferencaDeDias(5));
        mov.setValor(100f);
        mov.setStatus(true);
        return mov;
    }
}


