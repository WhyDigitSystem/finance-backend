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
import com.fasterxml.jackson.annotation.JsonGetter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "hsnsaccode")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HSNSacCodeVO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "hsnsaccodegen")
	@SequenceGenerator(name = "hsnsaccodegen", sequenceName = "hsnsaccodeseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "hsnsaccodeid")
	private Long id;
	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "type")
	private String type;
	@Column(name = "code", length = 10)
	private String code;
	@Column(name = "description", length = 50)
	private String description;
	@Column(name = "taxtype")
	private String taxType;
	@Column(name = "igst", precision = 10, scale = 2)
	private BigDecimal igst;
	@Column(name = "cgst", precision = 10, scale = 2)
	private BigDecimal cgst;

	@Column(name = "sgst", precision = 10, scale = 2)
	private BigDecimal sgst;

	@Column(name = "screencode")
	private String screenCode = "HSC";
	@Column(name = "screenname")
	private String screenName = "HSN SAC CODE";
	@Column(name = "createdby", length = 50)
	private String createdBy;
	@Column(name = "modifiedby", length = 50)
	private String updatedBy;
	@Column(name = "cancelremarks", length = 150)
	private String cancelRemarks;
	@Column(name = "cancel")
	private boolean cancel=false;
	@Column(name = "active")
	private boolean active= true;

	@JsonGetter("active")
	public String getActive() {
		return active ? "Active" : "In-Active";
	}

	@JsonGetter("cancel")
	public String getCancel() {
		return cancel ? "T" : "F";
	}

	@Embedded
	@Builder.Default
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();

}
