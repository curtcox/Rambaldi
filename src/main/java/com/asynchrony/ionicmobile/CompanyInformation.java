package com.asynchrony.ionicmobile;

import java.util.ArrayList;
import java.util.List;

public class CompanyInformation {

    private final List<User> users = new ArrayList<>();

    public User[] users() {
        return users.toArray(new User[0]);
    }
}
