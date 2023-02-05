package springbatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import springbatch.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
