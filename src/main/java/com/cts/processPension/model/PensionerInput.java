package com.cts.processPension.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Model class for pensioner input, given by the user
 *
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PensionerInput {

	@Id
	@Pattern(regexp = "[0-9]{12}", message = "Aadhaar Number is in invalid format")
	private String aadhaarNumber;

}