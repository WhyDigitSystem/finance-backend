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
@Table(name = "adjustmentjournal")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdjustmentJournalVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "adjustmentjournalgen")
	@SequenceGenerator(name = "adjustmentjournalgen", sequenceName = "adjustmentjournalseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "adjustmentjournalid")
	private Long id;

	@Column(name = "adjustmenttype", length = 25)
	private String adjustmentType;

	@Column(name = "docid", length = 50)
	private String docId;

	@Column(name = "docdate")
	private LocalDate docDate = LocalDate.now();

	@Column(name = "currency", length = 10)
	private String currency;
	@Column(name = "exrate", precision = 10, scale = 2)
	private BigDecimal exRate;
	@Column(name = "refno", length = 50)
	private String refNo;
	@Column(name = "refdate")
	private LocalDate refDate;
	@Column(name = "supprefno", length = 50)
	private String suppRefNo;
	@Column(name = "supprefdate")
	private LocalDate suppRefDate;

	@Column(name = "screencode", length = 5)
	private String screenCode = "AJ";

	@Column(name = "screenname", length = 25)
	private String screenName = "ADJUSTMENTJOURNAL";

	@Column(name = "remarks", length = 150)
	private String remarks;

	@Column(name = "totaldebitamount", precision = 10, scale = 2)
	private BigDecimal totalDebitAmount;
	@Column(name = "totalcreditamount", precision = 10, scale = 2)
	private BigDecimal totalCreditAmount;

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
	@Column(name = "status", length = 50)
	private String status;

	@Column(name = "cancelremarks", length = 50)
	private String cancelRemarks;

	@Column(name = "orgid")
	private Long orgId;

	@OneToMany(mappedBy = "adjustmentJournalVO", cascade = CascadeType.ALL)
	@JsonManagedReference
	List<AccountParticularsVO> accountParticularsVO;

	@Embedded
	@Builder.Default
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();

}
