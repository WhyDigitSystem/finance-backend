package com.base.basesetup.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="accountsdetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountsDetailsVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accountsdetailsgen")
	@SequenceGenerator(name = "accountsdetailsgen", sequenceName = "accountsdetailsseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "accountsdetailsid")
	private Long id;
	@Column(name = "accountname",length = 150)
	private String accountName;
	@Column(name = "acategory",length = 100)
	private String aCategory;
	@Column(name = "subledgercode",length = 30)
	private String subLedgerCode;
	@Column(name = "ndebitamount", precision = 10, scale = 2)
	private BigDecimal nDebitAmount;
	@Column(name = "debitamount", precision = 10, scale = 2)
	private BigDecimal debitAmount;
	@Column(name = "ncreditamount", precision = 10, scale = 2)
	private BigDecimal nCreditAmount;
	@Column(name = "creditamount", precision = 10, scale = 2)
	private BigDecimal creditAmount;
	@Column(name = "arapflag")
	private boolean arapFlag;
	@Column(name = "arapamount", precision = 10, scale = 2)
	private BigDecimal arapAmount;
	@Column(name = "bdebitamount", precision = 10, scale = 2)
	private BigDecimal bDebitAmount;
	@Column(name = "bcreditamount", precision = 10, scale = 2)
	private BigDecimal bCrAmount;
	@Column(name = "barapamount", precision = 10, scale = 2)
	private BigDecimal bArapAmount;
	@Column(name = "acurrency",length = 10)
	private String aCurrency;
	@Column(name = "aexrate", precision = 10, scale = 2)
	private BigDecimal aExRate;
	@Column(name = "subledgername",length = 150)
	private String subledgerName;
	@Column(name = "narapamount", precision = 10, scale = 2)
	private BigDecimal nArapAmount;
	@Column(name = "tdsamount", precision = 10, scale = 2)
	private BigDecimal tdsAmount;
	@Column(name = "gstflag",length = 5)
	private int gstflag;
    
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "accountsid")
	AccountsVO accountsVO;

}
