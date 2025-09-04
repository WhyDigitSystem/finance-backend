package com.base.basesetup.entity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.base.basesetup.dto.CreatedUpdatedDate;
import com.fasterxml.jackson.annotation.JsonGetter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "singleqrbarcode")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SingleQrBarCodeVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "singleqrbarcodegen")
	@SequenceGenerator(name = "singleqrbarcodegen", sequenceName = "singleqrbarcodeseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "singleqrbarcodeid")
	private Long id;
	@Column(name = "qrbarcodevalue")
	private String qrBarCodeValue;
	@Column(name = "count")
	private Long count;

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
	
	
	@JsonGetter("active")
	public String getActive() {
		return active ? "Active" : "In-Active";
	}

	@JsonGetter("cancel")
	public String getCancel() {
		return cancel ? "T" : "F";
	}
	
	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();

}
