package com.base.basesetup.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.base.basesetup.dto.CreatedUpdatedDate;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "kit")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KitVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "kitgen")
	@SequenceGenerator(name = "kitgen", sequenceName = "kitseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name="kitid")
	private Long id;
	
	@Column(name="orgid")
	private Long orgId;
	
	@Builder.Default
	private String scode="BNKIT";
	
	@Column(name="active")
	private boolean active;
	
	@Column(name="kitno")
	private String kitNo;
	
	@Column(name="kitdesc")
	private String kitDesc;
	
	@Column(name="cancel")
	private boolean cancel;
	
	@Column(name="finyear")
	private String finyr;
	
	@Column(name = "partno")
	private String partNo;
	
	@Column(name = "createdby")
	private String createdBy;

	@Column(name = "modifiedby")
	private String updatedBy;
	
	@Column(name = "cancelremarks")
	private String cancelRemarks;
	
	@Column(name = "docid")
	private String docId;
	
	@Column(name = "partqty")
	private int partQty;
	
	@Column(name = "block")
	private boolean block;
	
	@Column(name ="eflag")
	private boolean eflag;
	
	@OneToMany(mappedBy = "kitVO", cascade = CascadeType.ALL)
	@JsonManagedReference
	List<KitAssetVO> kitAssetVO;
	
	
	@Builder.Default
	@Embedded
	private CreatedUpdatedDate commonDate=new CreatedUpdatedDate();
	
	@JsonGetter("active")
    public String getActive() {
        return active ? "Active" : "In-Active";
    }

    // Optionally, if you want to control serialization for 'cancel' field similarly
    @JsonGetter("cancel")
    public String getCancel() {
        return cancel ? "T" : "F";
    }

	@PrePersist
	private void setDefaultFinyr() {
		// Execute the logic to set the default value for finyr
		String fyFull = calculateFinyr();
		this.finyr = fyFull;
	}

	private String calculateFinyr() {
		// Logic to calculate finyr based on the provided SQL query
		String currentMonthDay = LocalDate.now().format(DateTimeFormatter.ofPattern("MMdd"));
		String fyFull = (currentMonthDay.compareTo("0331") > 0)
				? LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy"))
				: LocalDate.now().minusYears(1).format(DateTimeFormatter.ofPattern("yyyy"));
		return fyFull;
	}
	
}
