package com.aliu.be.domain.role.controller;


import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.aliu.be.common.dto.ApiResponse;
import com.aliu.be.domain.role.dto.request.RoleCreateRequest;
import com.aliu.be.domain.role.dto.request.RoleUpdateRequest;
import com.aliu.be.domain.role.dto.response.RoleResponse;
import com.aliu.be.domain.role.service.RoleService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/roles")
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {
    private final RoleService service;

    public RoleController(RoleService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponse>> create(@Valid @RequestBody RoleCreateRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo role thành công", service.create(r)));
    }

    @PutMapping("/{id}")
    public ApiResponse<RoleResponse> update(@PathVariable Long id, @Valid @RequestBody RoleUpdateRequest r) {
        return ApiResponse.success("Cập nhật role thành công", service.update(id, r));
    }

    @GetMapping("/{id}")
    public ApiResponse<RoleResponse> get(@PathVariable Long id) {
        return ApiResponse.success(service.getById(id));
    }

    @GetMapping
    public ApiResponse<List<RoleResponse>> all() {
        return ApiResponse.success(service.getAll());
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ApiResponse.success("Vô hiệu hóa role thành công");
    }
}
