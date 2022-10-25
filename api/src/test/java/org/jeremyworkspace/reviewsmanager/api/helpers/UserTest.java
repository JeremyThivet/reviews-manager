package org.jeremyworkspace.reviewsmanager.api.helpers;

import lombok.Data;

@Data
public class UserTest {

    private Integer id;
    private String username;
    private String password;
    private String accesToken;
}
