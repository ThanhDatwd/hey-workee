package com.katech.service.common.utils;

import java.util.Base64;

public class Base64Util {

    public static String base64Encode(String rawStr) {
        return Base64.getEncoder().encodeToString(rawStr.getBytes());
    }

    public static String base64Decode(String encodedStr) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedStr);
        return new String(decodedBytes);
    }
}
