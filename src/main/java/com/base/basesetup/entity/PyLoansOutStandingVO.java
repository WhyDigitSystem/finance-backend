package com.base.basesetup.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

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
@Table(name="pyloanoutstanding")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PyLoansOutStandingVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pyloanoutstandinggen")
	@SequenceGenerator(name = "pyloanoutstandinggen", sequenceName = "pyloanoutstandingseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "pyloanoutstandingid")
	private Long id;
	
	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "client",length = 100 )
	private String client;
	@Column(name = "clientcode",length = 20 )
	private String clientCode;
	@Column(name = "year",length = 10 )
	private String year;
	@Column(name = "month",length = 30 )
	private String month;
	@Column(name = "accountname",length = 100)
	private String accountName; 
	@Column(name = "accountcode",length = 25)
	private String accountCode;
	@Column(name = "amount",length = 100)
	private BigDecimal amount;
	
	@Column(name = "banckname",length = 100)
	private String banckName;
	@Column(name = "sanctionamount",length = 100)
	private BigDecimal sanctionAmount;
	@Column(name = "outstanding",length = 100)
	private BigDecimal outstanding;
	@Column(name = "interestrate",length = 100)
	private double interestRate;
	@Column(name = "tenure")
	private int tenure;
	@Column(name = "interest",length = 100)
	private BigDecimal interest;
	@Column(name = "principal",length = 100)
	private BigDecimal principal;
	@Column(name = "emitotal",length = 100)
	private BigDecimal emiTotal;
	@Column(name="tenuredate")
	private LocalDate tenureDate;
	@Column(name = "balancemonth")
	private int balanceMonth;
	@Column(name = "active")
	private boolean active;
	@Column(name = "createdby",length = 50)
	private String createdBy;
	@Column(name = "modifiedby",length = 50)
	private String updatedBy;
	@Column(name = "cancel")
	private boolean cancel;
	@Column(name = "quater",length = 5)
	private String quater;
	private int monthsequence;
	
	@Embedded
	private CreatedUpdatedDate createdUpdatedDate= new CreatedUpdatedDate();

}
