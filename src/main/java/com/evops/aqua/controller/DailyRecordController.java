package com.evops.aqua.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.evops.aqua.entity.DailyRecord;
import com.evops.aqua.service.DailyRecordService;
import com.evops.common.ApiResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aqua/daily-records")
public class DailyRecordController {
    private final DailyRecordService service;

    public DailyRecordController(DailyRecordService service) {
        this.service = service;
    }

    @PostMapping
    public ApiResponse<DailyRecord> create(@Validated @RequestBody DailyRecord record) {
        return ApiResponse.ok(service.add(record));
    }

    @GetMapping
    public ApiResponse<List<DailyRecord>> list(@RequestParam(required = false) Long pondId,
                                        @RequestParam(required = false) Long batchId) {
        QueryWrapper<DailyRecord> query = new QueryWrapper<>();
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
