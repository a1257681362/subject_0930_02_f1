package com.evops.aqua.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.evops.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 投苗批次（原始投苗单，不可删除）。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_aqua_seed_batch")
public class SeedBatch extends BaseEntity {
    /** 批次编号，全局唯一 */
    @NotBlank(message = "批次编号不能为空")
    private String batchNo;

    /** 当前所在养殖池 */
    @NotNull(message = "养殖池不能为空")
    private Long pondId;

    /** 苗种来源 */
    @NotBlank(message = "苗种来源不能为空")
    private String source;

    /** 入池数量（尾） */
    @NotNull(message = "入池数量不能为空")
    @Min(value = 1, message = "入池数量必须大于0")
    private Integer quantity;

    /** 投苗日期 */
    @NotNull(message = "投苗日期不能为空")
    private LocalDate stockDate;

    /** 计划出池日 */
    @NotNull(message = "计划出池日不能为空")
    private LocalDate plannedHarvestDate;

    /** 在养 / 已出池 */
    private String status;

    /** 实际出池日 */
    private LocalDate actualHarvestDate;
}
