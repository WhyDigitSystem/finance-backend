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
@Table(name="automation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutomationVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "automationgen")
	@SequenceGenerator(name = "automationgen", sequenceName = "automationseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "automationid")
	private Long id;
	
	
	@Column(name = "maingroupname", length = 150)
	private String mainGroupName;
	
	@Column(name = "createdby", length = 50)
	private String createdBy;
	
	@Column(name = "modifiedby", length = 50)
	private String updatedBy;
	
	@Column(name = "cancelremarks", length = 150)
	private String cancelRemarks;
	
	private boolean cancel=false;
	
	private boolean active;
	
	@Column(name = "orgid")
	private Long orgId;
	
	@Column(name="subheading")
	private String subheading;

	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
	
	@OneToMany(mappedBy ="automationVO",cascade =CascadeType.ALL)
	@JsonManagedReference
	private List<AutomationDetailsVO>  automationDetailsVO;

}
