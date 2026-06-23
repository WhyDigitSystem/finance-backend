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
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonManagedReference;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "quotationnew")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuotationNewVO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "quotationnewgen")
	@SequenceGenerator(name = "quotationnewgen", sequenceName = "quotationnewseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "quotationnewid")
	private Long id;

	@Column(name ="quotationto")
	private String quotationTo;
	private String code;
	@Column(name = "shippingaddress")
	private String shippingAddress;
	@Column(name = "customeraddress")
	private String customerAddress;
	private boolean active;
	private boolean cancel;
	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedy")
	private String updatedBy;
	@Column(name = "cancelremarks")
	private String cancelRemarks;

	//private String prefix="WDS";

//	private LocalDate date1=LocalDate.now();
//	@Column(name = "finyear")
//	private String finYear;

	@OneToMany(mappedBy = "quotationNewVO", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<QuotationDetailsNewVO> quotationDetailsVO;

	@JsonGetter("active")
	public String getActive() {
		return active ? "Active" : "In-Active";
	}

	// Optionally, if you want to control serialization for 'cancel' field similarly
	@JsonGetter("cancel")
	public String getCancel() {
		return cancel ? "T" : "F";
	}

	@Embedded
	@Builder.Default
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();


}
