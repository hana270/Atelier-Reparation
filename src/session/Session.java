package session;

import gestionUtilisateurs.User;

public class Session {
    private static Session instance;
    private User user;

    private Session() {
        // Private constructor to prevent instantiation
    }

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void clearSession() {
        user = null;
    }

    public int getUserId() {
        return user != null ? user.getId() : -1;
    }
}
