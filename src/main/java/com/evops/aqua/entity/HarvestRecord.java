package com.evops.aqua.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.evops.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 出池记录。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_aqua_harvest_record")
public class HarvestRecord extends BaseEntity {
    @NotNull(message = "投苗批次不能为空")
    private Long batchId;

    @NotNull(message = "养殖池不能为空")
    private Long pondId;

    @NotNull(message = "出池日期不能为空")
    private LocalDate harvestDate;

    /** 出池数量（尾） */
    @NotNull(message = "出池数量不能为空")
    @Min(value = 1, message = "出池数量必须大于0")
    private Integer quantity;

    /** 去向 */
    private String destination;
}
