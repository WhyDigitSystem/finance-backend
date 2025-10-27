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
@Table(name = "ledgermapping")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LedgerMappingVO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "ledgermappinggen")
	@SequenceGenerator(name = "ledgermappinggen", sequenceName = "ledgermappingseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "ledgermappingid")
	private Long id;

	@Column(name = "clientcoa")
	private String clientCoa;
	@Column(name = "clientcoacode")
	private String clientCoaCode;
	
	@Column(name = "coa")
	private String coa;
	
	@Column(name = "coacode")
	private String coaCode;
	
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "updatedby")
	private String updatedBy;
	@Column(name = "cancelremarks")
	private String cancelRemarks;
	private boolean active;
	private boolean cancel;
	@Column(name = "clientcode")
	private String clientCode;
	@Column(name = "clientname")
	private String clientName;
	
	@Column(name = "orgid")
	private Long orgId;

	@Embedded
	@Builder.Default
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();

}
