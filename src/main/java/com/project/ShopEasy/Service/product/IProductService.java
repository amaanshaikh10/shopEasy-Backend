package com.project.ShopEasy.Service.product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project.ShopEasy.DTOs.ProductDto;
import com.project.ShopEasy.Model.Product;
import com.project.ShopEasy.request.AddProductRequest;
import com.project.ShopEasy.request.UpdateProductRequest;

public interface IProductService {

	ProductDto addProduct(AddProductRequest product);

	Product getProductById(Long id);

	void deleteProductById(Long id);

	ProductDto updateProductById(UpdateProductRequest product, Long productId);

	List<ProductDto> getAllProducts();

	Page<Product> getProductsByCategory(String category, Pageable pageable);

	Page<Product> getProductsByBrand(String brand, Pageable pageable);

	Page<Product> getProductsByCategoryAndBrand(String category, String brand, Pageable pageable);

	List<ProductDto> getProductsByName(String name);

	List<Product> getProductsByNameAndBrand(String name, String brand);

	Long countProductsByBrandAndName(String brand, String name);

	ProductDto convertToDto(Product product);

	List<ProductDto> getConvertedDto(List<Product> products);

	Page<Product> getProductByCategoryNameAndBrandAndPriceBetween(Optional<String> category, Optional<String> brand,
			Optional<Double> minPrice, Optional<Double> maxPrice, int page, int size);

}
