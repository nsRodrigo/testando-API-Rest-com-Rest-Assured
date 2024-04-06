package br.sp.nsrodrigo.rest.tests.refac;

import br.sp.nsrodrigo.rest.core.BaseTest;
import br.sp.nsrodrigo.rest.tests.Movimentacao;
import org.junit.Test;
import static br.sp.nsrodrigo.rest.utils.DateUtils.getDataDiferencaDeDias;
import static br.sp.nsrodrigo.rest.utils.MethodsUtils.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class MovimentacaoTest extends BaseTest {

    @Test
    public void deveIncluirMovimentacaoComSucesso() {
        Movimentacao mov = getMovimentacaoValida();
        given()
                .body(mov)
                .when()
                .post("/transacoes")
                .then()
                .statusCode(201)
                .extract().path("id");
    }

    @Test
    public void deveValidarCamposObrigatoriosNaMovimentacao() {
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
    public void naoDeveCadastrarMovimentacaoFutura() {
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
    public void naoDeveRemoverContaComMovimentacao() {
        Integer CONTA_ID = getIdContaPeloName("Conta para movimentacoes");
        given()
                .pathParam("id", CONTA_ID)
                .when()
                .delete("/contas/{id}")
                .then()
                .statusCode(500)
                .body("constraint", is("transacoes_conta_id_foreign"));
    }

    @Test
    public void deveRemoverMovimentacao() {
        Integer MOV_ID = getIdMovmentacaoPelaDescricao("Movimentacao para exclusao");
        given()
                .pathParam("id", MOV_ID)
                .when()
                .delete("/transacoes/{id}")
                .then()
                .statusCode(204);
    }


    private Movimentacao getMovimentacaoValida() {
        Movimentacao mov = new Movimentacao();
        mov.setConta_id(getIdContaPeloName("Conta para movimentacoes"));
        mov.setDescricao("Teste Movimentacao para conta");
        mov.setEnvolvido("Nataly");
        mov.setTipo("REC");
        mov.setData_transacao(getDataDiferencaDeDias(-1));
        mov.setData_pagamento(getDataDiferencaDeDias(5));
        mov.setValor(100f);
        mov.setStatus(true);
        return mov;
    }
}
