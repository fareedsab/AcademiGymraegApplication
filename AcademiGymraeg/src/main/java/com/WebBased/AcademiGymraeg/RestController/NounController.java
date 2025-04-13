package com.WebBased.AcademiGymraeg.RestController;

import com.WebBased.AcademiGymraeg.Models.NounGender;
import com.WebBased.AcademiGymraeg.Models.Nouns;
import com.WebBased.AcademiGymraeg.Services.NounService;
import com.WebBased.AcademiGymraeg.pojos.nouns.NounsPOJO;
import com.WebBased.AcademiGymraeg.utils.Messages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nouns")
public class NounController {

    private static Logger LOGGER = LogManager.getLogger(NounController.class);

    @Autowired
    NounService nounService;

    @GetMapping(path = "/nounGenders")
    public ResponseEntity getAllNounGenders() {
        try {
            List<NounGender> nounGenders = nounService.getAllNounGenders();

            if (nounGenders.isEmpty()) {
                System.out.println("No noun genders found");
                return ResponseEntity.ok().body(new Messages<>()
                        .setData(nounGenders)
                        .setStatus(HttpStatus.OK.value())
                        .setCode(String.valueOf(HttpStatus.OK))
                        .setMessage("No noun genders found"));
            } else {
                System.out.println("Found {} noun genders"+ nounGenders.size());
                return ResponseEntity.ok().body(new Messages<>()
                        .setData(nounGenders)
                        .setStatus(HttpStatus.OK.value())
                        .setCode(String.valueOf(HttpStatus.OK))
                        .setMessage("Noun genders retrieved successfully"));
            }
        } catch (Exception e) {
            System.out.println("Error in {}: {}"+ e.getMessage()+ e);
            return ResponseEntity.internalServerError()
                    .body(new Messages<>()
                            .setMessage("Internal server error")
                            .setData(null)
                            .setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)));
        }
    }
    @PostMapping(path = "/addUpdateNoun")
    public ResponseEntity addUpdateNoun(
            @RequestBody NounsPOJO nounDTO) {

        try {
            if (nounDTO.getWelshNoun() != null && !nounDTO.getWelshNoun().isEmpty()) {

                // Check for existing noun

                Nouns noun = nounService.getNounById(nounDTO.getId());

                if (noun != null) {
                    System.out.println("NOUN FOUND WITH ID: [{}]"+ nounDTO.getId());
                    // Update existing noun
                    noun.setWelshNoun(nounDTO.getWelshNoun());
                    noun.setEnglishNoun(nounDTO.getEnglishNoun());
                    noun.setNounGenderId(nounDTO.getNounGenderId());
                    noun.setIsActive(nounDTO.isActive());
                    noun.setUpdatedBy(nounDTO.getUpdatedBy());
                } else {
                    System.out.println("NOUN NOT FOUND, CREATING NEW");
                    // Create new noun
                    noun = new Nouns();
                    noun.setWelshNoun(nounDTO.getWelshNoun());
                    noun.setEnglishNoun(nounDTO.getEnglishNoun());
                    noun.setNounGenderId(nounDTO.getNounGenderId());
                    noun.setIsActive(nounDTO.isActive());
                    noun.setCreatedBy(nounDTO.getCreatedBy());
                    noun.setUpdatedBy(nounDTO.getCreatedBy());
                }

                // Save and return response
                Nouns savedNoun = nounService.saveNoun(noun);
                return ResponseEntity.ok()
                        .body(new Messages<>()
                                .setMessage("Noun details processed successfully")
                                .setData(savedNoun)
                                .setStatus(HttpStatus.OK.value())
                                .setCode(String.valueOf(HttpStatus.OK)));
            } else {
                return ResponseEntity.badRequest()
                        .body(new Messages<>()
                                .setMessage("Welsh noun is required")
                                .setData(null)
                                .setStatus(HttpStatus.BAD_REQUEST.value())
                                .setCode(String.valueOf(HttpStatus.BAD_REQUEST)));
            }
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new Messages<>()
                            .setMessage(e.getMessage())
                            .setData(null)
                            .setStatus(HttpStatus.CONFLICT.value())
                            .setCode(String.valueOf(HttpStatus.CONFLICT)));
        } catch (Exception e) {
            System.out.println("Exception occurred: {}"+e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new Messages<>()
                            .setMessage("Internal Server Error")
                            .setData(null)
                            .setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)));
        } finally {
            System.out.println("---- Add/Update Noun [END] ----");
        }
    }

    @GetMapping(path = "/allActiveNouns")
    public ResponseEntity getAllActiveNouns() {
        try {
            List<Nouns> nouns = nounService.getAllActiveNouns();

            if (nouns.isEmpty()) {
                System.out.println("No active nouns found");
                return ResponseEntity.ok().body(new Messages<>()
                        .setData(nouns)
                        .setStatus(HttpStatus.OK.value())
                        .setCode(String.valueOf(HttpStatus.OK))
                        .setMessage("No active nouns found"));
            } else {
                System.out.println("Found {} active nouns"+ nouns.size());
                return ResponseEntity.ok().body(new Messages<>()
                        .setData(nouns)
                        .setStatus(HttpStatus.OK.value())
                        .setCode(String.valueOf(HttpStatus.OK))
                        .setMessage("Active nouns retrieved successfully"));
            }
        } catch (Exception e) {
            System.out.println("Error in {}: {}"+ e.getMessage()+ e);
            return ResponseEntity.internalServerError()
                    .body(new Messages<>()
                            .setMessage("Internal server error")
                            .setData(null)
                            .setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)));
        }
    }

}
