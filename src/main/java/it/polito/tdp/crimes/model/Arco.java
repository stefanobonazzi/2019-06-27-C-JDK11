package it.polito.tdp.crimes.model;

public class Arco {

	private String type1;
	private String type2;
	private double weight;
	
	public Arco(String type1, String type2, double weight) {
		this.type1 = type1;
		this.type2 = type2;
		this.weight = weight;
	}
	
	public String getType1() {
		return type1;
	}
	
	public void setType1(String type1) {
		this.type1 = type1;
	}
	
	public String getType2() {
		return type2;
	}
	
	public void setType2(String type2) {
		this.type2 = type2;
	}
	
	public double getWeight() {
		return weight;
	}
	
	public void setWeight(double weight) {
		this.weight = weight;
	}

	@Override
	public String toString() {
		return type1 + ",  " + type2 + "     " + weight;
	}
	
}
