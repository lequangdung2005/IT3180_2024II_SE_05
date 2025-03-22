package com.hust.ittnk68.cnpm.communication;

import com.hust.ittnk68.cnpm.model.Family;

public class AdminCreateFamily extends ClientMessageBase {

    Family family;

    public AdminCreateFamily (String token, Family family) {
        super (token);
        this.family = family;
    }
 
    public Family getFamily () {
        return family;
    }
    public void setAccount (Family account) {
        this.family = account;
    }

}
