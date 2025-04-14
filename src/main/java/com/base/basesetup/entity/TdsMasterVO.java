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
@Table(name = "tdsmaster")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TdsMasterVO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "tdsmastergen")
	@SequenceGenerator(name = "tdsmastergen", sequenceName = "tdsmasterseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "tdsmasterid")
	private Long id;
	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "section")
	private String section;
	@Column(name = "sectionname")
	private String sectionName;
	
	@Column(name = "recievableaccount")
	private String recievableAccount;
	@Column(name = "payableaccount")
	private String payableAccount;
	
	@Builder.Default
	@Column(name = "screencode")
	private String screenCode="TM";
	@Builder.Default
	@Column(name="screenname")
	private String screenName="TDS MASTER";
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedby")
	private String updatedBy;
	@Column(name = "cancelremarks")
	private String cancelRemarks;
	private boolean cancel;
	private boolean active;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "tdsMasterVO", cascade = CascadeType.ALL)
	private List<TdsMaster2VO> tdsMaster2VO;
	

	@Embedded
	@Builder.Default
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}
