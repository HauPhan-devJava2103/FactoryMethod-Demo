package vn.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.com.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
