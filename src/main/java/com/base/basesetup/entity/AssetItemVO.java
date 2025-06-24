package com.base.basesetup.entity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.base.basesetup.dto.CreatedUpdatedDate;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "assetitem")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetItemVO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "assetitemgen")
	@SequenceGenerator(name = "assetitemgen", sequenceName = "assetitemseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name="assetitemid")
	private Long id;
	
	@Column(name="skuid")
	private String skuId;
	
	@Column(name="asset")
	private String assetName;
	
	@Column(name="status")
	private int status;
	
//	@ManyToOne
//	@JsonBackReference
//	@JoinColumn(name = "assetid")
//	private AssetVO assetVO;

	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}
