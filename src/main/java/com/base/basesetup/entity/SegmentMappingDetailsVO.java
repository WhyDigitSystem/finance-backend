package com.base.basesetup.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "segmentmappingdetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SegmentMappingDetailsVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "segmentmappingdetailsgen")
	@SequenceGenerator(name = "segmentmappingdetailsgen", sequenceName = "segmentmappingdetailsseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "segmentmappingdetailsid")
	private Long id;
	private String value;
	private boolean active;
	
	@ManyToOne
	@JoinColumn(name = "segmentmappingid")
	@JsonBackReference
	private SegmentMappingVO segmentMappingVO;

}
