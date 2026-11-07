package ru.netology.api;

import io.restassured.http.ContentType;
import ru.netology.data.DataHelper;
import static io.restassured.RestAssured.given;

public class AuthorizationAPI {

    public void generationCode() {
        given()
                .baseUri("http://localhost:9999/api")
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"login\": \"vasya\",\n" +
                        "  \"password\": \"qwerty123\"\n" +
                        "}")
                .when()
                .log()
                .all()
                .post("/auth")
                .then()
                .log()
                .all()
                .statusCode(200)
        ;
    }

    public String getToken(String code) {
        DataHelper.VerificationCodeAPI bodyRequest = new DataHelper.VerificationCodeAPI("vasya", code);
        return given()
                .baseUri("http://localhost:9999/api/auth")
                .contentType(ContentType.JSON)
                .body(bodyRequest)
                .when()
                .log()
                .all()
                .post("/verification")
                .then()
                .log()
                .all()
                .statusCode(200)
                .extract()
                .path("token");
    }
}