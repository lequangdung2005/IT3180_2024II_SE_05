package com.hust.ittnk68.cnpm.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.hust.ittnk68.cnpm.communication.*;

@Component("authz")
public class AuthorizationService {

    public boolean canStartSession(ClientMessageStartSession message) {
        // every one can /auth
        return true;
    }

    public boolean canTest() {
        System.out.println("canTest() called");
        return true;
    }

	public boolean canQueryObjectById(UserQueryObjectById req)
    {
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

    public boolean canQueryPaymentStatus (UserQueryPaymentStatus req)
    {
        return true;
    }

}
