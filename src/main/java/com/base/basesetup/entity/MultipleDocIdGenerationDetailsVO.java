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
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "multipledocidgendetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MultipleDocIdGenerationDetailsVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "multipledocidgendetailsgen")
	@SequenceGenerator(name = "multipledocidgendetailsgen", sequenceName = "multipledocidgendetailsseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "multipledocidgendetailsid")
	private Long id;
	
	@Column(name = "screencode",length =10)
	private String screenCode;
	
	@Column(name = "screenname",length =50)
	private String screenName;
	
	@Column(name = "sourcescreencode",length =10)
	private String sourceScreenCode;
	
	@Column(name = "sourcescreenname",length =50)
	private String sourceScreenName;
	
	@Column(name = "sourcedoccode",length =25)
	private String sourceDocCode;
	
	@Column(name = "doccode",length =25)
	private String docCode;
	
	@Column(name = "branch",length =25)
	private String branch;
	
	@Column(name = "branchcode",length =25)
	private String branchCode;
	
	@Column(name = "prefixfield",length =25)
	private String prefixField;
	
	@Column(name = "finyear",length =10)
	private String finYear;
	
	
	@Column(name = "finyearidentifier",length =50)
	private String finYearIdentifier;
	
	@Column(name = "orgid")
	private Long orgId;
	
	@Column(name = "concatenation",length =25)
	private String concatenation;
	
	@Column(name="lastno")
	@Builder.Default
	private int lastno=1;
	

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "multipledocidgenerationid")
	private MultipleDocIdGenerationVO multipleDocIdGenerationVO;
}
