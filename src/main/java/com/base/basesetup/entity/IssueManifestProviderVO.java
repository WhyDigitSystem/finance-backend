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
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mim")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueManifestProviderVO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "mimgen")
	@SequenceGenerator(name = "mimgen", sequenceName = "mimseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "mimid")
	private Long id;
	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "finyear")
	private String finYear;
	@Column(name = "transactionno")
	private String transactionNo;
	@Column(name = "transactiondate")
	private LocalDate transactionDate;
	@Column(name = "dispatchdate")
	private LocalDate dispatchDate;
	@Column(name = "transactiontype")
	private String transactionType;
	@Column(name = "fromwarehouse")
	private String fromWarehouse;
	@Column(name = "warehouseaddress")
	private String warehouseAddress;
	@Column(name = "sender")
	private String sender;
	@Column(name = "senderaddress")
	private String senderAddress;
	@Column(name = "receiver")
	private String receiver;
	@Column(name = "receiveraddress")
	private String receiverAddress;
	@Column(name = "receivergst")
	private String receiverGst;
	@Column(name = "Amountinwords")
	private String amountInWords;
	@Column(name = "amount")
	private Long amount;
	@Column(name = "transportername")
	private String transporterName;
	@Column(name = "vehicleno")
	private String vehicleNo;
	@Column(name = "driverphoneno")
	private String driverPhoneNo;
//	@Column(name = "declaration")
//	private String declaration;
//	@Column(name = "notes")
//	private String notes;
	@Column(name = "active")
	private boolean active;
	@Column(name = "cancel")
	private boolean cancel;
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedby")
	private String updatedBy;
	@Column(name ="declaration",length = 1000)
	private String declaration;
	@Column(name ="note1",length = 1000)
	private String note1;
	@Column(name ="note1bold",length = 1000)
	private String note1Bold;
	@Column(name ="note2",length = 1000)
	private String note2;
	@Column(name = "receivername")
	private String receiverName;


	@JsonGetter("active")
	public String getActive() {
		return active ? "Active" : "In-Active";
	}

	// Optionally, if you want to control serialization for 'cancel' field similarly
	@JsonGetter("cancel")
	public String getCancel() {
		return cancel ? "T" : "F";
	}
	
	@OneToMany(mappedBy ="issueManifestProviderVO",cascade =CascadeType.ALL)
	@JsonManagedReference
	private List<IssueManifestProviderDetailsVO> issueManifestProviderDetailsVOs;
	
	

	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}
