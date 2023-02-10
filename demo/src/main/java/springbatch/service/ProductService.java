package springbatch.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import springbatch.entity.Product;
import springbatch.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	// @Transactional
	public void addProductData(ExecutionContext executionContext, ExecutionContext jobExecutionContext) {
		List<Product> productList = new ArrayList<>();
		for (int i = 0; i < 100000; i++) {
			Product randomProduct = Product.createRandomProduct();
			executionContext.put("얼마나 실행?", i + 1);
			jobExecutionContext.put("how much?", i + 1);
			productList.add(randomProduct);
		}
		productRepository.saveAll(productList);
	}
}
