package com.evops.aqua.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evops.aqua.entity.FeedRecord;
import com.evops.aqua.mapper.FeedRecordMapper;
import com.evops.aqua.service.FeedRecordService;
import com.evops.aqua.service.support.AquaGuard;
import org.springframework.stereotype.Service;

@Service
public class FeedRecordServiceImpl extends ServiceImpl<FeedRecordMapper, FeedRecord> implements FeedRecordService {
    private final AquaGuard guard;

    public FeedRecordServiceImpl(AquaGuard guard) {
        this.guard = guard;
    }

    @Override
    public FeedRecord add(FeedRecord record) {
        guard.requireBatchInPond(record.getBatchId(), record.getPondId());
        record.setId(null);
        save(record);
        return record;
    }
}
