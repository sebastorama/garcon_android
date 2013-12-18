package br.usp.icmc.redesmoveis.garcon;

public class OrderItem {
		private String name;
		private String status;
		
		public OrderItem(String name, String status) {
			this.name = name;
			this.status = status;
		}
		
		public String getName() {
			return this.name;
		}
		
		public String getStatus() {
			return this.status;
		}
		
}
