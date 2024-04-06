package br.sp.nsrodrigo.classes;

import static io.restassured.RestAssured.*;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class OlaMundoTest {

    @Test
    public void testOlaMundo() {
        Response response = request(Method.GET, "https://restapi.wcaquino.me/ola");
        Assert.assertTrue(response.getBody().asString().equals("Ola Mundo!"));
        Assert.assertTrue("status code deveria ser 200", response.statusCode() == 200);
        Assert.assertEquals(200, response.statusCode());

        ValidatableResponse validacao = response.then();
        validacao.statusCode(200);
    }

    @Test
    public void devoConhecerOutrasFormasRestAssured() {
        Response response = request(Method.GET, "https://restapi.wcaquino.me/ola");
        ValidatableResponse validacao = response.then();
        validacao.statusCode(200);

        get("https://restapi.wcaquino.me/ola").then().statusCode(200);

        given()
            //Pré Condições
        .when()
            .get("https://restapi.wcaquino.me/ola")
        .then()
            .statusCode(200);
    }

    @Test
    public void devoConhecerOsMatchersComHamcrest(){
        assertThat("Maria", is("Maria"));
        assertThat(128, is(128));
        assertThat(128, isA(Integer.class));
        assertThat(128d, isA(Double.class));
        assertThat(128d, greaterThan(120d));
        assertThat(128d, lessThan(130d));

        List<Integer> impares = Arrays.asList(1, 3, 5, 7, 9);
        assertThat(impares, hasSize(5));
        assertThat(impares, containsInAnyOrder(3, 5, 7, 9, 1));
        assertThat(impares, hasItem(1));
        assertThat(impares, hasItems(1, 5));

        assertThat("Maria", not("João"));
        assertThat("Maria", is(not("João")));
        assertThat("Joaquina", anyOf(is("Maria"), is("Joaquina")));
        assertThat("Joaquina", allOf(startsWith("Joa"), endsWith("ina"), containsString("aqui")));
    }

    @Test
    public void devoValidarOBody() {
        given()
                //Pré Condições
        .when()
            .get("https://restapi.wcaquino.me/ola")
        .then()
            .statusCode(200)
            .body(is("Ola Mundo!"))
            .body(containsString("Mundo"))
            .body(is(not(nullValue())));
    }

}