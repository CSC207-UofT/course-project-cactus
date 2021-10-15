package com.cactus.adapters;

public interface AuthAdapter {

    /**
     * Returns a Response object with the results of a login operation done using the
     * provided username and password.
     *
     * If the login was successful, the Response will have code 200 (OK), and a
     * payload with the following keys:
     * - userid
     * - name
     *
     * If the login was unsuccessful, the Response will have code 400 (BAD_REQUEST)
     * and a null payload.
     *
     * @param username a String containing the username of the user to be logged in
     * @param password a String containing the password to validate
     * @return         a Response to the login operation
     *
     * @see Response
     */
    public Response login(String username, String password);

    /**
     * Returns a Response object with the results of a create user operation done
     * using the provided username, password, and name.
     *
     * If the user was created successfully, the Response will have code 200 (OK),
     * and a payload with the following keys:
     * - userid
     * - name
     *
     * If the user creation was unsuccessful (i.e. username already exists), the
     * Response will have code 400 (BAD_REQUEST) and a null payload.
     *
     * @param username a String containing the username of the new user
     * @param password a String containing the password of the new user
     * @param name     a String containing the name of the user
     * @return         a Response to the create user operation
     *
     * @see Response
     */
    public Response create(String username, String password, String name);
}
