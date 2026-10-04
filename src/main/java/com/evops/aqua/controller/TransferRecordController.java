package com.evops.aqua.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.evops.aqua.entity.TransferRecord;
import com.evops.aqua.mapper.TransferRecordMapper;
import com.evops.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** 转移记录只读查询入口（记录由换池动作产生，不提供新增/删除） */
@RestController
@RequestMapping("/api/aqua/transfers")
public class TransferRecordController {
    private final TransferRecordMapper transferRecordMapper;

    public TransferRecordController(TransferRecordMapper transferRecordMapper) {
        this.transferRecordMapper = transferRecordMapper;
    }

    @GetMapping
    public ApiResponse<List<TransferRecord>> list(@RequestParam(required = false) Long batchId) {
        QueryWrapper<TransferRecord> query = new QueryWrapper<>();
        if (batchId != null) {
            query.eq("batch_id", batchId);
        }
        query.orderByDesc("id");
        return ApiResponse.ok(transferRecordMapper.selectList(query));
    }
}
