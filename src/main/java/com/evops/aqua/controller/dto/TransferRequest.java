package com.evops.aqua.controller.dto;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/** 换池请求 */
public class TransferRequest {
    @NotNull(message = "目标池不能为空")
    private Long toPondId;

    @NotNull(message = "转移日期不能为空")
    private LocalDate transferDate;

    /** 缺省为批次全部数量 */
    private Integer quantity;

    private String reason;

    public Long getToPondId() { return toPondId; }
    public void setToPondId(Long toPondId) { this.toPondId = toPondId; }
    public LocalDate getTransferDate() { return transferDate; }
    public void setTransferDate(LocalDate transferDate) { this.transferDate = transferDate; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
