package com.base.basesetup.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.base.basesetup.dto.CreatedUpdatedDate;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "company")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "companygen")
	@SequenceGenerator(name = "companygen", sequenceName = "companyseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "companyid")
	private Long id;

	@Column(name = "companycode")
	private String companyCode;
	@Column(name = "companyname")
	private String companyName;
	@Column(name = "country")
	private String country;
	@Column(name = "currency")
	private String currency;
	@Column(name = "maincurrency")
	private String mainCurrency;
	@Column(name = "address")
	private String address;
	@Column(name = "zipcode")
	private String zip;
	@Column(name = "city")
	private String city;
	@Column(name = "state")
	private String state;
	@Column(name = "phone")
	private String phone;
	@Column(name = "email")
	private String email;
	@Column(name = "website")
	private String webSite;
	@Column(name = "notes")
	private String note;
//	@Column(name = "userd")
//	private String userId;
	@Column(name = "active")
	private boolean active;
//	@Column(unique = true)
//	private String dupchk;
	@Column(name = "employeename")
	private String employeeName;
	@Column(name = "employeecode")
	private String employeeCode;
	@Column(name = "password")
	private String password;
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedby")
	private String updatedBy;
	@Column(name = "cancel")
	private boolean cancel;
	private int role;
	private String ceo;

	private String gst;
	@Column(name = "termsandconditions",length=10000)
	private String termsAndConditions;
	@Column(name = "cin")
	private String cin;
	@Column(name = "panno", length = 10)
    @Size(min = 10, max = 10, message = "PanNo must be exactly 10 characters.")
    private String panNo;
	
	
	@Lob
	@Column(name = "companylogo", columnDefinition = "LONGCLOB")
	private byte[] companyLogo;
	
	@OneToMany(mappedBy = "companyVO", cascade = CascadeType.ALL)
	@JsonManagedReference
	List<BankDetailsVO> bankDetailsVO;



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
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();

}