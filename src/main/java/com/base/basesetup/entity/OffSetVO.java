package com.base.basesetup.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.base.basesetup.dto.CreatedUpdatedDate;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="GST_ARAPOFFSETHDRID")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OffSetVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "offsetgen")
	@SequenceGenerator(name = "offsetgen", sequenceName = "offsetseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "offsetid")
	private Long id;
	@Column(name = "branch",length =25)
	private String branch;
	@Column(name = "branchcode",length =25)
	private String branchCode;
	@Column(name = "finyear",length =10)
	private String finYear;
	@Column(name = "docid",length =25)
	private String docId;
	@Column(name = "docdate")
	private LocalDate docDate=LocalDate.now();
	@Column(name = "rpdocid",length =50)
	private String rpDocId;
	@Column(name = "rpdocdate")
	private LocalDate rpDocDate;
	@Column(name = "subledgertype",length =50)
	private String subLedgerType;
	@Column(name = "subledgername",length =100)
	private String subLedgername;
	@Column(name = "subledgercode",length =50)
	private String subLedgerCode;
	@Column(name = "accountname",length =100)
	private String accountName;
	@Column(name = "acategory",length =50)
	private String aCategoty;
	
	
	@Column(name = "currency",length =15)
	private String currency;
	@Column(name = "exrate",precision =10,scale = 2)
	private BigDecimal exRate;
	@Column(name = "amount",precision =10,scale = 2)
	private BigDecimal amount;
	
	
	@Column(name = "fxgaorloss",precision =10,scale = 2)
	private BigDecimal fxGaOrLoss;
	@Column(name = "totalsettled",precision =10,scale = 2)
	private BigDecimal totalSettled;
	@Column(name = "roundoffamount",precision =10,scale = 2)
	private BigDecimal roundOffAmount;
	@Column(name = "onaccount",precision =10,scale = 2)
	private BigDecimal onAccount;
	
	@Column(name = "narration",length =150)
	private String narration;
	
	
	@Column(name = "orgid",length =15)
	private Long orgId;
	@Column(name = "active")
	private boolean active;
	@Column(name = "cancel")
	private boolean cancel;
	@Column(name = "cancelremarks",length =50)
	private String canelRemarks;
	@Column(name = "createdby",length =25)
	private String createdBy;
	@Column(name = "modifiedby",length =25)
	private String updatedBy;
	@Column(name = "Screencode",length =10)
	private String ScreenCode="OFS";
	@Column(name = "Screenname",length =25)
	private String ScreenName="OFF SET";
	
	
	@OneToMany(mappedBy = "offSetVO", cascade = CascadeType.ALL)
	@JsonManagedReference
	List<OffSetDetailsVO> offSetDetailsVO;


	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
	
	

}
