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
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "contravoucher")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContraVoucherVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contravouchergen")
	@SequenceGenerator(name = "contravouchergen", sequenceName = "contravoucherseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "contravoucherid")
	private Long id;

    @Column(name = "docid", length = 50)
    private String docId;

    @Column(name = "docdate")
    private LocalDate docDate= LocalDate.now();
    
    @Column(name = "currency", length = 10)
	private String currency;
    
	@Column(name = "exrate",precision = 10, scale = 6)
	private BigDecimal exRate;

    @Column(name = "referenceno", length = 75)
    private String referenceNo;

    @Column(name = "referencedate")
    private LocalDate referenceDate;
    
    @Column(name = "chequeno", length = 50)
    private String chequeNo;

    @Column(name = "chequedate")
    private LocalDate chequeDate;

    @Column(name = "totaldebitamount", precision = 10, scale = 2)
    private BigDecimal totalDebitAmount;

    @Column(name = "totalcreditamount", precision = 10, scale = 2)
    private BigDecimal totalCreditAmount;

    @Column(name = "remarks")
    private String remarks;
    
	@Column(name = "branch", length = 25)
	private String branch;

	@Column(name = "branchcode", length = 20)
	private String branchCode;

	@Column(name = "finyear", length = 5)
	private String finYear;

	@Column(name = "createdby", length = 25)
	private String createdBy;

	@Column(name = "modifyby", length = 25)
	private String updatedBy;

	@Column(name = "active")
	private boolean active;

	@Column(name = "cancel")
	private boolean cancel;

	@Column(name = "cancelremarks", length = 150)
	private String cancelRemarks;

	@Column(name = "orgid")
	private Long orgId;
	
	@Column(name = "screencode", length = 5)
	private String screenCode = "CV";

	@Column(name = "screenname", length = 25)
	private String screenName = "CONTRAVOUCHER";
    
	@OneToMany(mappedBy = "contraVoucherVO", cascade = CascadeType.ALL)
	@JsonManagedReference
	List<ContraVoucherParticularsVO> contraVoucherParticularsVO;
    
	@JsonGetter("active")
	public String getActive() {
		return active ? "Active" : "In-Active";
	}
    
    @Embedded
	@Builder.Default
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}
