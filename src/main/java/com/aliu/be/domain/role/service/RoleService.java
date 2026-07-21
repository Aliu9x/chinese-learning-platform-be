package com.aliu.be.domain.role.service;

import java.util.List;

import com.aliu.be.domain.role.dto.request.RoleCreateRequest;
import com.aliu.be.domain.role.dto.request.RoleUpdateRequest;
import com.aliu.be.domain.role.dto.response.RoleResponse;


public interface RoleService {
    RoleResponse create(RoleCreateRequest request);

    RoleResponse update(Long id, RoleUpdateRequest request);

    RoleResponse getById(Long id);

    List<RoleResponse> getAll();

    void delete(Long id);
}
