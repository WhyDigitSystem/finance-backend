package com.base.basesetup.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartySalesPersonTaggingDTO {
	private String salesPerson;

	private String empCode;

	private String salesBranch;

	private LocalDate effectiveFrom;

	private LocalDate effectiveTill;
}


