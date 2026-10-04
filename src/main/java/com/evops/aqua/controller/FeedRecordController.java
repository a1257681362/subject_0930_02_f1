package com.evops.aqua.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.evops.aqua.entity.FeedRecord;
import com.evops.aqua.service.FeedRecordService;
import com.evops.common.ApiResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aqua/feed-records")
public class FeedRecordController {
    private final FeedRecordService service;

    public FeedRecordController(FeedRecordService service) {
        this.service = service;
    }

    @PostMapping
    public ApiResponse<FeedRecord> create(@Validated @RequestBody FeedRecord record) {
        return ApiResponse.ok(service.add(record));
    }

    @GetMapping
    public ApiResponse<List<FeedRecord>> list(@RequestParam(required = false) Long pondId,
                                        @RequestParam(required = false) Long batchId) {
        QueryWrapper<FeedRecord> query = new QueryWrapper<>();
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
