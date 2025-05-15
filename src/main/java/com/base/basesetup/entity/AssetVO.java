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
@Table(name = "asset")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetVO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "assetgen")
	@SequenceGenerator(name = "assetgen", sequenceName = "assetseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "assetid")
	private Long id;

	@Column(name = "orgid")
	private Long orgId;

	@Column(name = "assetcategory")
	private String category;

	@Column(name = "categorycode")
	private String categoryCode;

	@Column(name = "assetcode")
	private String assetCodeId;

	@Column(name = "asset")
	private String assetName;

	@Column(name = "belongsto")
	private String belongsTo;

	@Column(name = "materialidentification")
	private String materialIdentification;

	@Column(name = "manufacturepartcode")
	private String manufacturePartCode;

	@Column(name = "design")
	private String design;

	@Column(name = "length")
	private float length;

	@Column(name = "breath")
	private float breath;

	@Column(name = "height")
	private float height;

	@Column(name = "weight")
	private float weight;

	@Column(name = "quantity")
	private int quantity;

	@Column(name = "dimunit")
	private String dimUnit;

	@Column(name = "manufacturer")
	private String manufacturer;

	@Column(name = "chargableweight")
	private String chargableWeight;

	@Column(name = "brand")
	private String brand;

	@Column(name = "eanupc")
	private String eanUpc;

	@Column(name = "assettype")
	private String assetType;

	@Column(name = "expectedlife")
	private String expectedLife;

	@Column(name = "maintanenceperiod")
	private String maintanencePeriod;

	@Column(name = "expectedtrips")
	private String expectedTrips;

	@Column(name = "hsncode")
	private String hsnCode;

	@Column(name = "taxrate")
	private String taxRate;

	@Column(name = "skufrom")
	private long skuFrom;

	@Column(name = "skuto")
	private long skuTo;

	@Column(name = "costprice")
	private String costPrice;

	@Column(name = "sellprice")
	private String sellPrice;

	@Column(name = "scrapvalue")
	private String scrapValue;

	private boolean cancel;

	@Column(name = "createdby")
	private String createdBy;

	@Column(name = "modifiedby")
	private String updatedBy;

	@Column(name = "cancelremarks")
	private String cancelremarks;

	@Column(name = "pono")
	private String poNo;

	@Column(name = "podate")
	private LocalDate poDate = LocalDate.now();

	private boolean active;

	private boolean eflag = false;

//	@OneToMany(mappedBy = "assetVO", cascade = CascadeType.ALL)
//	@JsonManagedReference
//	private List<AssetItemVO> assetItemVO;

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
