package com.base.basesetup.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuotationNewDTO {

	private Long id;
	private String quotationTo;
	private String shippingAddress;
	private String customerAddress;
	private Long orgId;
	private String createdBy;
	private String code;
//		private String finYear;

	private List<QuotationDetailsNewDTO> quotationDetailsDTO;
}
