package springbatch.entity;

import java.util.Date;
import java.util.Random;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class AvailableProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private Double price;
	private Date expirationDate;
	private Date creationDate;
	private Integer stock;
	private String description;

	public AvailableProduct(String name, Double price, Date expirationDate, Date creationDate, Integer stock, String description) {
		this.name = name;
		this.price = price;
		this.expirationDate = expirationDate;
		this.creationDate = creationDate;
		this.stock = stock;
		this.description = description;
	}

	public AvailableProduct(Product product) {
		this.name = product.getName();
		this.price = product.getPrice();
		this.expirationDate = product.getExpirationDate();
		this.creationDate = product.getCreationDate();
		this.stock = product.getStock();
		this.description = product.getDescription();
	}

}