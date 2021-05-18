package server.commandHandler.utility;

import java.util.Random;
/**
 * Generates the string fields for flat if default constructor was called
 * **/
public class RandomStr {
    /**
     * Generates random string
     * @return random string
     * **/
    public static String randomStr(int len) {
        Random rand = new Random();
        String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789!@#$%^&*()\"№;%:?*{}`><,./'";
        String res = "";
        for (int i = 0; i<len; i++) {
            res += letters.charAt(rand.nextInt(letters.length()));
        }
        return res;
    }
}

