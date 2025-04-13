package com.WebBased.AcademiGymraeg.Services;

import com.WebBased.AcademiGymraeg.Models.NounGender;
import com.WebBased.AcademiGymraeg.Models.Nouns;
import com.WebBased.AcademiGymraeg.Models.Users;
import com.WebBased.AcademiGymraeg.Repositories.NounGenderRepository;
import com.WebBased.AcademiGymraeg.Repositories.NounRepository;
import com.WebBased.AcademiGymraeg.RestController.NounController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NounServiceImpl implements NounService {
    @Autowired
    NounRepository nounRepository;

    @Autowired
    NounGenderRepository nounGenderRepository;

    private static Logger LOGGER = LogManager.getLogger(NounServiceImpl.class);
    @Override
    public List<NounGender> getAllNounGenders() {
        return nounGenderRepository.findAll();
    }

    @Override
    public List<Nouns> getAllActiveNouns() {
        return nounRepository.getNounsByIsActive(true);
    }

    @Override
    public Nouns getNounById(Integer id) {
        return nounRepository.findById(id).orElse(null);
    }

    @Override
    public Nouns saveNoun(Nouns nouns) {
        if (nouns.getId() == 0) {
            if (nounRepository.existsByWelshNoun(nouns.getWelshNoun())) {
                throw new DuplicateKeyException("Welsh Noun already exists");
            }

        }

        // For existing users
        else {
            if (nounRepository.existsByWelshNounAndNotId(nouns.getEnglishNoun(), nouns.getId())) {
                throw new DuplicateKeyException("Welsh Noun already exists ");
            }
        }

        Nouns savedUser = nounRepository.save(nouns);
        return savedUser;
    }
}
