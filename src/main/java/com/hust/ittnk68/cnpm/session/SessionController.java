package com.hust.ittnk68.cnpm.session;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import com.hust.ittnk68.cnpm.exception.UserCreateSecondSession;
import com.hust.ittnk68.cnpm.model.Account;
import com.hust.ittnk68.cnpm.type.AccountType;

public class SessionController
{
    private static Map <String, String> usernameToToken = new TreeMap <String, String> ();
    private static Map <String, Session> tokenToSession = new TreeMap <String, Session> ();

    private static void updateUserSessionState (String username)
    {
        String token = usernameToToken.get (username);
        if (token == null)
        {
            return;
        }
        Session session = tokenToSession.get (token);

        Date now = new Date ();
        if (now.after(session.getSessionEnd ()))
        {
            usernameToToken.remove (username);
            tokenToSession.remove (token);
        }
    }
    
    public static boolean checkIfTokenExists (String token)
    {
        return tokenToSession.get (token) != null;
    }

    public static Session getSession (String token) {
        Session session = tokenToSession.get (token);
        if (session == null) {
            return null;
        }
        updateUserSessionState (session.getAccount().getUsername ());
        return tokenToSession.get (token);
    }

    public static String newSession (Account account) throws UserCreateSecondSession
    {
        String username = account.getUsername();
        AccountType accountType = account.getAccountType();

        updateUserSessionState (username);
        String token = usernameToToken.get (username);
        if (token != null)
        {
            throw new UserCreateSecondSession (username);
        }
        token = Token.generateToken ();
        usernameToToken.put (username, token);
        tokenToSession.put (token, new Session (account, accountType));
        return token;
    }

    public static int endSession (String token)
    {
        if (!checkIfTokenExists (token))
        {
            return -1;
        }

        String username = tokenToSession.get(token).getAccount().getUsername();
        usernameToToken.remove (username);
        tokenToSession.remove (token);
        return 0;
    }
}
