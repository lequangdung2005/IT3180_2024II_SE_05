package com.hust.ittnk68.cnpm.communication;

import com.hust.ittnk68.cnpm.model.Person;

public class AdminCreatePerson extends ClientMessageBase {

    Person person;

    public AdminCreatePerson (String token, Person person) {
        super (token);
        this.person = person;
    }
 
    public Person getPerson () {
        return person;
    }
    public void setAccount (Person account) {
        this.person = account;
    }

}
