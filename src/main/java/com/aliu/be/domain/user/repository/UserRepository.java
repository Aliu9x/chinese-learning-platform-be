package com.aliu.be.domain.user.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import com.aliu.be.domain.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByUsernameIgnoreCase(String username);

    Optional<User> findByEmailIgnoreCase(String email);

    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByEmailIgnoreCase(String email);

    @Query("""
                select distinct u
                from User u
                where lower(u.username)=lower(:login)
                   or lower(u.email)=lower(:login)
            """)
    Optional<User> findByUsernameOrEmailWithAuthorities(@Param("login") String login);

    @EntityGraph(attributePaths = { "roles" })
    @Query("select distinct u from User u where u.id=:id")
    Optional<User> findByIdWithRoles(@Param("id") Long id);
}
