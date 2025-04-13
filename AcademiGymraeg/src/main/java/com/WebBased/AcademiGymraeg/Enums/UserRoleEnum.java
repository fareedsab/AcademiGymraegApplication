package com.WebBased.AcademiGymraeg.Enums;

public enum UserRoleEnum {
    ADMINISTRATOR(1),
    INSTRUCTOR(2),
    STUDENT(3);

    public Integer getUserRoleType() {
        return userRoleType;
    }

    private final Integer userRoleType;

    private UserRoleEnum(Integer type) {
        this.userRoleType = type;
    }
    public static UserRoleEnum fromUserRoleId(int userRoleId) {
        for (UserRoleEnum product : values()) {
            if (product.userRoleType == userRoleId) {
                return product;
            }
        }
        throw new IllegalArgumentException("No enum constant with userRoleId " + userRoleId);
    }
}