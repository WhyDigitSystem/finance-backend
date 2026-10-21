package com.base.basesetup.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TaxInvoiceKitLineDTO {
	
	private Long id;
	private String annexureDate;
    private String manifestNo;
    private String emitter;
    private String location;
    private String kitNo;
    private BigDecimal kitQty;

}
