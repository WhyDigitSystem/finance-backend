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
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "servicelevel")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceLevelVO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "serviceLevelgen")
	@SequenceGenerator(name = "serviceLevelgen", sequenceName = "serviceLevelseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "servicelevelid")
	private Long id;
	@Column(name = "levelcode")
	private String levelCode;
	@Column(name = "levelname")
	private String levelName;
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifyedby")
	private String updatedBy;
	@Column(name = "orgid")
	private Long orgId;

	private boolean active;

	private boolean cancel;
	
	@OneToMany(mappedBy ="serviceLevelVO",cascade =CascadeType.ALL)
	@JsonManagedReference
	private List<ServiceLevelDetailsVO> serviceLevelDetailsVO;

	@Embedded
	@Builder.Default
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();

}
