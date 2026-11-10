package ru.netology.api;

import io.restassured.http.ContentType;
import org.json.JSONObject;
import ru.netology.data.DataHelper;
import static io.restassured.RestAssured.given;

public class WorkWithCardsAPI {

    public WorkWithCardsAPI() {
    }

    public static DataHelper.CardInfo[] getInfoCards(String token) {
        return given()
                .baseUri("http://localhost:9999/api")
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .log()
                .all()
                .get("/cards")
                .then()
                .log()
                .all()
                .statusCode(200)
                .extract()
                .as(DataHelper.CardInfo[].class)
                ;
    }

    public static String getNumberCard(String token, int index) {
        String numberCard = "";
        var cards = getInfoCards(token);
        String hiddenNumbercards = cards[index].getNumber();
        if (hiddenNumbercards.contains("**** **** **** 0001")) {
            numberCard = "5559 0000 0000 0001";
        }
        if (hiddenNumbercards.contains("**** **** **** 0002")) {
            numberCard = "5559 0000 0000 0002";
        }
        return numberCard;
    }

    public static void transfer(String token, String from, String to, int amount) {
        JSONObject json = new JSONObject();
        json.put("amount", amount);
        json.put("from", from);
        json.put("to", to);
        given()
                .baseUri("http://localhost:9999/api")
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(json.toString())
                .when()
                .log()
                .all()
                .post("/transfer")
                .then()
                .log()
                .all()
                .statusCode(200)
        ;
    }
}