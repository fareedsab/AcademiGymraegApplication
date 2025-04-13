package com.WebBased.AcademiGymraeg.Repositories;


import com.WebBased.AcademiGymraeg.Models.NounGender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NounGenderRepository extends JpaRepository<NounGender,Integer> {
}
