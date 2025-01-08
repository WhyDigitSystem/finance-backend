package com.base.basesetup.entity;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.base.basesetup.dto.CreatedUpdatedDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sampleexcelupload")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SampleExcelUploadVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sampleexceluploadgen")
	@SequenceGenerator(name = "sampleexceluploadgen", sequenceName = "sampleexceluploadseq", initialValue = 1000000001, allocationSize = 1)
    	@Column(name = "sampleexceluploadid")
     	private Long id;
	    @Column(name="name")
	    private String name;

	    @Column(name="email")
	    private String email;

	    @Column(name="address")
	    private String address;

	    @Column(name="mobile")
	    private Long mobile; 

	    @Column(name="dob")
	    private LocalDate dob=LocalDate.now();
	    
	    @Embedded
		@Builder.Default
		private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}
