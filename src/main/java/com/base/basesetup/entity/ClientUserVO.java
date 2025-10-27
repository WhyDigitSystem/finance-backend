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
import com.base.basesetup.dto.Role;
import com.fasterxml.jackson.annotation.JsonGetter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clientuser")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientUserVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clientusergen")
	@SequenceGenerator(name = "clientusergen", sequenceName = "clientuserseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "clientuserid")
	private Long id;
	@Column(name = "clientuser")
	private String clientUser;
	@Column(name = "clientcode")
	private String clientUserCode;
	@Column(name = "clintuseradminname")
	private String clintUserAdminName;
	@Column(name = "clientusertype")
	private String clientUserType;
	@Column(name = "clientusermail")
	private String clientUserMail;
	@Column(name = "phoneno")
	private String phoneNo;
	@Column(name = "conntactperson")
	private String conntactPerson;
	@Column(name = "username")
	private String userName;
	private String password;
	private boolean active;
	private boolean cancel;
	@Column(name ="orgid")
	private Long orgId;
	@Column(name ="createdby")
	private String createdBy;
	@Column(name ="modifiedby")
	private String updatedBy;
	private Role role;

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
