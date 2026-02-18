package com.base.basesetup.entity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.base.basesetup.dto.CreatedUpdatedDate;
import com.fasterxml.jackson.annotation.JsonGetter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "organization")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationVO{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "organizationgen")
	@SequenceGenerator(name = "organizationgen", sequenceName = "organizationseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "organizationid")
	private Long id;
	@Column(name = "orgname")
	private String organizationName;
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedby")
	private String updatedBy;
	@Column(name = "orgmail")
	private String organizationMail;
	@Column(name = "phoneno")
	private String phoneNo;
	private String state;
	private String city;
	private String country;
	@Column(name = "pincode")
	private String pinCode;
	private String address;
	private boolean active;

	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();

//	@OneToMany(mappedBy = "organizationVO", cascade = CascadeType.ALL)
//	@JsonManagedReference
//	private List<ClientVO> clientVO;
	
	
	
	@JsonGetter("active")
	public String getActive() {
		return active ? "Active" : "In-Active";
		
		
		
	}
}
