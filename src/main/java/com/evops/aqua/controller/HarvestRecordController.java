package com.evops.aqua.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.evops.aqua.entity.HarvestRecord;
import com.evops.aqua.mapper.HarvestRecordMapper;
import com.evops.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** 出池记录只读查询入口（记录由出池动作产生，不提供新增/删除） */
@RestController
@RequestMapping("/api/aqua/harvests")
public class HarvestRecordController {
    private final HarvestRecordMapper harvestRecordMapper;

    public HarvestRecordController(HarvestRecordMapper harvestRecordMapper) {
        this.harvestRecordMapper = harvestRecordMapper;
    }

    @GetMapping
    public ApiResponse<List<HarvestRecord>> list(@RequestParam(required = false) Long batchId) {
        QueryWrapper<HarvestRecord> query = new QueryWrapper<>();
        if (batchId != null) {
            query.eq("batch_id", batchId);
        }
        query.orderByDesc("id");
        return ApiResponse.ok(harvestRecordMapper.selectList(query));
    }
}
