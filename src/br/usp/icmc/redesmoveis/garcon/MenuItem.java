package br.usp.icmc.redesmoveis.garcon;

public class MenuItem {
	private String id;
	private String name;
	private String price;
	
	public MenuItem(String id, String name, String price) {
		this.id = id;
		this.name = name;
		this.price = price;
	}
	
	public String getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getPrice() {
		return this.price;
	}	
}
