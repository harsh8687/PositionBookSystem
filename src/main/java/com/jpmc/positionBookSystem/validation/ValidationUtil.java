package com.jpmc.positionBookSystem.validation;

public class ValidationUtil {

    public static void validateNotNullAndEmpty(String input, String errorMessage) {
        if (input == null || input.isEmpty())
            throw new IllegalArgumentException(errorMessage);
    }
}
