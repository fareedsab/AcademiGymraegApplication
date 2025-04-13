package com.WebBased.AcademiGymraeg.Models;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "testResult")
@Getter
@Setter
@Data
public class TestResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private int userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "id", insertable = false, updatable = false)
    private Users users;

    @Column
    private int testScore;

    @Column
    private int isActive;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "completed_at")
    private Date completedAt;

    @Column
    private int isCompleted;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "resultId", cascade = CascadeType.ALL)
    private Set<TestResultDetails> testResultDetails;




}
