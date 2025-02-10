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
@Table(name = "chargetyperequest")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChargeTypeRequestVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chargetyperequestgen")
	@SequenceGenerator(name = "chargetyperequestgen", sequenceName = "chargetyperequestseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "chargetyperequestid")
	private Long id;
	@Column(name = "chargetype",length = 30)
	private String chargeType;
	@Column(name = "chargecode",length = 30)
	private String chargeCode;
	@Column(name = "chargedescription",length = 150)
	private String chargeDescription;
	@Column(name = "product",length = 30)
	private String product;
	@Column(name = "localchargedescripition",length = 150)
	private String localChargeDescripition;
	@Column(name = "serviceaccountcode",length = 30)
	private String serviceAccountCode;
	@Column(name = "sacdescripition",length = 50)
	private String sacDescripition;
	@Column(name = "salesaccount",length = 150)
	private String salesAccount;
	@Column(name = "purchaseaccount",length = 150)
	private String purchaseAccount;
	@Column(name = "taxable",length = 30)
	private String taxable;
	@Column(name = "taxablepercentage",length = 5)
	private int taxablePercentage;
	@Column(name = "govtsac",length = 150)
	private String govtSac;
	@Column(name = "excempted",length = 30)
	private String excempted;
	@Column(name = "gsttax",precision = 5,scale = 2)
	private float gstTax;
	@Column(name = "gstcontrol",length = 30)
	private String gstControl;
	@Column(name = "service",length = 150)
	private String service;
	@Column(name = "type",length = 30)
	private String type;
	@Column(name = "approved")
	private boolean approved;

	@Column(name = "active")
	private boolean active;
	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "createdby",length = 30)
	private String createdBy;
	@Column(name = "modifiedby",length = 30)
	private String updatedBy;
	@Column(name = "cancel")
	@Builder.Default
	private boolean cancel=false;
	@Column(name = "cancelremarks")
	private Long cancelRemarks;

	@Embedded
	@Builder.Default
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}
