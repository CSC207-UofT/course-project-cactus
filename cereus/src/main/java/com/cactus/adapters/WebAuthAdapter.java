package com.cactus.adapters;

import com.cactus.entities.User;

public class WebAuthAdapter implements AuthAdapter {
    @Override
    public User login(String username, String password) {
        return null;
    }

    @Override
    public User create(String username, String password, String name) {
        return null;
    }

    @Override
    public User getUser(long userId) {
        return null;
    }
}
