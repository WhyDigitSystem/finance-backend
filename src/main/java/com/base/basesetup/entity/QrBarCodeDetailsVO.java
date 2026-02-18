package com.base.basesetup.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="qrbarcodedetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QrBarCodeDetailsVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qrbarcodedetailsgen")
	@SequenceGenerator(name = "qrbarcodedetailsgen", sequenceName = "qrbarcodedetailsseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "qrbarcodedetailsid")
	private Long id;
	
	@Column(name = "partno")
	private String partNo;
	@Column(name = "partdescription")
	private String partDescription;
	@Column(name = "batchno")
	private String batchNo;
	@Column(name = "barcodevalue")
	private String barCodeValue;
	@Column(name = "qrcodevalue")
	private String qrCodeValue;
	
	
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "qrbarcodeid")
	private QrBarCodeVO qrBarCodeVO;

}
