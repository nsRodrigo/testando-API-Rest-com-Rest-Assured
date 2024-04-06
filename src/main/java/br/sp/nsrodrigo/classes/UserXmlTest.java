package br.sp.nsrodrigo.com;

import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class UserXmlTest {

    @Test
    public void devoTrabalharComXML() {
        given()
        .when()
            .get("https://restapi.wcaquino.me/usersXML/3")
        .then()
            .statusCode(200)

            .rootPath("user")
            .body("name", is("Ana Julia"))
            .body("@id", is("3"))

            .rootPath("user.filhos")
            .body("name.size()", is("2"))


            .detachRootPath("filhos")
            .body("filhos.name[0]", is("Zezinho"))
            .body("filhos.name[1]", is("Luizinho"))

            .appendRootPath("filhos")
            .body("name", hasItem("Luizinho"))
            .body("name", hasItems("Luizinho", "Zezinho"));
    }

    @Test
    public void devoFazerPesquisasAvancadasComXML() {
        given()
                .when()
                .get("https://restapi.wcaquino.me/usersXML/3")
                .then()
                .statusCode(200)

                .rootPath("user")
                .body("name", is("Ana Julia"))
                .body("@id", is("3"))

                .rootPath("user.filhos")
                .body("name.size()", is("2"))

                .detachRootPath("filhos")
                .body("filhos.name", hasItem("Luizinho"))
                .body("filhos.name", hasItems("Luizinho", "Zezinho"))

                .appendRootPath("filhos")
                .body("name[0]", is("Zezinho"))
                .body("name[1]", is("Luizinho"));

    }

    @Test
    public void devoTrabalharComXMLAvançado() {
        given()
                .when()
                .get("https://restapi.wcaquino.me/usersXML")
                .then()
                .statusCode(200)

                .rootPath("user")
                .body("name", is("Ana Julia"))
                .body("@id", is("3"))

                .rootPath("user.filhos")
                .body("name.size()", is("2"))

                .detachRootPath("filhos")
                .body("filhos.name", hasItem("Luizinho"))
                .body("filhos.name", hasItems("Luizinho", "Zezinho"))

                .appendRootPath("filhos")
                .body("name[0]", is("Zezinho"))
                .body("name[1]", is("Luizinho"));

    }
}
