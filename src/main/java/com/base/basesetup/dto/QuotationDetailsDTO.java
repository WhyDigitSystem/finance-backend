package com.base.basesetup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuotationDetailsDTO {

	private Long id;

	private String description;

	private Long unit;

	private Long pricre;

	private Long total;

}
