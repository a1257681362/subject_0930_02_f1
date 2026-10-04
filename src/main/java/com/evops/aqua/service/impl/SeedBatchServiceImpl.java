package com.evops.aqua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evops.aqua.entity.HarvestRecord;
import com.evops.aqua.entity.SeedBatch;
import com.evops.aqua.entity.TransferRecord;
import com.evops.aqua.mapper.HarvestRecordMapper;
import com.evops.aqua.mapper.SeedBatchMapper;
import com.evops.aqua.mapper.TransferRecordMapper;
import com.evops.aqua.service.SeedBatchService;
import com.evops.aqua.service.support.AquaGuard;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class SeedBatchServiceImpl extends ServiceImpl<SeedBatchMapper, SeedBatch> implements SeedBatchService {
    private final AquaGuard guard;
    private final TransferRecordMapper transferRecordMapper;
    private final HarvestRecordMapper harvestRecordMapper;

    public SeedBatchServiceImpl(AquaGuard guard,
                                TransferRecordMapper transferRecordMapper,
                                HarvestRecordMapper harvestRecordMapper) {
        this.guard = guard;
        this.transferRecordMapper = transferRecordMapper;
        this.harvestRecordMapper = harvestRecordMapper;
    }

    @Override
    public SeedBatch createBatch(SeedBatch batch) {
        guard.requirePond(batch.getPondId());
        long exists = count(new QueryWrapper<SeedBatch>().eq("batch_no", batch.getBatchNo()));
        if (exists > 0) {
            throw new IllegalArgumentException("批次编号不能重复: " + batch.getBatchNo());
        }
        if (batch.getPlannedHarvestDate().isBefore(batch.getStockDate())) {
            throw new IllegalArgumentException("计划出池日不能早于投苗日期");
        }
        batch.setId(null);
        batch.setStatus(AquaGuard.BATCH_STATUS_RAISING);
        batch.setActualHarvestDate(null);
        save(batch);
        return batch;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TransferRecord transfer(Long batchId, Long toPondId, LocalDate transferDate, Integer quantity, String reason) {
        SeedBatch batch = guard.requireBatch(batchId);
        if (!AquaGuard.BATCH_STATUS_RAISING.equals(batch.getStatus())) {
            throw new IllegalArgumentException("批次 " + batch.getBatchNo() + " 已出池，不能换池");
        }
        guard.requirePond(toPondId);
        if (batch.getPondId().equals(toPondId)) {
            throw new IllegalArgumentException("目标池与当前池相同，无需换池");
        }
        if (transferDate == null) {
            throw new IllegalArgumentException("转移日期不能为空");
        }
        int qty = quantity == null ? batch.getQuantity() : quantity;
        if (qty <= 0) {
            throw new IllegalArgumentException("转移数量必须大于0");
        }

        TransferRecord record = new TransferRecord();
        record.setBatchId(batch.getId());
        record.setFromPondId(batch.getPondId());
        record.setToPondId(toPondId);
        record.setTransferDate(transferDate);
        record.setQuantity(qty);
        record.setReason(reason);
        transferRecordMapper.insert(record);

        batch.setPondId(toPondId);
        updateById(batch);
        return record;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HarvestRecord harvest(Long batchId, LocalDate harvestDate, Integer quantity, String destination) {
        SeedBatch batch = guard.requireBatch(batchId);
        if (!AquaGuard.BATCH_STATUS_RAISING.equals(batch.getStatus())) {
            throw new IllegalArgumentException("批次 " + batch.getBatchNo() + " 已出池，不能重复出池");
        }
        if (harvestDate == null) {
            throw new IllegalArgumentException("出池日期不能为空");
        }
        int qty = quantity == null ? batch.getQuantity() : quantity;
        if (qty <= 0) {
            throw new IllegalArgumentException("出池数量必须大于0");
        }

        HarvestRecord record = new HarvestRecord();
        record.setBatchId(batch.getId());
        record.setPondId(batch.getPondId());
        record.setHarvestDate(harvestDate);
        record.setQuantity(qty);
        record.setDestination(destination);
        harvestRecordMapper.insert(record);

        batch.setStatus(AquaGuard.BATCH_STATUS_HARVESTED);
        batch.setActualHarvestDate(harvestDate);
        updateById(batch);
        return record;
    }
}
