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
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "qrbarcode")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QrBarCodeVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qrbarcodegen")
	@SequenceGenerator(name = "qrbarcodegen", sequenceName = "qrbarcodeseq", initialValue = 1000000001, allocationSize = 1)

	@Column(name = "qrbarcodeid")
	private Long id;
	@Column(name = "docid")
	private String docId;
	@Column(name = "docdate")
	private LocalDate docDate = LocalDate.now();
	@Column(name = "entryno")
	private String entryNo;
	@Column(name = "count")
	private Long count;
	
	
	@Column(name = "branch",length = 30)
	private String branch;
	@Column(name = "branchcode",length = 10)
	private String branchCode;
	@Column(name = "finyear",length =10)
	private String finYear;
	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedby")
	private String updatedBy;
	@Column(name = "active")
	private boolean active = true;
	@Column(name = "cancel")
	private boolean cancel = false;
	@Column(name = "cancelremarks")
	private String cancelRemarks;
	@Column(name = "screencode",length = 30)
	private String screenCode="QRBAR";
	@Column(name = "screenname",length = 30)
	private String screenName="QRBARCODE";

	@JsonManagedReference
	@OneToMany(mappedBy = "qrBarCodeVO", cascade = CascadeType.ALL)
	private List<QrBarCodeDetailsVO> qrBarCodeDetailsVO;

	@JsonGetter("active")
	public String getActive() {
		return active ? "Active" : "In-Active";
	}

	// Optionally, if you want to control serialization for 'cancel' field similarly
	@JsonGetter("cancel")
	public String getCancel() {
		return cancel ? "T" : "F";
	}

	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();

}
