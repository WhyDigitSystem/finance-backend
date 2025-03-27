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
@Table(name = "declarationandnotes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeclarationAndNotesVO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "declarationandnotesgen")
	@SequenceGenerator(name = "declarationandnotesgen", sequenceName = "declarationandnotesseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name ="declarationandnotesid")
	private long id;
	@Column(name ="declaration",columnDefinition ="TEXT")
	private String declaration;
	@Column(name ="note1",columnDefinition ="TEXT")
	private String note1;
	@Column(name ="note1bold",columnDefinition ="TEXT")
	private String note1Bold;
	@Column(name ="note2",columnDefinition ="TEXT")
	private String note2;
	
	
}
