package com.project.ShopEasy.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.ShopEasy.Model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	Page<Product> findByCategoryNameAndBrand(String category, String brand, Pageable pageable);

	List<Product> findByName(String name);

	List<Product> findByNameAndBrand(String name, String brand);

	Long countByBrandAndName(String brand, String name);

	boolean existsByNameAndBrand(String name, String brand);

	Page<Product> findByCategoryNameAndBrandAndPriceBetween(String category, String brand, double minPrice,
			double maxPrice, Pageable pageable);

	Page<Product> findByCategoryName(String category, Pageable pageable);

	Page<Product> findByBrand(String brand, Pageable pageable);

}
