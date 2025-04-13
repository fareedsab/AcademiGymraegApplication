package com.WebBased.AcademiGymraeg.Models;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "userRole")
@Data
@Getter
@Setter
public class UserRole {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String roleName;

    @Column
    private boolean isActive;


}
