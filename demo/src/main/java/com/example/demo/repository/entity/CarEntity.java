package com.example.demo.repository.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Builder
@Table("car")
public class CarEntity {

    @Id
    Integer id;

    String brand;

    Integer kilowatt;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public Integer getKilowatt() {
		return kilowatt;
	}

	public void setKilowatt(Integer kilowatt) {
		this.kilowatt = kilowatt;
	}

	public CarEntity(Integer id, String brand, Integer kilowatt) {
		this.id = id;
		this.brand = brand;
		this.kilowatt = kilowatt;
	}
	
	
    
    

}
