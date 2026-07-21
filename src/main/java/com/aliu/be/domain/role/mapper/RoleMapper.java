package com.aliu.be.domain.role.mapper;
import org.springframework.stereotype.Component;

import com.aliu.be.domain.role.dto.request.RoleCreateRequest;
import com.aliu.be.domain.role.dto.request.RoleUpdateRequest;
import com.aliu.be.domain.role.dto.response.RoleResponse;
import com.aliu.be.domain.role.entity.Role;

@Component
public class RoleMapper {
    public Role toEntity(RoleCreateRequest r) {
        Role e = new Role();
        e.setName(r.name());
        e.setDescription(r.description());
        return e;
    }

    public void update(RoleUpdateRequest r, Role e) {
        e.setName(r.name());
        e.setDescription(r.description());
        e.setStatus(r.status());
    }

    public RoleResponse toResponse(Role e) {
        return new RoleResponse(e.getId(), e.getName(), e.getDescription(), e.getStatus(),
                e.getCreatedAt(), e.getUpdatedAt());
    }
}
