package com.WebBased.AcademiGymraeg.Repositories;

import com.WebBased.AcademiGymraeg.Models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users,Integer> {
    Optional<Users> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END " +
            "FROM Users u WHERE u.email = :email AND u.id <> :id")
    boolean existsByEmailAndNotId(@Param("email") String email, @Param("id") Integer id);

    List<Users> getUsersByIsActive(boolean isActive);


}
