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
@Table(name="groupledgers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupLedgersVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "groupledgersgen")
	@SequenceGenerator(name = "groupledgersgen", sequenceName = "groupledgersseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "groupledgersid")
	private Long id;
	
	@Column(name="groupname")
	private String mainGroupName;
	
	@Column(name = "subgroupcode", length = 150)
	private String parentCode;
	
	@Column(name = "subgroupname", length = 150)
	private String groupName;
	
	@Column(name = "accountcode", length = 150)
	private String accountCode;
	
	@Column(name = "accountname", length = 150)
	private String accountName;
	
	@Column(name = "displayseq")
	private String displaySeq;
	
	@Column(name="orgid")
	private Long orgId;
	
	private boolean active;
	
	
	@ManyToOne
	@JoinColumn(name = "groupmappingid")
	@JsonBackReference
	private GroupMappingVO groupMappingVO;

}
