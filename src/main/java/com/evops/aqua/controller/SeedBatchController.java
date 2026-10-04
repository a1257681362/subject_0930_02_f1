package com.evops.aqua.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.evops.aqua.controller.dto.HarvestRequest;
import com.evops.aqua.controller.dto.TransferRequest;
import com.evops.aqua.entity.HarvestRecord;
import com.evops.aqua.entity.SeedBatch;
import com.evops.aqua.entity.TransferRecord;
import com.evops.aqua.service.SeedBatchService;
import com.evops.common.ApiResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** 投苗批次（原始投苗单）入口 */
@RestController
@RequestMapping("/api/aqua/batches")
public class SeedBatchController {
    private final SeedBatchService seedBatchService;

    public SeedBatchController(SeedBatchService seedBatchService) {
        this.seedBatchService = seedBatchService;
    }

    /** 投苗 */
    @PostMapping
    public ApiResponse<SeedBatch> create(@Validated @RequestBody SeedBatch batch) {
        return ApiResponse.ok(seedBatchService.createBatch(batch));
    }

    @GetMapping
    public ApiResponse<List<SeedBatch>> list(@RequestParam(required = false) Long pondId,
                                             @RequestParam(required = false) String batchNo) {
        QueryWrapper<SeedBatch> query = new QueryWrapper<>();
        if (pondId != null) {
            query.eq("pond_id", pondId);
        }
        if (batchNo != null && !batchNo.isEmpty()) {
            query.eq("batch_no", batchNo);
        }
        return ApiResponse.ok(seedBatchService.list(query));
    }

    @GetMapping("/{id}")
    public ApiResponse<SeedBatch> detail(@PathVariable Long id) {
        SeedBatch batch = seedBatchService.getById(id);
        if (batch == null) {
            throw new IllegalArgumentException("投苗批次不存在: " + id);
        }
        return ApiResponse.ok(batch);
    }

    /** 原始投苗单不可删除 */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        throw new IllegalArgumentException("原始投苗单不可删除: " + id);
    }

    /** 换池：形成转移记录 */
    @PostMapping("/{id}/transfer")
    public ApiResponse<TransferRecord> transfer(@PathVariable Long id,
                                                @Validated @RequestBody TransferRequest request) {
        return ApiResponse.ok(seedBatchService.transfer(
                id, request.getToPondId(), request.getTransferDate(), request.getQuantity(), request.getReason()));
    }

    /** 出池 */
    @PostMapping("/{id}/harvest")
    public ApiResponse<HarvestRecord> harvest(@PathVariable Long id,
                                              @Validated @RequestBody HarvestRequest request) {
        return ApiResponse.ok(seedBatchService.harvest(
                id, request.getHarvestDate(), request.getQuantity(), request.getDestination()));
    }
}
