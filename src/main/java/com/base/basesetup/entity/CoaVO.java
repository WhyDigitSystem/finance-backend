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
@Table(name = "coa")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoaVO {//coaVO


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "coagen")
	@SequenceGenerator(name = "coagen", sequenceName = "coaseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "coaid")
	private Long id;
	
	@Column(name = "type", length = 30)
	private String type; 
	
	@Column(name = "groupname", length = 150)
	private String groupName;
	
	@Column(name = "accountgroupname", length = 150)
	private String accountGroupName;
	
	@Column(name = "natureofaccount", length = 50)
	private String natureOfAccount;
	
	@Column(name = "parentid")
	private String parentId;
	
	@Column(name = "parentcode",length =10)
	private String parentCode;
	
	@Column(name = "accountcode", length = 10)
	private String accountCode;
	
	@Column(name = "createdby", length = 50)
	private String createdBy;
	
	@Column(name = "modifiedby", length = 50)
	private String updatedBy;
	
	@Column(name = "cancelremarks", length = 150)
	private String cancelRemarks;
	
	@Column(name = "interbranchac")
	private boolean interBranchAc;
	
	@Column(name = "controllac")
	private boolean controllAc;
	
	@Column(name = "currency", length = 50)
	private String currency;
	
	private boolean cancel=false;
	
	private boolean active;
	
	@Column(name = "orgid")
	private Long orgId;

	@Embedded
	@Builder.Default
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}
