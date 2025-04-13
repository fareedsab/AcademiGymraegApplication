package com.WebBased.AcademiGymraeg.Services;

import com.WebBased.AcademiGymraeg.Models.NounGender;
import com.WebBased.AcademiGymraeg.Models.Nouns;

import java.util.List;

public interface NounService {
    List<NounGender> getAllNounGenders();
    List<Nouns> getAllActiveNouns();
    Nouns getNounById(Integer id);
    Nouns saveNoun(Nouns nouns);
}
