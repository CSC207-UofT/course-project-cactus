package com.cactus.adapters;

import com.cactus.entities.User;

import javax.inject.Inject;

public class WebAuthAdapter implements AuthAdapter {

    @Inject
    public WebAuthAdapter() {}

    @Override
    public User login(String username, String password) {
        return new User("Name", username, password);
    }

    @Override
    public User create(String username, String password, String name) {
        return new User(name, username, password);
    }

    @Override
    public User getUser(long userId) {
        return new User("Caleb", "calebxcaleb", "password");
    }
}
