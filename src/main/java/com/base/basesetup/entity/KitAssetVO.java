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
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "kitasset")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KitAssetVO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "kitassetgen")
	@SequenceGenerator(name = "kitassetgen", sequenceName = "kitassetseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name="kitassetid")
	private long id;
	
	@Column(name="kit2rowid")
	private int kit2RowId;
	
	@Column(name="assettype")
	private String assetType;
	
	@Column(name = "belongsto")
	private String belongsTo;
	
	@Column(name = "manufacturepartcode")
	private String manufacturePartCode;
	
	@Column(name="assetcategory")
	private String assetCategory;
	
	@Column(name="categorycode")
	private String categoryCode;
	
	@Column(name="assetcode")
	private String assetCodeId;
	
	@Column(name="asset")
	private String assetName;
	
	@Column(name="quantity")
	private int quantity;
	
	@ManyToOne
	@JoinColumn(name = "kitid")
	@JsonBackReference
	private KitVO kitVO;
}
