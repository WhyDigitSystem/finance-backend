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
@Table(name = "groupledger")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupLedgerVO {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "groupledgergen")
	@SequenceGenerator(name = "groupledgergen", sequenceName = "groupledgerseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "groupledgerid")
	private Long id;
	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "groupname", length = 150)
	private String groupName;
	@Column(name = "gsttaxflag", length = 50)
	private String gstTaxFlag;
	@Column(name = "coalist", length = 50)
	private String coaList;
	@Column(name = "accountgroupname", length = 150)
	private String accountGroupName;
	@Column(name = "accountcode", length =50)
	private String accountCode;
	@Column(name = "type", length = 50)
	private String type;
	@Column(name = "gsttype", length = 50)
	private String gstType;
	@Column(name = "gstpercentage", precision = 3,scale = 2)
	private double gstPercentage;
	@Column(name = "interbranchac")
	private boolean interBranchAc;
	@Column(name = "controllac")
	private boolean controllAc;
	@Column(name = "category", length = 50)
	private String category;
	@Column(name = "currency", length = 50)
	private String currency;
	@Column(name = "createdby", length = 50)
	private String createdBy;
	@Column(name = "modifiedby", length = 50)
	private String updatedBy;
	@Column(name = "cancelremarks", length = 150)
	private String cancelRemarks;
	private boolean cancel;
	private boolean active;
	@Column(name = "natureofaccount", length = 50)
	private String natureOfAccount;
	@Column(name = "pbflag", length = 15)
	private String pBFlag;
	@Column(name = "parentcode", length = 50)
	private String parentCode;

	@Embedded
	@Builder.Default
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}