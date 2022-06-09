package com.cts.processPension.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Model class for bank details
 *
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
public class Bank {
	@Column
	private String bankName;
	@Id
	private long accountNumber;
	@Column
	private String bankType;
}