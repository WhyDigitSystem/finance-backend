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
@Table(name="groupmapping")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupMappingVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "groupmappinggen")
	@SequenceGenerator(name = "groupmappinggen", sequenceName = "groupmappingseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "groupmappingid")
	private Long id;
	
	
	@Column(name = "groupname", length = 150)
	private String groupName;
	
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
	
	@OneToMany(mappedBy ="groupMappingVO",cascade =CascadeType.ALL)
	@JsonManagedReference
	private List<SubGroupDetailsVO>  subGroupDetailsVO;
	
	@OneToMany(mappedBy ="groupMappingVO",cascade =CascadeType.ALL)
	@JsonManagedReference
	private List<GroupLedgersVO>  groupLedgresVOs;
	
	

}
