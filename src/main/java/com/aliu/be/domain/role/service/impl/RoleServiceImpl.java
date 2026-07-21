package com.aliu.be.domain.role.service.impl;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aliu.be.common.enums.Status;
import com.aliu.be.domain.role.dto.request.RoleCreateRequest;
import com.aliu.be.domain.role.dto.request.RoleUpdateRequest;
import com.aliu.be.domain.role.dto.response.RoleResponse;
import com.aliu.be.domain.role.entity.Role;
import com.aliu.be.domain.role.mapper.RoleMapper;
import com.aliu.be.domain.role.repository.RoleRepository;
import com.aliu.be.domain.role.service.RoleService;
import com.aliu.be.exception.DuplicateResourceException;
import com.aliu.be.exception.ResourceNotFoundException;

import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    private final RoleRepository repo;
    private final RoleMapper mapper;

    public RoleServiceImpl(RoleRepository repo, RoleMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public RoleResponse create(RoleCreateRequest r) {
        if (repo.existsByNameIgnoreCase(r.name()))
            throw new DuplicateResourceException("Role đã tồn tại: " + r.name());
        return mapper.toResponse(repo.save(mapper.toEntity(r)));
    }

    public RoleResponse update(Long id, RoleUpdateRequest r) {
        Role e = find(id);
        repo.findByNameIgnoreCase(r.name()).filter(x -> !x.getId().equals(id)).ifPresent(x -> {
            throw new DuplicateResourceException("Role đã tồn tại: " + r.name());
        });
        mapper.update(r, e);
        return mapper.toResponse(repo.save(e));
    }

    @Transactional(readOnly = true)
    public RoleResponse getById(Long id) {
        return mapper.toResponse(find(id));
    }

    @Transactional(readOnly = true)
    public List<RoleResponse> getAll() {
        return repo.findAll().stream().map(mapper::toResponse).toList();
    }

    public void delete(Long id) {
        Role e = find(id);
        e.setStatus(Status.DELETED);
        repo.save(e);
    }

    private Role find(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role", "id", id));
    }
}
