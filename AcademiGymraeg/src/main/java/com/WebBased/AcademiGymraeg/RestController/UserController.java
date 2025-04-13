package com.WebBased.AcademiGymraeg.RestController;

import com.WebBased.AcademiGymraeg.ApiResponse;
import com.WebBased.AcademiGymraeg.Models.UserRole;
import com.WebBased.AcademiGymraeg.Models.Users;
import com.WebBased.AcademiGymraeg.Services.UserService;
import com.WebBased.AcademiGymraeg.pojos.users.UserDetailsPOJO;
import com.WebBased.AcademiGymraeg.pojos.users.UserLoginPOJO;
import com.WebBased.AcademiGymraeg.utils.Messages;
import org.apache.catalina.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;
    private static Logger LOGGER = LogManager.getLogger(UserController.class);
//    @GetMapping(path = "/allUsers")
//    public ResponseEntity<ApiResponse<List<Users>>> getUsers() {
//        List<Users> users = userService.getAllUsers();
//        ApiResponse<List<Users>> response;
//        if (users.isEmpty()) {
//            response = new ApiResponse<>(200, "No Users Found", new ArrayList<>());
//        } else {
//            response = new ApiResponse<>(200, "Users Found", users);
//        }
//        return ResponseEntity.ok(response);
//    }
    @PostMapping(value = "/validateUser",produces = "application/json")
    public ResponseEntity<ApiResponse<Users>> login(@RequestBody UserLoginPOJO loginRequest) {

        boolean isValid = userService.validateUserByUsernameAndPassword(loginRequest);
        ApiResponse<Users> response;

        if (isValid) {

            Users user = userService.getUserbyUserName(loginRequest.getUserName());
            user.setPassword(null);
            if(user.isActive()) {
                response = new ApiResponse<>(200, "Login Successful", user);
                return ResponseEntity.ok(response);
            }
            else
            {
                response = new ApiResponse<>(401, "User InActive", null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } else {
            response = new ApiResponse<>(401, "Invalid Credentials", null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
//    @GetMapping(path = "/userRoles")
//    public ResponseEntity<ApiResponse<List<UserRole>>> getAllUserRoles() {
//        List<UserRole> users = userService.getAllUserRoles();
//        ApiResponse<List<UserRole>> response;
//        if (users.isEmpty()) {
//            response = new ApiResponse<>(200, "No Users Found", new ArrayList<>());
//        } else {
//            response = new ApiResponse<>(200, "Users Found", users);
//        }
//        return ResponseEntity.ok(response);
//    }
    @PostMapping(path = "/addUpdateUserDetails")
    public ResponseEntity addUpdateUserDetails(
            @RequestBody UserDetailsPOJO userDetailsDTO) {

        try {
            if (userDetailsDTO.getUsername() != null && !userDetailsDTO.getUsername().isEmpty()) {

                // Check for existing user
                Users user = userService.getUserbyUserName(userDetailsDTO.getUsername());

                if (user != null) {
                    System.out.println("USER FOUND WITH USERNAME: [{}]"+ userDetailsDTO.getUsername());
                    // Update existing user
                    user.setFullname(userDetailsDTO.getFullname() != null ?
                            userDetailsDTO.getFullname() : user.getFullname());
                    user.setEmail(userDetailsDTO.getEmail() != null ?
                            userDetailsDTO.getEmail() : user.getEmail());
                    user.setPassword(userDetailsDTO.getPassword() != null ?
                            userDetailsDTO.getPassword() : user.getPassword());
                    user.setRoleId(userDetailsDTO.getRoleId());
                    user.setActive(userDetailsDTO.isActive());
                } else {
                    System.out.println("USER NOT FOUND WITH USERNAME: [{}]"+ userDetailsDTO.getUsername());
                    // Create new user
                    user = new Users();
                    user.setUsername(userDetailsDTO.getUsername());
                    user.setFullname(userDetailsDTO.getFullname());
                    user.setEmail(userDetailsDTO.getEmail());
                    user.setPassword(userDetailsDTO.getPassword());
                    user.setRoleId(userDetailsDTO.getRoleId());// Will be encoded in service
                    user.setActive(true);
                }

                // Save and return response
                Users savedUser = userService.saveUser(user);
                return ResponseEntity.ok()
                        .body(new Messages<>()
                                .setMessage("User details processed successfully")
                                .setData(savedUser)
                                .setStatus(HttpStatus.OK.value())
                                .setCode(String.valueOf(HttpStatus.OK)));
            } else {
                return ResponseEntity.badRequest()
                        .body(new Messages<>()
                                .setMessage("Username is required")
                                .setData(null)
                                .setStatus(HttpStatus.BAD_REQUEST.value())
                                .setCode(String.valueOf(HttpStatus.BAD_REQUEST)));
            }
        } catch (DuplicateKeyException e) {

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new Messages<>()
                            .setMessage(e.getMessage())
                            .setData(null)
                            .setStatus(HttpStatus.CONFLICT.value())
                            .setCode(String.valueOf(HttpStatus.CONFLICT)));
        } catch (Exception e) {
            System.out.println("Exception occurred: {}"+e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new Messages<>()
                            .setMessage("Internal Server Error")
                            .setData(null)
                            .setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)));
        } finally {
            System.out.println("---- Add/Update User Details [END] ----");
        }
    }
    @GetMapping(path = "/userRoles")
    public ResponseEntity getAllUserRoles() {

        try {
            List<UserRole> userRoles = userService.getAllUserRoles();


            if (userRoles.isEmpty()) {
                LOGGER.info("No user roles found");
               return ResponseEntity.ok().body(new Messages<>()
                        .setData(userRoles)
                        .setStatus(HttpStatus.OK.value())
                        .setCode(String.valueOf(HttpStatus.OK))
                        .setMessage("No user roles found"));

            } else {
                LOGGER.info("Found {} user roles", userRoles.size());
                return  ResponseEntity.ok().body(new Messages<>()
                        .setData(userRoles)
                        .setStatus(HttpStatus.OK.value())
                        .setCode(String.valueOf(HttpStatus.OK))
                        .setMessage("User roles retrieved successfully"));
            }



        } catch (Exception e) {
            LOGGER.error("Error in {}: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(new Messages<>()
                            .setMessage("Internal server error")
                            .setData(null)
                            .setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)));
        }
    }

    @GetMapping(path = "/allUsers")
    public ResponseEntity getUsers() {

        try {
            List<Users> users = userService.getAllUsers();


            if (users.isEmpty()) {
                LOGGER.info("No user  found");
                return ResponseEntity.ok().body(new Messages<>()
                        .setData(users)
                        .setStatus(HttpStatus.OK.value())
                        .setCode(String.valueOf(HttpStatus.OK))
                        .setMessage("No user  found"));

            } else {
                LOGGER.info("Found {} users", users.size());
                return  ResponseEntity.ok().body(new Messages<>()
                        .setData(users)
                        .setStatus(HttpStatus.OK.value())
                        .setCode(String.valueOf(HttpStatus.OK))
                        .setMessage("User  retrieved successfully"));
            }



        } catch (Exception e) {
            LOGGER.error("Error in {}: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(new Messages<>()
                            .setMessage("Internal server error")
                            .setData(null)
                            .setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)));
        }
    }
    @GetMapping(path = "/allActiveUsers")
    public ResponseEntity getAllActiveUsers() {

        try {
            List<Users> users = userService.getAllActiveUsers();


            if (users.isEmpty()) {
                LOGGER.info("No user  found");
                return ResponseEntity.ok().body(new Messages<>()
                        .setData(users)
                        .setStatus(HttpStatus.OK.value())
                        .setCode(String.valueOf(HttpStatus.OK))
                        .setMessage("No user  found"));

            } else {
                LOGGER.info("Found {} users", users.size());
                return  ResponseEntity.ok().body(new Messages<>()
                        .setData(users)
                        .setStatus(HttpStatus.OK.value())
                        .setCode(String.valueOf(HttpStatus.OK))
                        .setMessage("User  retrieved successfully"));
            }



        } catch (Exception e) {
            LOGGER.error("Error in {}: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(new Messages<>()
                            .setMessage("Internal server error")
                            .setData(null)
                            .setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)));
        }
    }

    @DeleteMapping(path = "/deleteUser/{userId}")
    public ResponseEntity deleteUser(
            @PathVariable Integer userId) {

        try {
            if (userId != null && userId > 0) {
                // Check if user exists
                Users user = userService.getUserById(userId);

                if (user == null) {
                    System.out.println("USER NOT FOUND WITH ID: [{}]"+ userId);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new Messages<>()
                                    .setMessage("User not found with id: " + userId)
                                    .setData(null)
                                    .setStatus(HttpStatus.NOT_FOUND.value())
                                    .setCode(String.valueOf(HttpStatus.NOT_FOUND)));
                }

                // Delete the user
                int saved = userService.inActiveUserByUserId(userId);

                return ResponseEntity.ok()
                        .body(new Messages<>()
                                .setMessage("User deleted successfully")
                                .setData(null)
                                .setStatus(HttpStatus.OK.value())
                                .setCode(String.valueOf(HttpStatus.OK)));
            } else {
                return ResponseEntity.badRequest()
                        .body(new Messages<>()
                                .setMessage("User ID is required")
                                .setData(null)
                                .setStatus(HttpStatus.BAD_REQUEST.value())
                                .setCode(String.valueOf(HttpStatus.BAD_REQUEST)));
            }
        } catch (Exception e) {
            System.out.println("Exception occurred: {}"+e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new Messages<>()
                            .setMessage("Internal Server Error")
                            .setData(null)
                            .setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)));
        } finally {
            System.out.println("---- Delete User [END] ----");
        }
    }
}

