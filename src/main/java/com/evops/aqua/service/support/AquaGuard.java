package com.evops.aqua.service.support;

import com.evops.aqua.entity.Pond;
import com.evops.aqua.entity.SeedBatch;
import com.evops.aqua.mapper.PondMapper;
import com.evops.aqua.mapper.SeedBatchMapper;
import org.springframework.stereotype.Component;

/**
 * 台账通用校验：池、批次存在性与"按池按批"一致性。
 */
@Component
public class AquaGuard {
    public static final String BATCH_STATUS_RAISING = "在养";
    public static final String BATCH_STATUS_HARVESTED = "已出池";

    private final PondMapper pondMapper;
    private final SeedBatchMapper seedBatchMapper;

    public AquaGuard(PondMapper pondMapper, SeedBatchMapper seedBatchMapper) {
        this.pondMapper = pondMapper;
        this.seedBatchMapper = seedBatchMapper;
    }

    public Pond requirePond(Long pondId) {
        Pond pond = pondId == null ? null : pondMapper.selectById(pondId);
        if (pond == null) {
            throw new IllegalArgumentException("养殖池不存在: " + pondId);
        }
        return pond;
    }

    public SeedBatch requireBatch(Long batchId) {
        SeedBatch batch = batchId == null ? null : seedBatchMapper.selectById(batchId);
        if (batch == null) {
            throw new IllegalArgumentException("投苗批次不存在: " + batchId);
        }
        return batch;
    }

    /**
     * 按池按批留痕的前提：批次存在、在养，且当前正挂在该池。
     */
    public SeedBatch requireBatchInPond(Long batchId, Long pondId) {
        SeedBatch batch = requireBatch(batchId);
        requirePond(pondId);
        if (!BATCH_STATUS_RAISING.equals(batch.getStatus())) {
            throw new IllegalArgumentException("批次 " + batch.getBatchNo() + " 已出池，不能再登记养殖记录");
        }
        if (!batch.getPondId().equals(pondId)) {
            throw new IllegalArgumentException(
                    "批次 " + batch.getBatchNo() + " 当前不在池 " + pondId + "，请核对池号或先办理换池");
        }
        return batch;
    }
}
