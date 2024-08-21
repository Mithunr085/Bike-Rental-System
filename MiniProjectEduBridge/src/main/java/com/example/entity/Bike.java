package com.example.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "Bike")
public class Bike {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "table_one_seq")
    @SequenceGenerator(name = "table_one_seq", sequenceName = "seq_table_one", initialValue = 1000, allocationSize = 1)
	private int bikeId;
	private String bikeName;
	private boolean availability;
	public int getBikeId() {
		return bikeId;
	}
	public void setBikeId(int bikeId) {
		this.bikeId = bikeId;
	}
	public String getBikeName() {
		return bikeName;
	}
	public void setBikeName(String bikeName) {
		this.bikeName = bikeName;
	}
	public boolean isAvailability() {
		return availability;
	}
	public void setAvailability(boolean availability) {
		this.availability = availability;
	}
	@Override
	public String toString() {
		return "Bike [bikeId=" + bikeId + ", bikeName=" + bikeName + ", availability=" + availability + "]";
	}
	
	
	
}
