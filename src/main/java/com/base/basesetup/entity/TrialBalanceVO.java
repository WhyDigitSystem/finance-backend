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
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbexcelupload")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrialBalanceVO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "tbgen")
	@SequenceGenerator(name = "tbgen", sequenceName = "tbseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "tbid")
	private Long id;
	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "accountname",length = 150)
	private String accountName;
	@Column(name = "accountcode",length = 20)
	private String accountCode;
	@Column(name = "clientcode",length = 10)
	private String clientCode;
	@Column(name = "client",length =100 )
	private String client;
	@Column(name = "month",length =30 )
	private String month;
	@Column(name = "transcredit",precision = 20,scale = 2)
	private BigDecimal transCredit;
	@Column(name = "transdebit",precision = 20,scale = 2)
	private BigDecimal transDebit;
	@Column(name = "opbalancedb",precision = 20,scale = 2)
	private BigDecimal opBalanceDb;
	@Column(name = "opbalancecr",precision = 20,scale = 2)
	private BigDecimal opBalanceCr;
	@Column(name = "clbalancedb",precision = 20,scale = 2)
	private BigDecimal clBalanceDb;
	@Column(name = "clbalancecr",precision = 20,scale = 2)
	private BigDecimal clBalanceCr;
	@Column(name = "finyear",length =10)
	private String finYear;
	@Column(name = "createdby",length =50)
	private String createdBy;
	@Column(name = "modifiedby",length =50)
	private String updatedBy;
	private boolean cancel = false;
	private boolean active;
	@Embedded
	@Builder.Default
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();

}
