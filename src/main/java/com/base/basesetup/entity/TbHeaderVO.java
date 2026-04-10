package com.base.basesetup.entity;

import java.time.LocalDate;
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
@Table(name = "tbheader")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class TbHeaderVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "tbheadergen")
	@SequenceGenerator(name = "tbheadergen", sequenceName = "tbheaderseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "tbheaderid")
	private Long id;
	@Column(name="docid",length = 50)
	private String docId;
	@Column(name="docdate")
	private LocalDate docDate=LocalDate.now();
	@Column(name="clientcode",length = 50)
	private String clientCode;
	@Column(name="client",length = 100)
	private String client;
	@Column(name="orgid",length = 50)
	private Long orgId;
	
	@Column(name="screencode",length = 10)
	private String screenCode="TB";
	@Column(name="screenname",length = 50)
	private String screenName="TRIAL BALANCE";
	@Column(name="tbmonth",length = 50)
	private String tbMonth;
	@Column(name="finyear",length = 10)
	private String finYear;
	@Column(name="createdby",length = 50)
	private String createdBy;
	@Column(name="modifiedby",length = 50)
	private String updatedBy;
	private boolean cancel = false;
	private boolean active=true;
	
	
	@OneToMany(mappedBy ="tbHeaderVO",cascade =CascadeType.MERGE)
	@JsonManagedReference
	private List<TbDetailsVO> tbDetailsVO;
	

	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
	
}
