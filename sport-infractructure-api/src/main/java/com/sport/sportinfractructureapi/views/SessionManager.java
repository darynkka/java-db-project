package com.sport.sportinfractructureapi.views;
import com.sport.sportinfractructureapi.model.Key;
import org.springframework.stereotype.Component;

@Component
public class SessionManager {
    private Key authenticatedUser;
    private boolean greetingShown = false;

    public void setAuthenticatedUser(Key user) {
        this.authenticatedUser = user;
    }

    public Key getAuthenticatedUser() {
        return authenticatedUser;
    }

    public void clearAuthenticatedUser() {
        this.greetingShown = false;
    }
    public boolean isGreetingShown() {
        return greetingShown;
    }

    public void setGreetingShown(boolean greetingShown) {
        this.greetingShown = greetingShown;
    }
}

