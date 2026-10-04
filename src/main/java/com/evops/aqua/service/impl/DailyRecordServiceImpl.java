package com.evops.aqua.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evops.aqua.entity.DailyRecord;
import com.evops.aqua.mapper.DailyRecordMapper;
import com.evops.aqua.service.DailyRecordService;
import com.evops.aqua.service.support.AquaGuard;
import org.springframework.stereotype.Service;

@Service
public class DailyRecordServiceImpl extends ServiceImpl<DailyRecordMapper, DailyRecord> implements DailyRecordService {
    private final AquaGuard guard;

    public DailyRecordServiceImpl(AquaGuard guard) {
        this.guard = guard;
    }

    @Override
    public DailyRecord add(DailyRecord record) {
        guard.requireBatchInPond(record.getBatchId(), record.getPondId());
        record.setId(null);
        save(record);
        return record;
    }
}
