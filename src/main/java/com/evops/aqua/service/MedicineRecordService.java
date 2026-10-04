package com.evops.aqua.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.evops.aqua.entity.MedicineRecord;

public interface MedicineRecordService extends IService<MedicineRecord> {
    /** 登记：按池按批校验通过后方可留痕。 */
    MedicineRecord add(MedicineRecord record);
}
