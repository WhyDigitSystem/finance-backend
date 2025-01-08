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
@Table(name = "multipledocidgeneration")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultipleDocIdGenerationVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "multipledocidgenerationgen")
	@SequenceGenerator(name = "multipledocidgenerationgen", sequenceName = "multipledocidgenerationseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "multipledocidgenerationid")
	private Long id;
	
	@Column(name = "screencode",length =10)
	private String screenCode;
	
	@Column(name = "screenname",length =50)
	private String screenName;
	
	@Column(name = "doccode",length =10)
	private String docCode;
	
	@Column(name = "branch",length =25)
	private String branch;
	
	@Column(name = "branchcode",length =25)
	private String branchCode;
	
	@Column(name = "finyear",length =10)
	private String finYear;
	
	
	@Column(name = "finyearidentifier",length =50)
	private String finYearIdentifier;
	
	@Column(name = "orgid")
	private Long orgId;
	
	@Column(name="createdby",length =25)
	private String createdBy;
	
	@Column(name="modifiedby",length =25)
	private String updatedBy;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "multipleDocIdGenerationVO", cascade = CascadeType.ALL)
	private List<MultipleDocIdGenerationDetailsVO> multipleDocIdGenerationDetailsVO;
	
	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}
