package com.evops.aqua.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.evops.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 饲料投喂记录：按池按批留痕。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_aqua_feed_record")
public class FeedRecord extends BaseEntity {
    @NotNull(message = "养殖池不能为空")
    private Long pondId;

    @NotNull(message = "投苗批次不能为空")
    private Long batchId;

    @NotNull(message = "投喂日期不能为空")
    private LocalDate feedDate;

    @NotBlank(message = "饲料名称不能为空")
    private String feedName;

    /** 投喂量（公斤） */
    @NotNull(message = "投喂量不能为空")
    @DecimalMin(value = "0", inclusive = false, message = "投喂量必须大于0")
    private BigDecimal quantityKg;

    private String note;
}
