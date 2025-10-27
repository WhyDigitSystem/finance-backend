package com.base.basesetup.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PyAdvancePaymentReceiptDTO {
	
    private String year;
    private String month;
    private Long orgId;
    private String client;
    private String clientCode;
    private String createdBy;
    private String updatedBy;
    private String cancelRemarks;
    private String type;
    private String party;
    private String yearType;
    private BigDecimal amount;
   
}
