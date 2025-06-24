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
@Table(name = "rimdeclaration")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RimDeclarationVO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "rimdeclarationgen")
	@SequenceGenerator(name = "rimdeclarationgen", sequenceName = "rimdeclarationseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name ="rimdeclarationid")
	private long id;
	
	private String declaration="1.The packaging products given on hire shall always remain the property of SCM AI-PACKS Private Limited and shall not be used for the purpose otherwise agreed upon. The\r\n"
			+ "same shall be returned at the address notified by SCM AI-PACKS Private Limited.";
	private String note1="The goods listed in the above manifest are used empty packaging issued to customer on a daily hire basis. The service is packaging on";
	private String note1Bold="rental model and not sale to customer.";
	private String note2="2.No E-Way Bill is required for Empty Cargo Containers. Refer, Rule 14 of Central Goods and Services Tax (Second Amendment) Rules, 2018.";
	
//	@OneToMany(mappedBy = "rimDeclarationVO", fetch = FetchType.LAZY)
//    @JsonManagedReference
//    private List<RetrievalManifestProviderVO> retrievalManifestProviderVOs;
//	
	

}
