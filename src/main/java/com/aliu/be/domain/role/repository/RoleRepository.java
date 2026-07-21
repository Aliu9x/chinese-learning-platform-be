package com.aliu.be.domain.role.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.aliu.be.common.enums.Status;
import com.aliu.be.domain.role.entity.Role;

import java.util.*;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

    List<Role> findAllByStatusOrderByNameAsc(Status status);
}
