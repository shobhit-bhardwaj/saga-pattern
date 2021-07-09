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
@Table(name="INVENTORY")
public class Inventory {
	@Id
	@GeneratedValue
	private int inventoryId;
	private String inventoryName;
	private int quantity;

	public Inventory(String inventoryName, int quantity) {
		this.inventoryName = inventoryName;
		this.quantity = quantity;
	}
}