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
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stockbranch")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockBranchVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stockbranchgen")
	@SequenceGenerator(name = "stockbranchgen", sequenceName = "stockbranchseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "stockbranchid")
	private Long id;

	@Column(name = "branch")
	private String branch;
	@Column(name = "branchcode")
	private String branchCode;
	@Column(name = "orgid", length = 15)
	private Long orgId;
	@Column(name = "active")
	private boolean active;
	@Column(name = "modifiedby", length = 25)
	private String updatedBy;
	@Column(name = "createdby", length = 25)
	private String createdBy;
	@Column(name = "cancel")
	private boolean cancel;
	@Column(name = "cancelremarks", length = 25)
	private String cancelRemarks;


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
	@Builder.Default
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}
