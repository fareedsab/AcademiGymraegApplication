package com.WebBased.AcademiGymraeg.Repositories;

import com.WebBased.AcademiGymraeg.Models.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole,Integer> {
}
