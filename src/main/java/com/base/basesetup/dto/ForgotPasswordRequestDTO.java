package com.base.basesetup.dto;

import javax.validation.constraints.NotBlank;


import lombok.Data;

@Data
public class ForgotPasswordRequestDTO {
	@NotBlank
	private String userName; 

}
