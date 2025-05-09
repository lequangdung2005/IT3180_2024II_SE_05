package com.hust.ittnk68.cnpm.auth;

import java.security.*;

public class RsaKeyUtil
{
    public KeyPair generateKeyPair ()
    {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance ("RSA");
            generator.initialize (4096);
            return generator.generateKeyPair ();
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new IllegalStateException ("RSA algorithm not available", e);
        }
    }
}
