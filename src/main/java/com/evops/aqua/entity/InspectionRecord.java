package com.evops.aqua.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.evops.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 抽检记录。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_aqua_inspection_record")
public class InspectionRecord extends BaseEntity {
    @NotNull(message = "养殖池不能为空")
    private Long pondId;

    @NotNull(message = "投苗批次不能为空")
    private Long batchId;

    @NotNull(message = "抽检日期不能为空")
    private LocalDate inspectDate;

    /** 抽样数量（尾） */
    @NotNull(message = "抽样数量不能为空")
    @Min(value = 1, message = "抽样数量必须大于0")
    private Integer sampleCount;

    /** 均重（克） */
    private BigDecimal avgWeightG;

    /** 抽检结论 */
    private String result;
}
