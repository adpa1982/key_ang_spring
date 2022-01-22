package com.tutorial.user.model;

public class Car {

	private String brand;
	private String model;
	private Long userId;
	
	public Car() {
		super();
	}

	public Car(String brand, String model) {
		super();
		this.brand = brand;
		this.model = model;
	}

	public Car(String brand, String model, Long userId) {
		super();
		this.brand = brand;
		this.model = model;
		this.userId = userId;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
