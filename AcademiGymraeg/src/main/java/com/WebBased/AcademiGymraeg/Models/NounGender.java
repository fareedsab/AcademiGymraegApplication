package com.WebBased.AcademiGymraeg.Models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "nounGender")
@Entity
@Getter
@Setter
@Data
public class NounGender {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String genderName;

    @Column
    private int created_by;
}
