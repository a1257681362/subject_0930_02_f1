package com.evops.aqua.controller.dto;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/** 出池请求 */
public class HarvestRequest {
    @NotNull(message = "出池日期不能为空")
    private LocalDate harvestDate;

    /** 缺省为批次全部数量 */
    private Integer quantity;

    /** 去向 */
    private String destination;

    public LocalDate getHarvestDate() { return harvestDate; }
    public void setHarvestDate(LocalDate harvestDate) { this.harvestDate = harvestDate; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
}
