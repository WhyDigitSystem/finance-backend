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
@Table(name = "clientcompany")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientCompanyVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clientcompanygen")
	@SequenceGenerator(name = "clientcompanygen", sequenceName = "clientcompanyseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "clientcompanyid")
	private Long id;
	
	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "name" , length = 100)
	private String clientName;
	@Column(name = "clientcode",length = 10)
	private String clientCode; 
	@Column(name = "phone",length = 25)
	private String phone;
	@Column(name = "email",length = 100)
	private String email;
	@Column(name = "website",length = 100)
	private String webSite;
	@Column(name = "active")
	private boolean active;
	@Column(name = "createdby",length = 50)
	private String createdBy;
	@Column(name = "modifiedby",length = 50)
	private String updatedBy;
	@Column(name = "cancel")
	private boolean cancel;
	@Column(name = "clientyear",length = 50)
	private String clientYear;
	
	@Column(name = "clientGLCode",length = 50)
	private String clientGLCode;
	@Column(name = "startno")
	private Long startNo;
	@Column(name = "currency",length = 50)
	private String currency;
	@Column(name = "yearstartdate")
	private LocalDate yearStartDate;
	@Column(name = "yearenddate")
	private LocalDate yearEndDate;
	
	@Column(name = "bussinesstype",length = 255)
	private String bussinessType;
	
	@Column(name = "turnover",length = 100)
	private String turnOver;
	
	@Column(name = "levelofservice",length = 100)
	private String levelOfService;
	
	@Column(name = "repperson",length = 100)
	private String repPerson;
	
	@Column(name = "username",length = 100)
	private String userName;
	
	@Column(name = "password")
	private String password;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "clientCompanyVO", cascade = CascadeType.ALL)
	private List<ClientCompanyReportAccessVO>clientCompanyReportAccessVO;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "clientCompanyVO", cascade = CascadeType.ALL)
	private List<ClientUnitVO>clientUnitVO;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "clientCompanyVO", cascade = CascadeType.ALL)
	private List<ClientSegmentVO>clientSegmentVO;

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
