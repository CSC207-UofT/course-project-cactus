package com.cactus.adapters;

import com.cactus.data.EntityRepository;
import com.cactus.entities.User;
import java.util.Collection;
import java.util.HashMap;

/***
 * Represents ClassAuthAdapter which implements AuthAdapter interface.
 */

public class ClassAuthAdapter implements AuthAdapter{

    private EntityRepository repository;

    /***
     * Creates a new ClassAuthAdapter with the given repository.
     * @param repository given repository
     */

    public ClassAuthAdapter(EntityRepository repository){

        this.repository = repository;
    }


    @Override
    public Response login(String username, String password) {
        Collection<User> users = this.repository.getAllUsers();
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                HashMap<String, String> credential = new HashMap<String, String>();
                credential.put("userid", username);
                credential.put("name", user.getName());
                return new Response(Response.Status.OK, credential);
            }
        }
        return new Response(Response.Status.BAD_REQUEST, null);
    }

    @Override
    public Response create(String username, String password, String name) {
            Collection<User> users = this.repository.getAllUsers();
            for (User user : users) {
                if (user.getUsername().equals(username)) {
                    return new Response(Response.Status.BAD_REQUEST, null);
                }
            }
            User newUser = new User(name, username, password);
            this.repository.saveUser(newUser);
            HashMap<String, String> credential = new HashMap<String, String>();
            credential.put("userid", username);
            credential.put("name", name);
            return new Response(Response.Status.OK, credential);

    }
}
