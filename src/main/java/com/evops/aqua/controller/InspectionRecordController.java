package com.evops.aqua.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.evops.aqua.entity.InspectionRecord;
import com.evops.aqua.service.InspectionRecordService;
import com.evops.common.ApiResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aqua/inspections")
public class InspectionRecordController {
    private final InspectionRecordService service;

    public InspectionRecordController(InspectionRecordService service) {
        this.service = service;
    }

    @PostMapping
    public ApiResponse<InspectionRecord> create(@Validated @RequestBody InspectionRecord record) {
        return ApiResponse.ok(service.add(record));
    }

    @GetMapping
    public ApiResponse<List<InspectionRecord>> list(@RequestParam(required = false) Long pondId,
                                        @RequestParam(required = false) Long batchId) {
        QueryWrapper<InspectionRecord> query = new QueryWrapper<>();
        if (pondId != null) {
            query.eq("pond_id", pondId);
        }
        if (batchId != null) {
            query.eq("batch_id", batchId);
        }
        query.orderByDesc("id");
        return ApiResponse.ok(service.list(query));
    }
}
