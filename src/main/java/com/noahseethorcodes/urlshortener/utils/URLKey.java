package com.noahseethorcodes.urlshortener.utils;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Random;

public class URLKey {
    //Declare constants
    public static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String lower = upper.toLowerCase(Locale.ROOT);
    public static final String digits = "0123456789";
    public static final String alphanum = upper + lower + digits;
    private final Random random = new SecureRandom();

    private final char[] buf;

    public URLKey(int length) {
        if (length < 1)
            throw new IllegalArgumentException("length < 1: " + length);
        buf = new char[length];
    }

    public String NewKey() {
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = alphanum.charAt(random.nextInt(alphanum.length()));
        return new String(buf);
    }
}
