package org.jeremyworkspace.reviewsmanager.api.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.jeremyworkspace.reviewsmanager.api.model.ListReview;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

/**
 * Represents a DataTransferObject pattern for a User creation request. This is because we want to validate data in Controller but the persisted entity (User)
 * that will contains a hashed password must no be validated. Thus, this class only represent what we received from a POST request.
 */
@Data
public class UserDto {

    @Column(nullable=false, unique=true)
    @NotEmpty(message = "{validation.username.notEmpty}")
    @Size(min = 2, max = 25, message = "{validation.username.size}")
    @Pattern(regexp = "\\w*[a-z]\\w*", flags = Pattern.Flag.CASE_INSENSITIVE, message = "{validation.username.format}")
    private String username;

    @Column(name="user_password", nullable=false)
    @NotEmpty(message = "{validation.password.notEmpty}")
    @Size(min=8, message = "{validation.password.size}")
    @Pattern(regexp = "(?=\\w*[0-9])(?=\\w*[a-z]).*", flags = Pattern.Flag.CASE_INSENSITIVE, message = "{validation.password.format}")
    private String password;

}
