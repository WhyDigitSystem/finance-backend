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
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "taxmaster")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxMasterVO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "taxmastergen")
	@SequenceGenerator(name = "taxmastergen", sequenceName = "taxmasterseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "taxmasterid")
	private Long taxMasterId;
	@Column(name = "orgid", length=20)
	private Long orgId;
	@Column(name = "taxtype", length=10)
	private String taxType;
	@Column(name = "taxpercentage", precision = 2,scale = 2)
	private float taxPercentage;
	@Column(name = "taxdescription", length=100)
	private String taxDescription;
	
	@Column(name = "createdby", length=30)
	private String createdBy;
	@Column(name = "updatedby", length=30)
	private String updatedBy;
	@Column(name = "cancelremarks", length=50)
	private String cancelRemarks;
	private boolean cancel;
	private boolean active;

	

	@Embedded
	@Builder.Default
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}
