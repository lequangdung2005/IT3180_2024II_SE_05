package com.hust.ittnk68.cnpm.exception;

public class UserCreateSecondSession extends Exception {
    public UserCreateSecondSession (String username) {
        super("Tai khoan " + username + " tao session thu hai trong khi chua het han.");
    }
}
