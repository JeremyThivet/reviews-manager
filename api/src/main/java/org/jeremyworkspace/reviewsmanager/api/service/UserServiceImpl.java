package org.jeremyworkspace.reviewsmanager.api.service;

import org.jeremyworkspace.reviewsmanager.api.model.User;
import org.jeremyworkspace.reviewsmanager.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> getUserById(Long id){
        return this.userRepository.findById(id);
    }

    @Override
    public Iterable<User> getUsers(){
        return this.userRepository.findAll();
    }

    /**
     * This methods retrieves user input data, validates it and ask for data persistence if all controls are ok.
     * @param user user containing user input.
     * @return created user
     */
    @Override
    public User createUser(User user){

        // Set last connection date to now.
        user.setLastConnection(new Date());
        user.setSubscriptionDate(new Date());

        // If this is the first registered user, then set it as admin.
        if(this.countUsers() == 0){
            user.setIsAdmin((short)1);
        } else {
            user.setIsAdmin((short)0);
        }

        // Dealing with password
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));

        User u = this.saveUser(user);
        return u;
    }

    @Override
    public User updateUser(User user) {
        return this.saveUser(user);
    }


    @Override
    public void deleteUserById(Long id){
        this.userRepository.deleteById(id);
    }


    @Override
    public long countUsers(){
        return this.userRepository.count();
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return this.userRepository.findFirstByUsernameIgnoreCase(username);
    }

    private User saveUser(User user){
        return this.userRepository.save(user);
    }

}
