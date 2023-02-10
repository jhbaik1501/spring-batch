package springbatch.entity;

import java.util.Date;
import java.util.Random;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private Double price;
	private Date expirationDate;
	private Date creationDate;
	private Integer stock;
	private String description;

	public Product(String name, Double price, Date expirationDate, Date creationDate, Integer stock, String description) {
		this.name = name;
		this.price = price;
		this.expirationDate = expirationDate;
		this.creationDate = creationDate;
		this.stock = stock;
		this.description = description;
	}

	public static Product createRandomProduct() {
		Random random = new Random();

		String name = "Product " + random.ints(97, 123)
			.limit(5)
			.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
			.toString();
		Double price = random.nextDouble() * 100;
		Date expirationDate = new Date(System.currentTimeMillis() + random.nextInt(365) * 24 * 60 * 60 * 1000);
		Date creationDate = new Date();
		Integer stock = random.nextInt(1000);
		String description = "Description " + random.nextInt();

		return new Product(name, price, expirationDate, creationDate, stock, description);
	}

}