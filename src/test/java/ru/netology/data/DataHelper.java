package ru.netology.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class DataHelper {

    public DataHelper() {
    }

    public static VerificationCode getVerificationCodeValid(DataHelper.VerificationCode code) {
        return new VerificationCode(code.getCode());
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class VerificationCode {
        String code;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class VerificationCodeAPI {
        String login;
        String code;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CardInfo {
        String id;
        String number;
        int balance;
    }
}