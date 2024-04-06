package br.sp.nsrodrigo.rest.utils;

import static io.restassured.RestAssured.*;

public class MethodsUtils {

    public static Integer getIdContaPeloName(String nome) {
        return get("/contas?nome=" + nome).then().extract().path("id[0]");
    }

    public static Integer getIdMovmentacaoPelaDescricao(String desc) {
        return get("/transacoes?descricao=" + desc).then().extract().path("id[0]");
    }

}
