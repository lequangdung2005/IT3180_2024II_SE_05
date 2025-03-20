package com.hust.ittnk68.cnpm.session;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import com.hust.ittnk68.cnpm.exception.UserCreateSecondSession;

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

    public static String newSession (String username) throws UserCreateSecondSession
    {
        updateUserSessionState (username);
        String token = usernameToToken.get (username);
        if (token != null)
        {
            throw new UserCreateSecondSession (username);
        }
        token = Token.generateToken ();
        usernameToToken.put (username, token);
        tokenToSession.put (token, new Session (username));
        return token;
    }

    public static int endSession (String token)
    {
        if (!checkIfTokenExists (token))
        {
            return -1;
        }

        String username = tokenToSession.get(token).getUsername();
        usernameToToken.remove (username);
        tokenToSession.remove (token);
        return 0;
    }
}
