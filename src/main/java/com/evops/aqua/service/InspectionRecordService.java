package com.evops.aqua.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.evops.aqua.entity.InspectionRecord;

public interface InspectionRecordService extends IService<InspectionRecord> {
    /** 登记：按池按批校验通过后方可留痕。 */
    InspectionRecord add(InspectionRecord record);
}
