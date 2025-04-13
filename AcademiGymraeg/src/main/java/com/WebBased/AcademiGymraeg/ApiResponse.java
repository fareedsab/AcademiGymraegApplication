package com.WebBased.AcademiGymraeg;

import lombok.Data;

@Data
public class ApiResponse<T> {
    private int statusCode;
    private String statusDescription;
    private T data;

    public ApiResponse(int statusCode, String statusDescription, T data) {
        this.statusCode = statusCode;
        this.statusDescription = statusDescription;
        this.data = data;
    }
}
