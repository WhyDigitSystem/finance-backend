package com.base.basesetup.entity;

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
@Table(name = "reconcilecorpbank")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReconcileCorpBankVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reconcilecorpbankgen")
	@SequenceGenerator(name = "reconcilecorpbankgen", sequenceName = "reconcilecorpbankseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "reconcilecorpbankid")
	private Long id;
	@Column(name = "docid",length = 25)
	private String docId;
	@Column(name = "docdate")
	private LocalDate docDate= LocalDate.now();
	@Column(name="bankaccount",length = 30)
	private String bankAccount;
	@Column(name = "bankstmtdate")
	private LocalDate bankStmtDate;
	@Column(name="remarks",length = 150)
	private String remarks;
	
	@Builder.Default
	@Column(name = "screencode")
	private String screenCode="RC";
	@Builder.Default
	@Column(name="screenname")
	private String screenName="RECONCILE CORP";
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

	@OneToMany(mappedBy = "reconcileCorpBankVO",cascade = CascadeType.ALL)
	@JsonManagedReference
	List<ParticularsReconcileCorpBankVO> particularsReconcileCorpBankVO;
	
	@Embedded
	@Builder.Default
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();

}
