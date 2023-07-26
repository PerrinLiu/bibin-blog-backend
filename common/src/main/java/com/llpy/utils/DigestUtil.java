package com.llpy.utils;


public class DigestUtil {
    private static String DEFAULT_ENCODING = "UTF-8";
    private static String SHA_256 = "SHA-256";

    public static String sha256Digest(String str) {
        return Digest.digest(str, SHA_256, DEFAULT_ENCODING);
    }
}