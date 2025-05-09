package com.hust.ittnk68.cnpm.security;

import com.hust.ittnk68.cnpm.auth.JwtUtil;
import com.hust.ittnk68.cnpm.communication.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("authz")
public class AuthorizationService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private Token tokenGetter;

    private boolean checkToken (ClientMessageBase req)
    {
        String token = tokenGetter.get ();
        return jwtUtil.extractAccount (token).getUsername ()
            .equals (req.getUsername ());
    }

    // private boolean checkAdminPrivilege (ClientMessageBase req)
    // {
    //
    // }

    public boolean canStartSession(ClientMessageStartSession message) {
        // every one can /auth
        return true;
    }

    public boolean canTest() {
        System.out.println("canTest() called");
        return true;
    }

    public boolean canCreateObject(AdminCreateObject req)
    {
        return true;
    }
    public boolean canFindObject(AdminFindObject req)
    {
        return true;
    }
    public boolean canDeleteObject(AdminDeleteObject req)
    {
        return true;
    }
    public boolean canUpdateObject(AdminUpdateObject req)
    {
        return true;
    }

    public boolean canQueryObjectById(UserQueryObjectById req)
    {
        return true;
    }
    public boolean canQueryPaymentStatus (UserQueryPaymentStatus req)
    {
        return true;
    }

}
