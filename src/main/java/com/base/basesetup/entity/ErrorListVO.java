package com.base.basesetup.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="errorlist")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorListVO {
	
	@Id
	private String errorcode;
	
	private String errordesc;

}
