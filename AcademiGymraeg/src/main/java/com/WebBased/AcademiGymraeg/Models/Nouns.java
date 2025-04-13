package com.WebBased.AcademiGymraeg.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "nouns")
@Entity
@Data
public class Nouns {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "welsh_noun")
    private String welshNoun;

    @Column(name = "english_noun")
    private String englishNoun;

    @Column(name = "isActive")
    private boolean isActive;

    @Column(name = "updated_by")
    private int updatedBy;

    @Column(name = "created_by")
    private int createdBy;

    @Column
    private int nounGenderId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nounGenderId", referencedColumnName = "id", insertable = false, updatable = false)
    private NounGender nounGender;

    @JsonProperty("isActive")
    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
