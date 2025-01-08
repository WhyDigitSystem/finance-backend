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
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "paymentgen")
	@SequenceGenerator(name = "paymentgen", sequenceName = "paymentseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "paymentid")
	private Long id;
	
	@Column(name = "orgid")
	private Long orgId;

    @Column(name = "paymenttype", length = 20)
    private String paymentType;

    @Column(name = "bankchargeacc", length = 50)
    private String bankChargeAcc;

    @Column(name = "docid", length = 50)
    private String docId;

    @Column(name = "docdate")
    private LocalDate docDate = LocalDate.now();

    @Column(name = "bankcharges", precision = 10, scale = 2)
    private BigDecimal bankCharges;

    @Column(name = "bankincurrency", length = 10)
    private String bankInCurrency;

    @Column(name = "type", length = 50)
    private String type;

    @Column(name = "partycode", length = 50)
    private String partyCode;

    @Column(name = "servicetaxamt", precision = 10, scale = 2)
    private BigDecimal serviceTaxAmt;

    @Column(name = "staxincurrency", length = 10)
    private String sTaxInCurrency;

    @Column(name = "partyname", length = 150)
    private String partyName;

    @Column(name = "chequebank", length = 50)
    private String chequeBank;

    @Column(name = "gststate", length = 50)
    private String gstState;

    @Column(name = "gstin", length = 50)
    private String gstIn;

    @Column(name = "chequeno", length = 20)
    private String chequeNo;

    @Column(name = "chequedate")
    private LocalDate chequeDate;

    @Column(name = "bankcashacc", length = 50)
    private String bankCashAcc;

    @Column(name = "payto", length = 150)
    private String payTo;

    @Column(name = "paymentamt", precision = 10, scale = 2)
    private BigDecimal paymentAmt;

    @Column(name = "tdsacc", length = 50)
    private String tdsAcc;

    @Column(name = "receiptamt", precision = 10, scale = 2)
    private BigDecimal receiptAmt;
    
    @Column(name = "tdsamt", precision = 10, scale = 2)
    private BigDecimal tdsAmt;

    @Column(name = "currency", length = 100)
    private String currency;

    @Column(name = "currencyamt", precision = 10, scale = 2)
    private BigDecimal currencyAmt;
    
    @Column(name = "branch", length = 25)
    private String branch;

    @Column(name = "branchcode", length = 20)
    private String branchCode;

    @Column(name = "createdby", length = 25)
    private String createdBy;

    @Column(name = "modifyby", length = 25)
    private String updatedBy;

    @Column(name = "active")
    private boolean active;

    @Column(name = "cancel")
    private boolean cancel;

    @Column(name = "cancelremarks", length = 50)
    private String cancelRemarks;

    @Column(name = "finyear", length = 5)
    private String finYear;

    @Column(name = "screencode", length = 5)
    private String screenCode = "PT";

    @Column(name = "screenname", length = 25)
    private String screenName = "PAYMENT";
	
	@OneToMany(mappedBy = "paymentVO", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<PaymentInvDtlsVO> paymentInvDtlsVO;
	

	@Embedded
	@Builder.Default
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}
