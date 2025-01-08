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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "costcenter")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class CostCenterVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "costcentergen")
	@SequenceGenerator(name = "costcentergen", sequenceName = "costcenterseq", initialValue = 1000000001, allocationSize = 1)
	
	@Column(name = "costcenterid")
	private Long id;
	@Column(name = "dimensiontype")
	private String dimensionType;
	@Column(name = "valuecode")
	private String valueCode;
	@Column(name = "valuedescription")
	private String valueDescription;
	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "active")
	private boolean active;
	@Column(name = "cancel")
	private String cancel;
	@Column(name = "cancelremarks")
	private String cancelRemarks;
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedby")
	private String updatedBy;

	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}
