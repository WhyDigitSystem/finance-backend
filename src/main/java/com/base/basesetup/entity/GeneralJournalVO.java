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
@Table(name = "generaljournal")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneralJournalVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generaljournalgen")
	@SequenceGenerator(name = "generaljournalgen", sequenceName = "generaljournalseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "generaljournalid")
	private Long id;
	@Column(name = "vouchersubtype", length = 25)
	private String voucherSubType;
	
	@Column(name = "docid", length = 50)
    private String docId;

    @Column(name = "docdate")
    private LocalDate docDate = LocalDate.now();


	@Column(name = "currency", length = 10)
	private String currency;
	@Column(name = "exrate",precision = 10, scale = 2)
	private BigDecimal exRate;
	@Column(name = "refno", length = 50)
	private String refNo;
	@Column(name = "refdate")
	private LocalDate refDate;

	@Column(name = "screencode", length = 5)
	private String screenCode = "GJ";

	@Column(name = "screenname", length = 25)
	private String screenName = "GENERALJOURNAL";

	@Column(name = "remarks", length = 150)
	private String remarks;

	@Column(name = "totaldebitamount",precision = 10, scale = 2)
	private BigDecimal totalDebitAmount;
	@Column(name = "totalcreditamount",precision = 10, scale = 2)
	private BigDecimal totalCreditAmount;

	@Column(name = "branch", length = 25)
	private String branch;

	@Column(name = "branchcode", length = 20)
	private String branchCode;

	@Column(name = "finyear", length = 5)
	private String finYear;

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

	@Column(name = "status", length = 50)
	private String status;
	
	@Column(name = "orgid")
	private Long orgId;

	@OneToMany(mappedBy = "generalJournalVO", cascade = CascadeType.ALL)
	@JsonManagedReference
	List<ParticularsJournalVO> particularsJournalVO;

	@JsonGetter("active")
	public String getActive() {
		return active ? "Active" : "In-Active";
	}
	
	@Embedded
	@Builder.Default
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}
