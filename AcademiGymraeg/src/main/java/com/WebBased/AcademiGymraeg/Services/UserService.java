package com.WebBased.AcademiGymraeg.Services;



import com.WebBased.AcademiGymraeg.Models.UserRole;
import com.WebBased.AcademiGymraeg.Models.Users;
import com.WebBased.AcademiGymraeg.pojos.users.UserLoginPOJO;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public List<Users> getAllUsers();
    public boolean validateUserByUsernameAndPassword(UserLoginPOJO userLoginPOJO);
    public Users getUserbyUserName(String username);
    public List<UserRole> getAllUserRoles();
    Users saveUser(Users user);
    Users getUserById(int userId);
    int inActiveUserByUserId(int userId);
    List<Users> getAllActiveUsers();

}
