package com.base.basesetup.entity;

import java.math.BigDecimal;
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
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "quotation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuotationVO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "quotationgen")
	@SequenceGenerator(name = "quotationgen", sequenceName = "quotationseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "quotationid")
	private Long id;

	@Column(name ="quotationno")
	private String quotationNo;
	@Column(name ="quotationdate")
	private LocalDate quotationDate = LocalDate.now();
	private String code;
	@Column(name = "deliverygaddress")
	private String deliveryAddress;
	@Column(name = "customeraddress")
	private String customerAddress;
	private boolean active;
	private boolean cancel;
	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "finyear")
	private String finYear;
	@Column(name = "companyaddress")
	private String companyAddress;
	
	@Column(name = "customername")
	private String customerName;
	
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedy")
	private String updatedBy;
	@Column(name = "cancelremarks")
	private String cancelRemarks;
	
	@Column(name="totalamount",precision = 10, scale = 2)
    private BigDecimal totalAmount;
	
	@Column(name="subtotal",precision = 10, scale = 2)
    private BigDecimal subTotal;


	//private String prefix="WDS";

//	private LocalDate date1=LocalDate.now();
//	@Column(name = "finyear")
//	private String finYear;

	@OneToMany(mappedBy = "quotationVO", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<QuotationDetailsVO> quotationDetailsVO;

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

