package com.base.basesetup.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bankdetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankDetailsVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bankdetailsgen")
	@SequenceGenerator(name = "bankdetailsgen", sequenceName = "bankdetailsseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "bankdetailsid")
	private Long id;
	@Column(name = "bankname")
	private String bankName;
	@Column(name = "accountcode")
	private String accountCode;
	@Column(name = "beneficiaryname")
	private String beneficiaryName;
	@Column(name = "branch")
	private String branch;
	@Column(name = "ifsc")
	private String ifsc;
	@Column(name = "accountno")
	private Long accountNo;
	@Column(name = "accounttype")
	private String accountType;
	@Column(name = "primaryaccount")
	private boolean primaryAccount;

	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "companyid")
	CompanyVO companyVO;
}
