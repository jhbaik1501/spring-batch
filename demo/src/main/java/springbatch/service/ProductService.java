package springbatch.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import springbatch.entity.Product;
import springbatch.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	@Transactional
	public void addProductData() {
		List<Product> productList = new ArrayList<>();
		for (int i = 0; i < 100000; i++) {
			Product randomProduct = Product.createRandomProduct();
			productList.add(randomProduct);
		}
		productRepository.saveAll(productList);
	}
}
