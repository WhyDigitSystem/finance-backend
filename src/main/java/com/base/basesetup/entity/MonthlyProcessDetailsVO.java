package com.base.basesetup.entity;

import java.math.BigDecimal;

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
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="monthlyprocessdetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyProcessDetailsVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "monthlyprocessdetailsgen")
	@SequenceGenerator(name = "monthlyprocessdetailsgen", sequenceName = "monthlyprocessdetailsseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "monthlyprocessdetailsid")
	private Long id;
	
	@Column(name = "month", length = 30)
	private String month; 
	
	@Column(name = "orgid")
	private Long orgId; 
	
	@Column(name = "year", length = 10)
	private String year;
	
	@Column(name = "clientcode", length = 150)
	private String clientCode;
	
	@Column(name = "client", length = 150)
	private String client;
	
	@Column(name = "accountcode", length = 50)
	private String elGlCode;
	
	@Column(name = "accountname", length = 150)
	private String elGl;
	
	@Column(name = "natureofaccount", length = 50)
	private String natureOfAccount;
	
	@Column(name = "groupname", length = 150)
	private String segment;
	
	@Column(name = "groupcode", length = 150)
	private String segmentCode;
	
	@Column(name = "maingroup", length = 150)
	private String mainGroup;
	
	private boolean mismatch;
	
	@Column(name = "approvestatus")
	private boolean approveStatus;
	
	
	@Column(name = "currentmonthdebit", precision = 20,scale = 2)
	private BigDecimal currentMonthDebit;
	@Column(name = "currentmonthcredit", precision = 20,scale = 2)
	private BigDecimal currentMonthCredit;
	@Column(name = "currentmonthclosing", precision = 20,scale = 2)
	private BigDecimal currentMonthClosing;
	@Column(name = "previousmonthdebit", precision = 20,scale = 2)
	private BigDecimal previoustMonthDebit;
	@Column(name = "previousmonthcredit", precision = 20,scale = 2)
	private BigDecimal previoustMonthCredit;
	@Column(name = "previousmonthclosing", precision = 20,scale = 2)
	private BigDecimal previoustMonthClosing;
	@Column(name = "forthemonthactdebit", precision = 20,scale = 2)
	private BigDecimal forTheMonthActDebit;
	@Column(name = "forthemonthactcredit", precision = 20,scale = 2)
	private BigDecimal forTheMonthActCredit;
	@Column(name = "forthemonthactclosing", precision = 20,scale = 2)
	private BigDecimal forTheMonthActClosing;
	@Column(name = "provisiondebit", precision = 20,scale = 2)
	private BigDecimal provisionDebit;
	@Column(name = "provisioncredit", precision = 20,scale = 2)
	private BigDecimal provisionCredit;
	@Column(name = "provisionclosing", precision = 20,scale = 2)
	private BigDecimal provisionClosing;
	@Column(name = "forthemonthdebit", precision = 20,scale = 2)
	private BigDecimal forTheMonthDebit;
	@Column(name = "forthemonthcredit", precision = 20,scale = 2)
	private BigDecimal forTheMonthCredit;
	@Column(name = "forthemonthclosing", precision = 20,scale = 2)
	private BigDecimal forTheMonthClosing;
	@Column(name = "provisionremarks",length = 150)
	private String provisionRemarks;
	
	@Column(name = "closingbalance", precision = 20,scale = 2)
	private BigDecimal closingBalance;
	
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "monthlyprocessid")
	private MonthlyProcessVO monthlyProcessVO;

}
