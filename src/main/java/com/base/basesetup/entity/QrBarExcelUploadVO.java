package com.base.basesetup.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "qrbarexcelupload")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QrBarExcelUploadVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qrbarexceluploadgen")
	@SequenceGenerator(name = "qrbarexceluploadgen", sequenceName = "qrbarexceluploadseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "qrbarexceluploadid")
	private Long id;
	@Column(name = "entryno")
	private String entryNo;
	@Column(name = "partno")
	private String partNo;
	@Column(name = "partdescription")
	private String partDescription;
	@Column(name = "batchno")
	private String batchNo;
	
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedby")
	private String updatedBy;
	@Column(name = "active")
	private boolean active = true;
	@Column(name = "cancel")
	private boolean cancel = false;
	@Column(name = "cancelremarks")
	private String cancelRemarks;

}
