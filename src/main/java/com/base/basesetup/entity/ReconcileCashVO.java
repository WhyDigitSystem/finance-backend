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
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reconcilecash")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReconcileCashVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reconcilecashgen")
	@SequenceGenerator(name = "reconcilecashgen", sequenceName = "reconcilecashseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "reconcilecashid")
	private Long id;
	@Column(name = "docid",length = 30)
	private String docId;
	@Column(name = "docdate")
	private LocalDate docDate=LocalDate.now();
	@Column(name="accountname",length=30)
	private String cashAccount;
	@Column(name="bookbalance",precision = 10,scale = 2)
	private BigDecimal balanceAsPerBooks;
	@Column(name="dn1",length = 10)
	private int dn1;
	@Column(name="dn2",length = 10)
	private int dn2;
	@Column(name="dn3",length = 10)
	private int dn3;
	@Column(name="dn4",length = 10)
	private int dn4;
	@Column(name="dn5",length = 10)
	private int dn5;
	@Column(name="dn6",length = 10)
	private int dn6;
	@Column(name="dn7",length = 10)
	private int dn7;
	@Column(name="dn8",length = 10)
	private int dn8;
	@Column(name="dn1amt",length = 10)
	private BigDecimal dn1Amt;
	@Column(name="dn2amt",length = 10)
	private BigDecimal dn2Amt;
	@Column(name="dn3amt",length = 10)
	private BigDecimal dn3Amt;
	@Column(name="dn4amt",length = 10)
	private BigDecimal dn4Amt;
	@Column(name="dn5amt",length = 10)
	private BigDecimal dn5Amt;
	@Column(name="dn6amt",length = 10)
	private BigDecimal dn6Amt;
	@Column(name="dn7amt",length = 10)
	private BigDecimal dn7Amt;
	@Column(name="dn8amt",length = 10)
	private BigDecimal dn8Amt;
	@Column(name="totalpamount",length = 10)
	private BigDecimal totalPhyAmount ;
	@Column(name="diffamount",length = 10)
	private BigDecimal differenceAmount;
	@Column(name="remarks",length = 150)
	private String remarks;
	
	@Builder.Default
	@Column(name = "screencode",length = 5)
	private String screenCode="RCH";
	@Builder.Default
	@Column(name="screenname",length = 20)
	private String screenName="RECONCILE CASH";
	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "branch", length = 25)
	private String branch;
	@Column(name = "branchcode", length = 20)
	private String branchCode;
	@Column(name = "createdby", length = 25)
	private String createdBy;
	@Column(name = "modifiedby", length = 25)
	private String updatedBy;
	@Column(name = "active")
	private boolean active;
	@Column(name = "cancel")
	private boolean cancel;
	@Column(name = "cancelremarks", length = 50)
	private String cancelRemarks;
	@Column(name = "finyear", length = 5)
	private String finYear;
	@Column(name = "ipno", length = 15)
	private String ipNo;
	@Column(name = "latitude", length = 100)
	private String latitude;
	
	@Embedded
	@Builder.Default
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();

	
}

