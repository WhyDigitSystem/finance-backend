package com.base.basesetup.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.base.basesetup.dto.CreatedUpdatedDate;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="monthlyprocess")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyProcessVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "monthlyprocessgen")
	@SequenceGenerator(name = "monthlyprocessgen", sequenceName = "monthlyprocessseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "monthlyprocessid")
	private Long id;
	
	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "maingroup")
	private String mainGroup;
	@Column(name = "subgroup")
	private String subGroup;
	@Column(name = "subgroupcode")
	private String subGroupCode;
	
	
	
	@Column(name = "month", length = 30)
	private String month; 
	
	@Column(name = "year", length = 10)
	private String year;
	
	@Column(name = "clientcode", length = 150)
	private String clientCode;
	
	@Column(name = "client", length = 150)
	private String client;
	
	@Column(name = "createdby", length = 50)
	private String createdBy;
	
	@Column(name = "modifiedby", length = 50)
	private String updatedBy;
	
	@Column(name = "cancelremarks", length = 150)
	private String cancelRemarks;
	
	private boolean cancel=false;
	
	private boolean active=true;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "monthlyProcessVO", cascade = CascadeType.ALL)
	private List<MonthlyProcessDetailsVO>monthlyProcessDetailsVO;

	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();

}
