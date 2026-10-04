package com.evops.aqua.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.evops.aqua.entity.Pond;

public interface PondService extends IService<Pond> {
    /** 建池：池塘编号唯一，默认状态"启用"。 */
    Pond createPond(Pond pond);
}
