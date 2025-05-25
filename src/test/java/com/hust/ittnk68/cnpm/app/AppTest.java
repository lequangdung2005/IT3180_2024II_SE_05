package com.hust.ittnk68.cnpm.app;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.security.*;

import io.jsonwebtoken.*;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.context.annotation.*;

import com.hust.ittnk68.cnpm.model.*;
import com.hust.ittnk68.cnpm.type.*;
import com.hust.ittnk68.cnpm.auth.*;
import com.hust.ittnk68.cnpm.security.*;

public class AppTest extends TestCase
{

    public AppTest( String testName )
    {
        super( testName );
    }

    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    public void testSign ()
    {
        RsaKeyUtil rsaKeyUtil = new RsaKeyUtil ();
        KeyPair keyPair = rsaKeyUtil.generateKeyPair ();
        PrivateKey privateKey = keyPair.getPrivate ();
        String token = Jwts.builder()
            .setSubject ("xyz")
            .signWith (privateKey, SignatureAlgorithm.RS256)
            .compact ();
        assertTrue (token != null);
    }

    public void testAuth () throws Exception
    {
        Account acc = new Account (59, "anbatocom", "dfjsdl39fjs???jdlfjslfloewjlkjlf", AccountType.USER);
        JwtUtil jwtUtil = new JwtUtil ();

        String token = jwtUtil.generateToken (acc);

        System.out.printf ("token = %s\n", token);

        Account tmp = jwtUtil.extract (token, Account.class);
        assertTrue (tmp.getFamilyId () == 59);
        assertTrue (tmp.getUsername().equals("anbatocom"));
        assertTrue (tmp.getDigestPassword().equals("dfjsdl39fjs???jdlfjslfloewjlkjlf"));
        assertTrue (tmp.getAccountType().equals(AccountType.USER));
    }

    public void testDigestPassword ()
    {
        String str = "root";
        System.out.println ("digest: " + DigestUtils.sha256Hex(str));
    }

}
