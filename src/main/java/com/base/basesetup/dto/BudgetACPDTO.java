package com.base.basesetup.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetACPDTO {
	

    private String year;
    private String month;
    private Long orgId;
    private String client;
    private String clientCode;
    private String createdBy;
    private String supplier;
    private String paymentTerms;
    private BigDecimal grossPurchase;
    private Integer noOfMonthPurchase;
    private BigDecimal outStanding;
    private Integer paymentPeriod;
    private BigDecimal slab1;
    private BigDecimal slab2;
    private BigDecimal slab3;
    private BigDecimal slab4;
    private BigDecimal slab5;
    private BigDecimal slab6;
    private String yearType;
    private String type;
   

}
