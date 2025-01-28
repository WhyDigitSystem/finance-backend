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
@Table(name = "taxmaster")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxMasterVO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "taxmastergen")
	@SequenceGenerator(name = "taxmastergen", sequenceName = "taxmasterseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "taxmasterid")
	private Long id;
	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "finyear")
	private String finYear;
	@Column(name = "serviceaccountcode")
	private String serviceAccountCode;
	@Column(name = "gst")
	private String gst;
	@Column(name = "gstslab")
	private Long gstSlab;

	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedby")
	private String updatedBy;
	@Column(name = "cancelremarks")
	private String cancelRemarks;
	@Column(name = "cancel")
	private boolean cancel;
	@Column(name = "active")
	private boolean active;

	@OneToMany(mappedBy = "taxMasterVO", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<TaxMasterDetailsVO> taxMasterDetailsVO;

	@Embedded
	@Builder.Default
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}
