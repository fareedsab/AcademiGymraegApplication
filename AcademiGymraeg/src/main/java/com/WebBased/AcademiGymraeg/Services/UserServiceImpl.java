package com.WebBased.AcademiGymraeg.Services;

import com.WebBased.AcademiGymraeg.Models.UserRole;
import com.WebBased.AcademiGymraeg.Models.Users;
import com.WebBased.AcademiGymraeg.Repositories.UserRepository;
import com.WebBased.AcademiGymraeg.Repositories.UserRoleRepository;
import com.WebBased.AcademiGymraeg.pojos.users.UserLoginPOJO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.dao.DuplicateKeyException;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    UserRepository userRepository;

    @Autowired
    UserRoleRepository userRoleRepository;


    @Override
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean validateUserByUsernameAndPassword(UserLoginPOJO userLoginPOJO) {
        Users user = userRepository.findByUsername(userLoginPOJO.getUserName()).orElse(null);
        boolean isValid = false;
        if(user != null)
        {
            if(user.getPassword().equals(userLoginPOJO.getPassword()))
            {
                isValid = true;
            }
        }

        return isValid;
    }

    @Override
    public Users getUserbyUserName(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public List<UserRole> getAllUserRoles() {
        return userRoleRepository.findAll();
    }

    @Override
    public Users saveUser(Users user) {
        // For new users
        if (user.getId() == 0) {
            if (userRepository.existsByUsername(user.getUsername())) {
                throw new DuplicateKeyException("Username already exists");
            }
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new DuplicateKeyException("Email already exists");
            }
            user.setPassword(user.getPassword());
        }

        // For existing users
        else {
            if (userRepository.existsByEmailAndNotId(user.getEmail(), user.getId())) {
                throw new DuplicateKeyException("Email already exists for another user");
            }
        }

        Users savedUser = userRepository.save(user);
        return savedUser;
    }

    @Override
    public Users getUserById(int userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public int inActiveUserByUserId(int userId) {
        int saved = 0;
        Users users = userRepository.findById(userId).orElse(null);
        if(users != null)
        {
             users.setActive(false);
             Users savedUser = userRepository.save(users);
             if(savedUser != null)
             {
                 saved = savedUser.getId();
             }

        }
        return saved;
    }

    @Override
    public List<Users> getAllActiveUsers() {
        return userRepository.getUsersByIsActive(true);
    }


}
