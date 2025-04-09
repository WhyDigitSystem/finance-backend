package com.base.basesetup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankDetailsDTO {
	private String bankName;
	private String accountCode;
	private String beneficiaryName;
	private String branch;
	private String ifsc;
	private Long accountNo;
	private String accountType;
	private boolean primaryAccount;

}
