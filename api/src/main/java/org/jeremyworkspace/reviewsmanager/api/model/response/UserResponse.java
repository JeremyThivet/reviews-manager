package org.jeremyworkspace.reviewsmanager.api.model.response;

import lombok.Data;
import org.jeremyworkspace.reviewsmanager.api.model.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

/**
 * This class stands for the response we will provide the client as a REST API. We won't send password field to the client.
 */
@Data
public class UserResponse
{
    private Long id;
    private String username;
    private short isAdmin;
    private Date subscriptionDate;
    private Date lastConnection;

    public UserResponse(User u){
        this.id = u.getId();
        this.username = u.getUsername();
        this.isAdmin = u.getIsAdmin();
        this.subscriptionDate = u.getSubscriptionDate();
        this.lastConnection = u.getLastConnection();
    }
}
