package springbatch.jobExample3;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import springbatch.entity.Product;

public class ProductFieldSetMapper implements FieldSetMapper<Product> {
	@Override
	public Product mapFieldSet(FieldSet fieldSet) throws BindException {

		Product product = new Product();

		product.setId(fieldSet.readLong(0));
		product.setCreationDate(fieldSet.readDate(1));
		product.setDescription(fieldSet.readString(2));
		product.setExpirationDate(fieldSet.readDate(3));
		product.setName(fieldSet.readString(4));
		product.setPrice(fieldSet.readDouble(5));
		product.setStock(fieldSet.readInt(6));

		return product;
	}
}
