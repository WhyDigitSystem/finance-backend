package com.base.basesetup.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.base.basesetup.dto.CreatedUpdatedDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "previousyearactual")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreviousYearActualVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "previousyearactualgen")
	@SequenceGenerator(name = "previousyearactualgen", sequenceName = "previousyearactualseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "previousyearactualid")
	private Long id;

	@Column(name = "orgid")
	private Long orgId;

	@Column(name = "client", length = 100)
	private String client;
	@Column(name = "clientcode", length = 20)
	private String clientCode;
	@Column(name = "year", length = 10)
	private String year;
	@Column(name = "month", length = 30)
	private String month;
	@Column(name = "accountname", length = 100)
	private String accountName;
	@Column(name = "accountcode", length = 25)
	private String accountCode;
	@Column(name = "natureofaccount", length = 100)
	private String natureOfAccount;
	@Column(name = "amount", length = 100)
	private BigDecimal amount;
	@Column(name = "active")
	private boolean active;
	@Column(name = "createdby", length = 50)
	private String createdBy;
	@Column(name = "modifiedby", length = 50)
	private String updatedBy;
	@Column(name = "cancel")
	private boolean cancel;
	@Column(name = "maingroup",length = 100)
	private String mainGroup;
	@Column(name = "subgroup",length = 100)
	private String subGroup;
	@Column(name = "subgroupcode",length = 100)
	private String subGroupCode;
	
	private int monthsequence;
	
	@Column(name = "quater")
	private String quater;

	@Embedded
	private CreatedUpdatedDate createdUpdatedDate = new CreatedUpdatedDate();

}