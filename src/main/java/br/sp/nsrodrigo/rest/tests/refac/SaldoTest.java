package br.sp.nsrodrigo.rest.tests.refac;

import br.sp.nsrodrigo.rest.core.BaseTest;
import org.junit.Test;

import static br.sp.nsrodrigo.rest.utils.MethodsUtils.getIdContaPeloName;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class SaldoTest extends BaseTest {

    @Test
    public void deveCalcularSaldoDasContas() {
        Integer CONTA_ID = getIdContaPeloName("Conta para saldo");
        given()
                .when()
                .get("/saldo")
                .then()
                .statusCode(200)
                .body("find{it.conta_id == "+CONTA_ID+"}.saldo", is("534.00"));
    }

}
