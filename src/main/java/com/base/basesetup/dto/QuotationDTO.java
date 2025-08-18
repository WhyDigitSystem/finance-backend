	package com.base.basesetup.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuotationDTO {

	private Long id;
	private String quotationNo;
	private LocalDate quotationDate;
	private String deliveryAddress;
	private String customerAddress;
	private Long orgId;
	private String createdBy;
	private String code;
	private String finYear;
	private String companyAddress;	
	private String customerName;
//    private BigDecimal subTotal;

	private List<QuotationDetailsDTO> quotationDetailsDTO;
}
