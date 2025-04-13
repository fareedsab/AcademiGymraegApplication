package com.WebBased.AcademiGymraeg.pojos.nouns;

import lombok.Data;

@Data
public class NounsPOJO {
    private int id;
    private String welshNoun;
    private String englishNoun;
    private boolean isActive;
    private int updatedBy;
    private int createdBy;
    private int nounGenderId;
}
