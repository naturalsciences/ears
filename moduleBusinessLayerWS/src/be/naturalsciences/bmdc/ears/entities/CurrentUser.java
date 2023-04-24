/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.entities;

/**
 *
 * @author thomas
 */
public class CurrentUser implements CurrentSingleton<User> {

    private User currentUser;

    private static final CurrentUser instance = new CurrentUser();

    private CurrentUser() {
    }

    public User getConcept() {
        return currentUser;
    }

    public static CurrentUser getInstance(User currentUser) {
        if (currentUser == null) {
            instance.currentUser = null; //there is no current cruise
        }
        instance.currentUser = currentUser;
        return instance;
    }

}
