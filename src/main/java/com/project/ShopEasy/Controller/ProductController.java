package com.project.ShopEasy.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.ShopEasy.DTOs.ProductDto;
import com.project.ShopEasy.Exception.AlreadyExistException;
import com.project.ShopEasy.Exception.ResourceNotFoundException;
import com.project.ShopEasy.Model.Product;
import com.project.ShopEasy.Response.ApiResponse;
import com.project.ShopEasy.Service.product.IProductService;
import com.project.ShopEasy.request.AddProductRequest;
import com.project.ShopEasy.request.UpdateProductRequest;

@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {

	@Autowired
	IProductService productService;

	@GetMapping("/all")
	public ResponseEntity<ApiResponse> getAllProduct() {
		List<ProductDto> products = productService.getAllProducts();
		return ResponseEntity.ok(new ApiResponse("Found!", products));
	}

	@GetMapping("/product/{id}/product")
	public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id) {
		try {
			Product product = productService.getProductById(id);
			ProductDto productDto = productService.convertToDto(product);
			return ResponseEntity.ok(new ApiResponse("Found!", productDto));
		} catch (ResourceNotFoundException e) {
			// TODO Auto-generated catch block
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/add")
	public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product) {
		try {
			ProductDto theProduct = productService.addProduct(product);
			return ResponseEntity.ok(new ApiResponse("Add Product Succes!", theProduct));
		} catch (AlreadyExistException e) {
			// TODO Auto-generated catch block
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("/product/{id}/update")
	public ResponseEntity<ApiResponse> updateProduct(@RequestBody UpdateProductRequest request, @PathVariable Long id) {
		try {
			ProductDto product = productService.updateProductById(request, id);
			return ResponseEntity.ok(new ApiResponse("Update Success!", product));
		} catch (ResourceNotFoundException e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/product/{id}/delete")
	public ResponseEntity<ApiResponse> deleteProductById(@PathVariable Long id) {
		try {
			productService.deleteProductById(id);
			return ResponseEntity.ok(new ApiResponse("Delete Success!", id));
		} catch (ResourceNotFoundException e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@GetMapping("/{name}/products")
	public ResponseEntity<ApiResponse> getProductByName(@PathVariable String name) {
		try {
			List<ProductDto> products = productService.getProductsByName(name);
			if (products.isEmpty()) {
				return ResponseEntity.ok(new ApiResponse("No product found!", null));
			}
			return ResponseEntity.ok(new ApiResponse("Found Success!", products));
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
		}
	}

	@GetMapping("/product/by-brand")
	public ResponseEntity<ApiResponse> getProductByBrand(@RequestParam String brand, Pageable pageable) {
		try {
			Page<Product> productPage = productService.getProductsByBrand(brand, pageable);
			List<Product> products = productPage.getContent();
			if (products.isEmpty()) {
				return ResponseEntity.ok(new ApiResponse("No product found!", null));
			}
			List<ProductDto> dto = productService.getConvertedDto(products);
			return ResponseEntity.ok(new ApiResponse("Found Success!", dto));
		} catch (Exception e) {
			return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
		}
	}

	@GetMapping("/product/{category}/all/products")
	public ResponseEntity<ApiResponse> getProductByCategory(@PathVariable String category, Pageable pageable) {
		try {
			Page<Product> productsPage = productService.getProductsByCategory(category, pageable);
			List<Product> products = productsPage.getContent();
			if (products.isEmpty()) {
				return ResponseEntity.ok(new ApiResponse("No product found!", null));
			}
			List<ProductDto> dto = productService.getConvertedDto(products);
			return ResponseEntity.ok(new ApiResponse("Found Success!", dto));
		} catch (Exception e) {
			return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
		}
	}

	@GetMapping("/by/brand-and-name")
	public ResponseEntity<ApiResponse> getProductsByNameAndBrand(@RequestParam String productName,
			@RequestParam String brandName) {
		try {
			List<Product> products = productService.getProductsByNameAndBrand(productName, brandName);
			if (products.isEmpty()) {
				return ResponseEntity.ok(new ApiResponse("No products found!", null));
			}
			List<ProductDto> dto = productService.getConvertedDto(products);
			return ResponseEntity.ok(new ApiResponse("Found Success!", dto));
		} catch (Exception e) {
			return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
		}
	}

	@GetMapping("/by/category-and-brand")
	public ResponseEntity<ApiResponse> getProductsByCategoryAndBrand(@RequestParam String brand,
			@RequestParam String category, Pageable pageable) {
		try {
			Page<Product> productsPage = productService.getProductsByCategoryAndBrand(category, brand, pageable);
			List<Product> products = productsPage.getContent();
			if (products.isEmpty()) {
				return ResponseEntity.ok(new ApiResponse("No products found!", null));
			}
			List<ProductDto> dto = productService.getConvertedDto(products);
			return ResponseEntity.ok(new ApiResponse("Found Success!", dto));
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
		}
	}

	@GetMapping("/product/count/by-brand/and-name")
	public ResponseEntity<ApiResponse> countProductsByBrandAndName(@RequestParam String brand,
			@RequestParam String name) {
		try {
			var productCount = productService.countProductsByBrandAndName(brand, name);
			return ResponseEntity.ok(new ApiResponse("Product count!", productCount));
		} catch (Exception e) {
			return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
		}
	}

	@GetMapping("/allPro")
	public ResponseEntity<ApiResponse> getProductByCategoryNameAndBrandAndPriceBetween(
			@RequestParam Optional<String> category, @RequestParam Optional<String> brand,
			@RequestParam Optional<Double> minPrice, @RequestParam Optional<Double> maxPrice,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		try {
			Page<Product> productsPage = productService.getProductByCategoryNameAndBrandAndPriceBetween(category, brand,
					minPrice, maxPrice, page, size);
			List<Product> products = productsPage.getContent();
			if (products.isEmpty()) {
				return ResponseEntity.ok(new ApiResponse("No products found!", null));
			}
			List<ProductDto> dto = productService.getConvertedDto(products);
			return ResponseEntity.ok(new ApiResponse("Found Success!", dto));
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
		}
	}

}
