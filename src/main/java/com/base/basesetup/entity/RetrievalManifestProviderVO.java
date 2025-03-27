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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.base.basesetup.dto.CreatedUpdatedDate;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rim")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetrievalManifestProviderVO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "rimgen")
	@SequenceGenerator(name = "rimgen", sequenceName = "rimseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "rimid")
	private Long id;
	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "transactionno")
	private String transactionNo;
	@Column(name = "transactiondate")
	private LocalDate transactionDate;
	@Column(name = "dispatchdate")
	private LocalDate dispatchDate;
	@Column(name = "transactiontype")
	private String transactionType;
	@Column(name = "sender")
	private String sender;
	@Column(name = "senderaddress")
	private String senderAddress;
	@Column(name = "Receiver")
	private String receiver;
	@Column(name = "receiveraddress")
	private String receiverAddress;
	@Column(name = "sendergst")
	private String senderGst;
	@Column(name = "transportername")
	private String transporterName;
	@Column(name = "vehicleeno")
	private String vehicleeNo;
	@Column(name = "driverphoneno")
	private String driverPhoneNo;
	@Column(name = "declaration")
	private String declaration;
	@Column(name = "notes")
	private String notes;
	@Column(name = "active")
	private boolean active;
	@Column(name = "cancel")
	private boolean cancel;
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedby")
	private String updatedBy;

	@JsonGetter("active")
	public String getActive() {
		return active ? "Active" : "In-Active";
	}

	// Optionally, if you want to control serialization for 'cancel' field similarly
	@JsonGetter("cancel")
	public String getCancel() {
		return cancel ? "T" : "F";
	}
	
	@OneToMany(mappedBy ="retrievalManifestProviderVO",cascade =CascadeType.ALL)
	@JsonManagedReference
	private List<RetrievalManifestProviderDetailsVO> retrievalManifestProviderDetailsVOs;
	
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name="rimdeclarationid")
	private RimDeclarationVO rimDeclarationVO;

	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}


