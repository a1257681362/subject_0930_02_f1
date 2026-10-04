package com.evops.aqua.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.evops.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 换池转移记录。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_aqua_transfer_record")
public class TransferRecord extends BaseEntity {
    @NotNull(message = "投苗批次不能为空")
    private Long batchId;

    @NotNull(message = "转出池不能为空")
    private Long fromPondId;

    @NotNull(message = "转入池不能为空")
    private Long toPondId;

    @NotNull(message = "转移日期不能为空")
    private LocalDate transferDate;

    /** 转移数量（尾） */
    @NotNull(message = "转移数量不能为空")
    @Min(value = 1, message = "转移数量必须大于0")
    private Integer quantity;

    private String reason;
}
