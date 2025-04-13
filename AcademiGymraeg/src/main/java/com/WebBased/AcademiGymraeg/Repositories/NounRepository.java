package com.WebBased.AcademiGymraeg.Repositories;

import com.WebBased.AcademiGymraeg.Models.Nouns;
import com.WebBased.AcademiGymraeg.Models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NounRepository extends JpaRepository<Nouns,Integer> {
    List<Nouns> getNounsByIsActive(boolean isActive);
    boolean existsByWelshNoun(String welshNoun);
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END " +
            "FROM Nouns u WHERE u.welshNoun = :welshNoun AND u.id <> :id")
    boolean existsByWelshNounAndNotId(@Param("welshNoun") String welshNoun, @Param("id") Integer id);
}
