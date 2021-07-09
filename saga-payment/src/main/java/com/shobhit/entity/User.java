package com.shobhit.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="USER")
public class User {
	@Id
	@GeneratedValue
	private int userId;
	private String userName;
	private double balance;

	public User(String userName, double balance) {
		this.userName = userName;
		this.balance = balance;
	}
}