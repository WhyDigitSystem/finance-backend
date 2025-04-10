package com.base.basesetup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceProductLinesDTO {
	
	private String description;
    private String quantity;
    private String rate;
    private Long amount;
	

}