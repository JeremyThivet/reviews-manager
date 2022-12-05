package org.jeremyworkspace.reviewsmanager.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.jeremyworkspace.reviewsmanager.api.model.dto.UserDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name="appuser")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long id;

    @Column(nullable=false, unique=true)
    private String username;

    @Column(name="user_password", nullable=false)
    private String password;

    @Column(name="is_administrator", nullable=false)
    private short isAdmin;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="created_at", nullable=false)
    private Date subscriptionDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="last_connection", nullable=false)
    private Date lastConnection;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("owner")
    private Set<ListReview> listReviews;

    @Transient
    private final Set<? extends GrantedAuthority> grantedAuthorities;
    @Transient
    private final boolean isAccountNonExpired;
    @Transient
    private final boolean isAccountNonLocked;
    @Transient
    private final boolean isCredentialsNonExpired;
    @Transient
    private final boolean isEnabled;

    @Transient
    private final boolean stayConnected;

    public User(){
        this.grantedAuthorities = null;
        this.isAccountNonLocked = true;
        this.isCredentialsNonExpired = true;
        this.isAccountNonExpired = true;
        this.isEnabled = true;
        this.stayConnected = false;
    }

    public User(UserDto u){
        this.username = u.getUsername();
        this.password = u.getPassword();
        this.grantedAuthorities = null;
        this.isAccountNonLocked = true;
        this.isCredentialsNonExpired = true;
        this.isAccountNonExpired = true;
        this.isEnabled = true;
        this.stayConnected = false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.grantedAuthorities;
    }
}
