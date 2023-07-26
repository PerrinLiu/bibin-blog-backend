package com.llpy.utils;

import org.apache.tomcat.util.buf.HexUtils;

import java.security.MessageDigest;


public class Digest {
    public static String digest(String str, String alg, String charencoding) {
        try {
            byte[] data = str.getBytes(charencoding);
            MessageDigest md = MessageDigest.getInstance(alg);
            return HexUtils.toHexString(md.digest(data));
        } catch (Exception var5) {
            throw new RuntimeException("digest fail!", var5);
        }
    }
}