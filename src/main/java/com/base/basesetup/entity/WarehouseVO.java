package com.base.basesetup.entity;

import java.math.BigDecimal;

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
@Table(name = "warehouse")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseVO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "warehousegen")
	@SequenceGenerator(name = "warehousegen", sequenceName = "warehouseseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name="warehouseid")
	private Long id;
	@Column(name="orgid")
	private Long orgId;
	@Column(name="locationname")
	private String locationName;	
	@Column(name="locationunit")
	private String locationUnit;
	@Column(name="name")
	private String name;
	@Column(name="code")
	private String code;
	@Column(name="address")
	private String address;
	@Column(name="state")
	private String state;
	@Column(name="pincode")
	private Long pincode;
	@Column(name="city")
	private String city;
	@Column(name="country")
	private String country;
	@Column(name="gst",precision = 10,length = 2)
	private BigDecimal gst;
	@Column(name="active")
	private boolean active;
	@Column(name="cancel")
	private boolean cancel;
	@Column(name="cancelremarks")
	private String cancelremarks;
	@Column(name = "modifiedby", length = 25)
	private String updatedBy;
	@Column(name = "createdby", length = 25)
	private String createdBy;
	
	@Column(name="stockbranch")
	private String stockBranch;
	
	private boolean eflag;
	
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
