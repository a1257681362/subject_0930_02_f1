package com.evops.aqua.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.evops.aqua.entity.HarvestRecord;
import com.evops.aqua.entity.SeedBatch;
import com.evops.aqua.entity.TransferRecord;

import java.time.LocalDate;

public interface SeedBatchService extends IService<SeedBatch> {
    /** 投苗：批次编号唯一，池塘须存在，计划出池日不早于投苗日。 */
    SeedBatch createBatch(SeedBatch batch);

    /** 换池：生成转移记录并把批次挂到目标池。 */
    TransferRecord transfer(Long batchId, Long toPondId, LocalDate transferDate, Integer quantity, String reason);

    /** 出池：生成出池记录并把批次置为"已出池"。 */
    HarvestRecord harvest(Long batchId, LocalDate harvestDate, Integer quantity, String destination);
}
