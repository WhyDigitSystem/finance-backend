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
@Table(name = "ccoa")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CCoaVO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "ccoagen")
	@SequenceGenerator(name = "ccoagen", sequenceName = "ccoaseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "ccoaid")
	private Long id;
	
	
	@Column(name = "accountname", length = 150)
	private String accountName;
	
	@Column(name = "accountcode", length = 50)
	private String accountCode;
	
	@Column(name = "createdby", length = 50)
	private String createdBy;
	
	@Column(name = "modifiedby", length = 50)
	private String updatedBy;
	
	@Column(name = "cancelremarks", length = 150)
	private String cancelRemarks;
	
	@Column(name = "currency", length = 50)
	private String currency;
	
	@Column(name = "clientcode", length = 50)
	private String clientCode;
	
	@Column(name = "clientname", length = 100)
	private String clientName;
	
	@Column(name = "natureofaccount", length = 10)
	private String natureOfAccount;
	
	private boolean cancel=false;
	
	private boolean active;
	
	@Column(name = "orgid")
	private Long orgId;

	@Embedded
	@Builder.Default
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
	
}
