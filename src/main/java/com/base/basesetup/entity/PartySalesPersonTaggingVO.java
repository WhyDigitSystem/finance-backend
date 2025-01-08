package com.base.basesetup.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.base.basesetup.dto.CreatedUpdatedDate;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "partysalespersontagging")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartySalesPersonTaggingVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "partysalespersontagginggen")
	@SequenceGenerator(name = "partysalespersontagginggen", sequenceName = "partysalespersontaggingseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "partysalespersontaggingid")
	private Long id;
	@Column(name = "salesperson", length = 150)
	private String salesPerson;

	@Column(name = "empcode", length = 15)
	private String empCode;

	@Column(name = "salesbranch", length = 15)
	private String salesBranch;

	@Column(name = "effectivefrom")
	private LocalDate effectiveFrom;

	@Column(name = "effectivetill")
	private LocalDate effectiveTill;

	@ManyToOne
	@JoinColumn(name = "partymasterid")
	@JsonBackReference
	private PartyMasterVO partyMasterVO;

	@Embedded
	@Builder.Default
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}
