package com.WebBased.AcademiGymraeg.Models;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "testResultDetails")
@Data
public class TestResultDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String questionText;

    @Column
    private String systemAnswer;

    @Column
    private String userAnswer;

    @Column
    private boolean isCorrect;

    @Column
    private boolean isActive;


    @Column
    private int nounId;

    @Column
    private int questionTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nounId", referencedColumnName = "id", insertable = false, updatable = false)
    private Nouns nouns;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "questionTypeId", referencedColumnName = "id", insertable = false, updatable = false)
    private QuestionType questionType;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "completed_at")
    private Date completedAt;

    @Column
    private int resultId;


}
