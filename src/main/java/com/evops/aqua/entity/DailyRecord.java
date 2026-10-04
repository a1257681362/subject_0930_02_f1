package com.evops.aqua.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.evops.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 日常记录：按池按批留痕。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_aqua_daily_record")
public class DailyRecord extends BaseEntity {
    @NotNull(message = "养殖池不能为空")
    private Long pondId;

    @NotNull(message = "投苗批次不能为空")
    private Long batchId;

    @NotNull(message = "记录日期不能为空")
    private LocalDate recordDate;

    /** 实测水深（米） */
    private BigDecimal waterDepthM;

    /** 实测盐度（‰） */
    private BigDecimal salinity;

    private String note;
}
