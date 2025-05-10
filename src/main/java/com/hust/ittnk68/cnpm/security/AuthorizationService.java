package com.hust.ittnk68.cnpm.security;

import com.hust.ittnk68.cnpm.auth.JwtUtil;
import com.hust.ittnk68.cnpm.communication.*;
import com.hust.ittnk68.cnpm.service.AuthController;
import com.hust.ittnk68.cnpm.service.ApiController;
import com.hust.ittnk68.cnpm.type.AccountType;
import com.hust.ittnk68.cnpm.model.Account;
import com.hust.ittnk68.cnpm.model.Expense;
import com.hust.ittnk68.cnpm.model.PaymentStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("authz")
public class AuthorizationService {

    @Autowired
    private AuthController authController;
    @Autowired
    private ApiController apiController;

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

    private boolean checkAdminPrivilege (ClientMessageBase req)
    {
        Account account = authController.getAccountByUsername (req.getUsername ());
        return account.getAccountType().equals (AccountType.ROOT)
            || account.getAccountType().equals (AccountType.ADMIN);
    }

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
        return checkToken (req) && checkAdminPrivilege (req);
    }
    public boolean canFindObject(AdminFindObject req)
    {
        return checkToken (req) && checkAdminPrivilege (req);
    }
    public boolean canDeleteObject(AdminDeleteObject req)
    {
        return checkToken (req) && checkAdminPrivilege (req);
    }
    public boolean canUpdateObject(AdminUpdateObject req)
    {
        return checkToken (req) && checkAdminPrivilege (req);
    }

    public boolean canQueryObjectById(UserQueryObjectById req)
    {
        // i'm to lazy to implement this
        return checkToken (req);
    }
    public boolean canQueryPaymentStatus (UserQueryPaymentStatus req)
    {
        return checkToken (req);
    }

    public boolean canPay (PaymentRequest req, PaymentStatus ps, Expense ex)
    {
        return true;
    }

}
