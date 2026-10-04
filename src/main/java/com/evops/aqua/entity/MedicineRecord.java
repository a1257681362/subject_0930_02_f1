package com.evops.aqua.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.evops.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 用药记录：按池按批留痕。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_aqua_medicine_record")
public class MedicineRecord extends BaseEntity {
    @NotNull(message = "养殖池不能为空")
    private Long pondId;

    @NotNull(message = "投苗批次不能为空")
    private Long batchId;

    @NotNull(message = "用药日期不能为空")
    private LocalDate useDate;

    @NotBlank(message = "药品名称不能为空")
    private String medicineName;

    @NotBlank(message = "剂量不能为空")
    private String dosage;

    /** 用药原因 */
    private String reason;
}
