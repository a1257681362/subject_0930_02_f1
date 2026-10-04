package com.evops.aqua.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evops.aqua.entity.MedicineRecord;
import com.evops.aqua.mapper.MedicineRecordMapper;
import com.evops.aqua.service.MedicineRecordService;
import com.evops.aqua.service.support.AquaGuard;
import org.springframework.stereotype.Service;

@Service
public class MedicineRecordServiceImpl extends ServiceImpl<MedicineRecordMapper, MedicineRecord> implements MedicineRecordService {
    private final AquaGuard guard;

    public MedicineRecordServiceImpl(AquaGuard guard) {
        this.guard = guard;
    }

    @Override
    public MedicineRecord add(MedicineRecord record) {
        guard.requireBatchInPond(record.getBatchId(), record.getPondId());
        record.setId(null);
        save(record);
        return record;
    }
}
