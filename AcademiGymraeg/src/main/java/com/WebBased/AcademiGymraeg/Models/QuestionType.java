package com.WebBased.AcademiGymraeg.Models;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "questionType")
@Data
public class QuestionType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String questionTypeName;

    @Column
    private String questionTypeValue;

    @Column
    private boolean isActive;

}
