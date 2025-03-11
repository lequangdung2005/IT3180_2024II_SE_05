package com.hust.ittnk68.cnpm.session;

import java.util.Random;

public class Token
{
    final static int tokenLength = 256;
    final private static String characters = newCharacters ();

    static private String randomToken ()
    {
        Random rng = new Random ();
        char[] text = new char[tokenLength];
        for (int i = 0; i < tokenLength; ++i)
        {
            text [i] = characters.charAt (rng.nextInt (characters.length ()));
        }
        return new String (text);
    }

    static public String generateToken ()
    {
        String token;
        do
        {
            token = randomToken ();
        }
        while (SessionController.checkIfTokenExists (token));
        return token;
    }

    private static String newCharacters ()
    {
        String charaters = "[]{}()+-*/~";
        for (char c = 'a'; c <= 'z'; ++c)
            charaters += c;
        for (char c = 'A'; c <= 'Z'; ++c)
            charaters += c;
        for (char c = '0'; c <= '9'; ++c)
            charaters += c;
        return charaters;
    }
}
