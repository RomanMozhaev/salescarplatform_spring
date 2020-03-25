package ru.job4j.persistent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.job4j.models.User;

/**
 * The repository for User model.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("from User u where u.name like :userName and u.password like :userPassword")
    User isCredential(@Param("userName") String userName, @Param(("userPassword")) String userPassword);
}
