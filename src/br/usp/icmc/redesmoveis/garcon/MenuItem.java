package br.usp.icmc.redesmoveis.garcon;

public class MenuItem {
	private String name;
	private String price;
	
	public MenuItem(String name, String price) {
		this.name = name;
		this.price = price;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getPrice() {
		return this.price;
	}
}
