package com.evops.aqua.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.evops.aqua.entity.FeedRecord;

public interface FeedRecordService extends IService<FeedRecord> {
    /** 登记：按池按批校验通过后方可留痕。 */
    FeedRecord add(FeedRecord record);
}
