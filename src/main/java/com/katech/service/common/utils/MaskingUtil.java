package com.katech.service.common.utils;

public class MaskingUtil {

    public static String maskString(String input) {
        int length = input.length();
        return "*".repeat(6) + input.substring(length - 4);
    }
}
