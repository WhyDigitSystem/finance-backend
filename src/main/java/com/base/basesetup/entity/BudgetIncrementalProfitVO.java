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
@Table(name = "budgetincrementalprofit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetIncrementalProfitVO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "budgetincrementalprofitgen")
	@SequenceGenerator(name = "budgetincrementalprofitgen", sequenceName = "budgetincrementalprofitseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "budgetincrementalprofitid")
	private Long id;

	@Column(name = "year", length = 10)
	private String year;

	@Column(name = "month", length = 50)
	private String month;

	@Column(name = "orgid")
	private Long orgId;

	@Column(name = "client", length = 150)
	private String client;

	@Column(name = "clientcode", length = 50)
	private String clientCode;

	@Column(name = "createdby", length = 50)
	private String createdBy;

	@Column(name = "modifiedby", length = 50)
	private String updatedBy;

	@Column(name = "cancelremarks", length = 150)
	private String cancelRemarks;

	private boolean cancel = false;

	private boolean active = true;
	
	@Column(name = "subgroup", length = 150)
	private String subGroup;
	@Column(name = "accountname", length = 150)
	private String accountName;
	@Column(name = "accountcode", length = 150)
	private String accountCode;

	@Column(name = "amount", precision = 15, scale = 2)
	private BigDecimal amount;

	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}
