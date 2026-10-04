package com.evops.aqua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evops.aqua.entity.Pond;
import com.evops.aqua.mapper.PondMapper;
import com.evops.aqua.service.PondService;
import org.springframework.stereotype.Service;

@Service
public class PondServiceImpl extends ServiceImpl<PondMapper, Pond> implements PondService {
    private static final String STATUS_ENABLED = "启用";

    @Override
    public Pond createPond(Pond pond) {
        long exists = count(new QueryWrapper<Pond>().eq("pond_code", pond.getPondCode()));
        if (exists > 0) {
            throw new IllegalArgumentException("池塘编号已存在: " + pond.getPondCode());
        }
        if (pond.getStatus() == null || pond.getStatus().isEmpty()) {
            pond.setStatus(STATUS_ENABLED);
        }
        save(pond);
        return pond;
    }
}
