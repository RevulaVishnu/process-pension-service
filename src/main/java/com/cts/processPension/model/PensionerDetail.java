package com.cts.processPension.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Model class for pensioner details
 *
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
public class PensionerDetail {
	@Column
	private String name;

	@Id
	@NotNull
	private String aadhaarNumber;

	@Column
	private Date dateOfBirth;

	@Column
	private String pan;

	@Column
	private double salary;

	@Column
	private double allowance;

	@Column
	private String pensionType;

	@Column
	private long accountNumber;

	@OneToOne(cascade = CascadeType.ALL)
	private Bank bank;
}