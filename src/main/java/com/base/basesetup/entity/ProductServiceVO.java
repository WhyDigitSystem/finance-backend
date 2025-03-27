package com.base.basesetup.entity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.base.basesetup.dto.CreatedUpdatedDate;
import com.fasterxml.jackson.annotation.JsonGetter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "productservice")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductServiceVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "productservicegen")
	@SequenceGenerator(name = "productservicegen", sequenceName = "productserviceseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "productserviceid")
	private Long id;
	@Column(name = "type")
	private String type;
	@Column(name = "code")
	private String code;
	@Column(name = "name")
	private String name;
	@Column(name = "description")
	private String description;
	@Column(name = "dimension")
	private String dimension;

	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedby")
	private String updatedBy;
	@Column(name = "active")
	private boolean active;
	@Column(name = "cancel")
	private boolean cancel;
	
	@Lob
	@Column(name = "image", columnDefinition = "LONGBLOB") // Ensure the column is LONGBLOB
	private byte[] image;
	

	@JsonGetter("active")
	public String getActive() {
		return active ? "Active" : "In-Active";
	}

	@JsonGetter("cancel")
	public String getCancel() {
		return cancel ? "T" : "F";
	}

	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();

}
