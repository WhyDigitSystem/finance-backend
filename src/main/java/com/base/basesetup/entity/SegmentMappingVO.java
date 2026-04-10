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
@Table(name="segmentmapping")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class SegmentMappingVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "segmentmappinggen")
	@SequenceGenerator(name = "segmentmappinggen", sequenceName = "segmentmappingseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "segmentmappingid")
	private Long id;
	@Column(name="clientcode",length = 50)
	private String clientCode;
	@Column(name="clientname",length = 100)
	private String clientName;
	@Column(name="orgid",length = 50)
	private Long orgId;
	@Column(name="createdby",length = 50)
	private String createdBy;
	@Column(name="modifiedby",length = 50)
	private String updatedBy;
	private boolean cancel = false;
	private boolean active;
	
	@Column(name = "segmenttype")
	private String segmentType;
	
	
	@OneToMany(mappedBy ="segmentMappingVO",cascade =CascadeType.ALL)
	@JsonManagedReference
	private List<SegmentMappingDetailsVO> segmentMappingDetailsVO;
	

	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();

}
