package ru.netology.tests;

import org.junit.jupiter.api.*;
import ru.netology.api.AuthorizationAPI;
import ru.netology.api.WorkWithCardsAPI;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;

public class TransferTest {

    static AuthorizationAPI authorizationAPI = new AuthorizationAPI();
    WorkWithCardsAPI transferAPI = new WorkWithCardsAPI();
    static String token;

    @BeforeAll
    public static void loginAccount() throws InterruptedException {
        authorizationAPI.generationCode();
        Thread.sleep(3000);
        var verificationCode = DataHelper.getVerificationCodeValid(SQLHelper.getCode());
        var code = verificationCode.getCode();
        token = authorizationAPI.getToken(code);
    }

    @AfterEach
    public void returnOriginalStateBalance() {
        int difference1 = 10000 - transferAPI.getInfoCards(token)[0].getBalance();
        int difference2 = 10000 - transferAPI.getInfoCards(token)[1].getBalance();
        if (difference1 >= 0) {
            var from = transferAPI.getNumberCard(token, 1);
            var to = transferAPI.getNumberCard(token, 0);
            transferAPI.transfer(token, from, to, difference1);
        }
        if (difference1 < 0) {
            var from = transferAPI.getNumberCard(token, 0);
            var to = transferAPI.getNumberCard(token, 1);
            transferAPI.transfer(token, from, to, difference2);
        }
    }
    @AfterAll
    public static void returnOriginalStateDB() {
        SQLHelper.cleanDB();
    }

    @Test
    @DisplayName("Transfer from card1 to card2")
    public void transferTestFromCard1ToCard2() {
        var from = transferAPI.getNumberCard(token, 0);
        var to = transferAPI.getNumberCard(token, 1);
        int amount = 1000;
        transferAPI.transfer(token, from, to, amount);
        Assertions.assertEquals(9000, transferAPI.getInfoCards(token)[0].getBalance());
        Assertions.assertEquals(11000, transferAPI.getInfoCards(token)[1].getBalance());
    }

    @Test
    @DisplayName("Transfer from card2 to card1")
    public void transferTestFromCard2ToCard1() {
        var from = transferAPI.getNumberCard(token, 1);
        var to = transferAPI.getNumberCard(token, 0);
        int amount = 1000;
        transferAPI.transfer(token, from, to, amount);
        Assertions.assertEquals(9000, transferAPI.getInfoCards(token)[1].getBalance());
        Assertions.assertEquals(11000, transferAPI.getInfoCards(token)[0].getBalance());
    }

    @Test
    @DisplayName("Transfer if amount negativ")
    public void transferTestIfAmountNegativ() {
        var from = transferAPI.getNumberCard(token, 0);
        var to = transferAPI.getNumberCard(token, 1);
        int amount = -1000;
        transferAPI.transfer(token, from, to, amount);
        Assertions.assertEquals(10000, transferAPI.getInfoCards(token)[0].getBalance());
        Assertions.assertEquals(10000, transferAPI.getInfoCards(token)[1].getBalance());
    }

    @Test
    @DisplayName("Transfer if amount bigger balance")
    public void transferTestIfAmountBiggerBalance() {
        var from = transferAPI.getNumberCard(token, 0);
        var to = transferAPI.getNumberCard(token, 1);
        int amount = 11000;
        transferAPI.transfer(token, from, to, amount);
        Assertions.assertEquals(10000, transferAPI.getInfoCards(token)[0].getBalance());
        Assertions.assertEquals(10000, transferAPI.getInfoCards(token)[1].getBalance());
    }
}