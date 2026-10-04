package com.evops.aqua.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.evops.aqua.entity.DailyRecord;

public interface DailyRecordService extends IService<DailyRecord> {
    /** 登记：按池按批校验通过后方可留痕。 */
    DailyRecord add(DailyRecord record);
}
