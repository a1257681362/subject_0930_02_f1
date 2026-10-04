package com.evops.aqua.controller;

import com.evops.aqua.entity.Pond;
import com.evops.aqua.service.PondService;
import com.evops.common.ApiResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** 养殖池档案入口 */
@RestController
@RequestMapping("/api/aqua/ponds")
public class PondController {
    private final PondService pondService;

    public PondController(PondService pondService) {
        this.pondService = pondService;
    }

    /** 建池 */
    @PostMapping
    public ApiResponse<Pond> create(@Validated @RequestBody Pond pond) {
        return ApiResponse.ok(pondService.createPond(pond));
    }

    @GetMapping
    public ApiResponse<List<Pond>> list() {
        return ApiResponse.ok(pondService.list());
    }

    @GetMapping("/{id}")
    public ApiResponse<Pond> detail(@PathVariable Long id) {
        Pond pond = pondService.getById(id);
        if (pond == null) {
            throw new IllegalArgumentException("养殖池不存在: " + id);
        }
        return ApiResponse.ok(pond);
    }
}
