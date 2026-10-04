package com.evops.aqua.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evops.aqua.entity.InspectionRecord;
import com.evops.aqua.mapper.InspectionRecordMapper;
import com.evops.aqua.service.InspectionRecordService;
import com.evops.aqua.service.support.AquaGuard;
import org.springframework.stereotype.Service;

@Service
public class InspectionRecordServiceImpl extends ServiceImpl<InspectionRecordMapper, InspectionRecord> implements InspectionRecordService {
    private final AquaGuard guard;

    public InspectionRecordServiceImpl(AquaGuard guard) {
        this.guard = guard;
    }

    @Override
    public InspectionRecord add(InspectionRecord record) {
        guard.requireBatchInPond(record.getBatchId(), record.getPondId());
        record.setId(null);
        save(record);
        return record;
    }
}
