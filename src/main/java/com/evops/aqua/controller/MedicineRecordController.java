package com.evops.aqua.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.evops.aqua.entity.MedicineRecord;
import com.evops.aqua.service.MedicineRecordService;
import com.evops.common.ApiResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aqua/medicine-records")
public class MedicineRecordController {
    private final MedicineRecordService service;

    public MedicineRecordController(MedicineRecordService service) {
        this.service = service;
    }

    @PostMapping
    public ApiResponse<MedicineRecord> create(@Validated @RequestBody MedicineRecord record) {
        return ApiResponse.ok(service.add(record));
    }

    @GetMapping
    public ApiResponse<List<MedicineRecord>> list(@RequestParam(required = false) Long pondId,
                                        @RequestParam(required = false) Long batchId) {
        QueryWrapper<MedicineRecord> query = new QueryWrapper<>();
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
