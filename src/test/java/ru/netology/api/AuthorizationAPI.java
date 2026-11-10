package ru.netology.api;

import io.restassured.http.ContentType;
import org.json.JSONObject;
import ru.netology.data.DataHelper;
import static io.restassured.RestAssured.given;

public class AuthorizationAPI {

    public AuthorizationAPI() {
    }

    public static void generationCode(DataHelper.AuthInfo authInfo) {
        JSONObject json = new JSONObject();
        json.put("login", authInfo.getLogin());
        json.put("password", authInfo.getPassword());
        given()
                .baseUri("http://localhost:9999/api")
                .contentType(ContentType.JSON)
                .body(json.toString())
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

    public static String getToken(String code, DataHelper.AuthInfo authInfo) {
        JSONObject json = new JSONObject();
        json.put("login", authInfo.getLogin());
        json.put("code", code);
        return given()
                .baseUri("http://localhost:9999/api/auth")
                .contentType(ContentType.JSON)
                .body(json.toString())
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