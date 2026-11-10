package ru.netology.tests;

import org.junit.jupiter.api.*;
import ru.netology.api.AuthorizationAPI;
import ru.netology.api.WorkWithCardsAPI;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;

public class TransferTest {

    static DataHelper.AuthInfo authInfo = new DataHelper.AuthInfo("vasya", "qwerty123");
    static String token;

    @BeforeAll
    public static void loginAccount() {
        AuthorizationAPI.generationCode(authInfo);
        var verificationCode = DataHelper.getVerificationCodeValid(SQLHelper.getCode());
        var code = verificationCode.getCode();
        token = AuthorizationAPI.getToken(code, authInfo);
    }

    @AfterEach
    public void returnOriginalStateBalance() {
        int difference1 = 10000 - WorkWithCardsAPI.getInfoCards(token)[0].getBalance();
        int difference2 = 10000 - WorkWithCardsAPI.getInfoCards(token)[1].getBalance();
        if (difference1 >= 0) {
            var from = WorkWithCardsAPI.getNumberCard(token, 1);
            var to = WorkWithCardsAPI.getNumberCard(token, 0);
            WorkWithCardsAPI.transfer(token, from, to, difference1);
        }
        if (difference1 < 0) {
            var from = WorkWithCardsAPI.getNumberCard(token, 0);
            var to = WorkWithCardsAPI.getNumberCard(token, 1);
            WorkWithCardsAPI.transfer(token, from, to, difference2);
        }
    }

    @AfterAll
    public static void returnOriginalStateDB() {
        SQLHelper.cleanDB();
    }

    @Test
    @DisplayName("Transfer from card1 to card2")
    public void transferTestFromCard1ToCard2() {
        var from = WorkWithCardsAPI.getNumberCard(token, 0);
        var to = WorkWithCardsAPI.getNumberCard(token, 1);
        int amount = 1000;
        WorkWithCardsAPI.transfer(token, from, to, amount);
        Assertions.assertEquals(9000, WorkWithCardsAPI.getInfoCards(token)[0].getBalance());
        Assertions.assertEquals(11000, WorkWithCardsAPI.getInfoCards(token)[1].getBalance());
    }

    @Test
    @DisplayName("Transfer from card2 to card1")
    public void transferTestFromCard2ToCard1() {
        var from = WorkWithCardsAPI.getNumberCard(token, 1);
        var to = WorkWithCardsAPI.getNumberCard(token, 0);
        int amount = 1000;
        WorkWithCardsAPI.transfer(token, from, to, amount);
        Assertions.assertEquals(9000, WorkWithCardsAPI.getInfoCards(token)[1].getBalance());
        Assertions.assertEquals(11000, WorkWithCardsAPI.getInfoCards(token)[0].getBalance());
    }

    @Test
    @DisplayName("Transfer if amount negativ")
    public void transferTestIfAmountNegativ() {
        var from = WorkWithCardsAPI.getNumberCard(token, 0);
        var to = WorkWithCardsAPI.getNumberCard(token, 1);
        int amount = -1000;
        WorkWithCardsAPI.transfer(token, from, to, amount);
        Assertions.assertEquals(10000, WorkWithCardsAPI.getInfoCards(token)[0].getBalance());
        Assertions.assertEquals(10000, WorkWithCardsAPI.getInfoCards(token)[1].getBalance());
    }

    @Test
    @DisplayName("Transfer if amount bigger balance")
    public void transferTestIfAmountBiggerBalance() {
        var from = WorkWithCardsAPI.getNumberCard(token, 0);
        var to = WorkWithCardsAPI.getNumberCard(token, 1);
        int amount = 11000;
        WorkWithCardsAPI.transfer(token, from, to, amount);
        Assertions.assertEquals(10000, WorkWithCardsAPI.getInfoCards(token)[0].getBalance());
        Assertions.assertEquals(10000, WorkWithCardsAPI.getInfoCards(token)[1].getBalance());
    }
}