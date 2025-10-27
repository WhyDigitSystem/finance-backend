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
@Table(name="subgroup")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubGroupDetailsVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "subgroupgen")
	@SequenceGenerator(name = "subgroupgen", sequenceName = "subgroupseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "subgroupid")
	private Long id;
	
	
	@Column(name = "accountcode", length = 150)
	private String accountCode;
	
	@Column(name = "accountname", length = 150)
	private String accountName;
	
	private boolean active;
	
	@Column(name = "displayseq")
	private String displaySeq;
	
	
	@ManyToOne
	@JoinColumn(name = "groupmappingid")
	@JsonBackReference
	private GroupMappingVO groupMappingVO;

}
