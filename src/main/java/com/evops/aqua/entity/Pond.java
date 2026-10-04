package com.evops.aqua.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.evops.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 养殖池档案：水深、盐度、所属海区。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_aqua_pond")
public class Pond extends BaseEntity {
    @NotBlank(message = "池塘编号不能为空")
    private String pondCode;

    @NotBlank(message = "池塘名称不能为空")
    private String pondName;

    /** 所属海区 */
    @NotBlank(message = "所属海区不能为空")
    private String seaArea;

    /** 水深（米） */
    @NotNull(message = "水深不能为空")
    @DecimalMin(value = "0", inclusive = false, message = "水深必须大于0")
    private BigDecimal waterDepthM;

    /** 盐度（‰） */
    @NotNull(message = "盐度不能为空")
    @DecimalMin(value = "0", message = "盐度不能为负")
    private BigDecimal salinity;

    /** 启用 / 停用 */
    private String status;
}
