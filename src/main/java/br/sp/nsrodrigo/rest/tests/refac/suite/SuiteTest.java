package br.sp.nsrodrigo.rest.tests.refac.suite;

import br.sp.nsrodrigo.rest.core.BaseTest;
import br.sp.nsrodrigo.rest.tests.refac.AuthTest;
import br.sp.nsrodrigo.rest.tests.refac.ContasTest;
import br.sp.nsrodrigo.rest.tests.refac.MovimentacaoTest;
import br.sp.nsrodrigo.rest.tests.refac.SaldoTest;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        ContasTest.class,
        MovimentacaoTest.class,
        SaldoTest.class,
        AuthTest.class
})

public class SuiteTest extends BaseTest {
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
        get("/reset").then().statusCode(200);
    }
}
