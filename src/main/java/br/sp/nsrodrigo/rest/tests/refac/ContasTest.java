package br.sp.nsrodrigo.rest.tests.refac;

import br.sp.nsrodrigo.rest.core.BaseTest;
import br.sp.nsrodrigo.rest.utils.MethodsUtils;
import org.junit.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ContasTest extends BaseTest {

    @Test
    public void deveIncluirContaComSucesso() {
        given()
                .body("{ \"nome\": \"Conta Inserida\" }")
                .when()
                .post("/contas")
                .then()
                .log().all()
                .statusCode(201)
                .body("id", notNullValue())
                .body("nome", is("Conta Inserida"))
                .body("visivel", equalTo(true))
                .body("usuario_id", notNullValue())
                .extract().path("id");
    }

    @Test
    public void naoDeveInserirContaComMesmoNome() {
        given()
                .body("{ \"nome\": \"Conta mesmo nome\" }")
                .when()
                .post("/contas")
                .then()
                .statusCode(400)
                .body("error", is("Já existe uma conta com esse nome!"));
    }

    @Test
    public void deveAlterarContaComSucesso() {
        Integer CONTA_ID = MethodsUtils.getIdContaPeloName("Conta para alterar");

        given()
                .pathParam("id", CONTA_ID)
                .body("{ \"nome\": \"Conta Alterada\" }")
                .when()
                .put("/contas/{id}")
                .then()
                .statusCode(200)
                .body("nome", is("Conta Alterada"));
    }
}
