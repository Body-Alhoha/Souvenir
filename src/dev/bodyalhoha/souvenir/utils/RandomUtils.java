package dev.bodyalhoha.souvenir.utils;

import dev.bodyalhoha.souvenir.Obfuscator;

import java.nio.charset.Charset;

public class RandomUtils {

    public static String getRandomUTF(int length){
        byte[] array = new byte[length];
        Obfuscator.getInstance().r.nextBytes(array);
        return new String(array, Charset.defaultCharset());
    }

    public static String getRandomAscii(int length) {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomLimitedInt = 97 + (int)
                    (Obfuscator.getInstance().r.nextFloat() * (122 - 97 + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }
}
