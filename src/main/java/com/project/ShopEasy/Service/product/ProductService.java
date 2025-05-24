package com.project.ShopEasy.Service.product;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.project.ShopEasy.DTOs.ImageDto;
import com.project.ShopEasy.DTOs.ProductDto;
import com.project.ShopEasy.Exception.AlreadyExistException;
import com.project.ShopEasy.Exception.ResourceNotFoundException;
import com.project.ShopEasy.Model.Category;
import com.project.ShopEasy.Model.Image;
import com.project.ShopEasy.Model.Product;
import com.project.ShopEasy.Repository.CategoryRepository;
import com.project.ShopEasy.Repository.ImageRepository;
import com.project.ShopEasy.Repository.ProductRepository;
import com.project.ShopEasy.request.AddProductRequest;
import com.project.ShopEasy.request.UpdateProductRequest;

@Service
public class ProductService implements IProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ImageRepository imageRepository;

	@Autowired
	private ModelMapper mapper;

	@Override
	public ProductDto addProduct(AddProductRequest request) {
		// TODO Auto-generated method stub

		if (productExists(request.getName(), request.getBrand())) {
			throw new AlreadyExistException(request.getBrand() + " " + request.getName() + " already exists");
		}
		Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
				.orElseGet(() -> {
					Category newCategory = new Category(request.getCategory().getName());
					return categoryRepository.save(newCategory);
				});
		request.setCategory(category);
		Product product = createProduct(request, category);
		return convertToDto(productRepository.save(product));
	}

	private boolean productExists(String name, String brand) {
		return productRepository.existsByNameAndBrand(name, brand);
	}

	private Product createProduct(AddProductRequest request, Category category) {

		return new Product(request.getName(), request.getBrand(), request.getInventory(), request.getPrice(),
				request.getDescription(), category);

	}

	@Override
	public Product getProductById(Long id) {
		// TODO Auto-generated method stub
		return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
	}

	@Override
	public void deleteProductById(Long id) {
		// TODO Auto-generated method stub
		productRepository.findById(id).ifPresentOrElse(productRepository::delete, () -> {
			throw new ResourceNotFoundException("Product not found!");
		});
	}

	@Override
	public ProductDto updateProductById(UpdateProductRequest product, Long productId) {
		// TODO Auto-generated method stub
		Product existingProd = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
		return convertToDto(productRepository.save(updateExistingProduct(existingProd, product)));
	}

	private Product updateExistingProduct(Product existingProd, UpdateProductRequest Request) {
		existingProd.setName(Request.getName());
		existingProd.setBrand(Request.getBrand());
		existingProd.setPrice(Request.getPrice());
		existingProd.setDescription(Request.getDescription());
		existingProd.setInventory(Request.getInventory());

		Category category = categoryRepository.findByName(Request.getCategory().getName());
		existingProd.setCategory(category);

		return existingProd;
	}

	@Override
	public List<ProductDto> getAllProducts() {
		// TODO Auto-generated method stub
		return getConvertedDto(productRepository.findAll());
	}

	@Override
	public Page<Product> getProductsByCategory(String category, Pageable pageable) {
		// TODO Auto-generated method stub
		return productRepository.findByCategoryName(category, pageable);
	}

	@Override
	public Page<Product> getProductsByBrand(String brand, Pageable pageable) {
		// TODO Auto-generated method stub
		return productRepository.findByBrand(brand, pageable);
	}

	@Override
	public Page<Product> getProductsByCategoryAndBrand(String category, String brand, Pageable pageable) {
		// TODO Auto-generated method stub
		return productRepository.findByCategoryNameAndBrand(category, brand, pageable);
	}

	@Override
	public List<ProductDto> getProductsByName(String name) {
		// TODO Auto-generated method stub
		List<Product> products = productRepository.findByName(name);
		return getConvertedDto(products);
	}

	@Override
	public List<Product> getProductsByNameAndBrand(String name, String brand) {
		// TODO Auto-generated method stub
		return productRepository.findByNameAndBrand(name, brand);
	}

	@Override
	public Long countProductsByBrandAndName(String brand, String name) {
		// TODO Auto-generated method stub
		return productRepository.countByBrandAndName(brand, name);
	}

	@Override
	public List<ProductDto> getConvertedDto(List<Product> products) {
		return products.stream().map(this::convertToDto).toList();
	}

	@Override
	public ProductDto convertToDto(Product product) {
		ProductDto dto = mapper.map(product, ProductDto.class);
		List<Image> images = imageRepository.findByProductId(product.getId());
		List<ImageDto> imageDtos = images.stream().map(image -> mapper.map(image, ImageDto.class)).toList();
		dto.setImages(imageDtos);
		return dto;
	}

	@Override
	public Page<Product> getProductByCategoryNameAndBrandAndPriceBetween(Optional<String> category,
			Optional<String> brand, Optional<Double> minPrice, Optional<Double> maxPrice, int page, int size) {
		// TODO Auto-generated method stub

		Pageable pageable = PageRequest.of(page, size);

		if (category.isPresent() && brand.isPresent() && minPrice.isPresent() && maxPrice.isPresent()) {
			return productRepository.findByCategoryNameAndBrandAndPriceBetween(category.get(), brand.get(),
					minPrice.get(), maxPrice.get(), pageable);
		} else if (category.isPresent()) {
			return productRepository.findByCategoryName(category.get(), pageable);
		} else if (brand.isPresent()) {
			return productRepository.findByBrand(brand.get(), pageable);
		} else {
			return productRepository.findAll(pageable);
		}
	}

}
