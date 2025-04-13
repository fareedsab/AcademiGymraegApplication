package com.WebBased.AcademiGymraeg.pojos.users;

import lombok.Data;

@Data
public class UserDetailsPOJO {
    private String username;
    private String fullname;
    private String email;
    private String password;
    private boolean active;
    private int roleId;
}
