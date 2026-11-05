package com.base.basesetup.entity;

import java.math.BigDecimal;

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
@Data
@Table(name = "tolerance")
@AllArgsConstructor
@NoArgsConstructor
public class ToleranceMasterVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tolerancemastergen")
	@SequenceGenerator(name = "tolerancemastergen", sequenceName = "tolerancemasterseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "toleranceid")
	private Long toleranceid;

	@Column(name = "lt", precision = 15, scale = 4)
	private BigDecimal lowerTolerance;

	@Column(name = "ut", precision = 15, scale = 4)
	private BigDecimal upperTolerance;

	@Column(name = "tol", precision = 15, scale = 4)
	private BigDecimal tolerance;

	@Column(name = "orgid")
	private Long orgid;

	@Column(name = "branch")
	private String branch;
	
	@Column(name = "branchcode")
	private String branchCode;

	@Column(name = "active")
	private boolean active;

	@Column(name = "createdby", length = 50)
	private String createdBy;

	@Column(name = "modifiedby", length = 50)
	private String updatedBy;

	@Column(name = "cancel")
	private boolean cancel;

	@Column(name = "screenid", length = 10)
	private String screenCode;

	@Column(name = "screen", length = 50)
	private String screenName;
	

	@Embedded
	private CreatedUpdatedDate createdUpdatedDate = new CreatedUpdatedDate();

}
