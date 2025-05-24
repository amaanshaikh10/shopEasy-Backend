package com.project.ShopEasy.Service.category;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.ShopEasy.Exception.AlreadyExistException;
import com.project.ShopEasy.Exception.ResourceNotFoundException;
import com.project.ShopEasy.Model.Category;
import com.project.ShopEasy.Repository.CategoryRepository;

@Service
public class CategoryService implements ICategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public Category getCategoryById(Long id) {
		// TODO Auto-generated method stub
		return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
	}

	@Override
	public Category getCategoryByName(String name) {
		// TODO Auto-generated method stub
		return categoryRepository.findByName(name);
	}

	@Override
	public Category addCategory(Category category) {
		// TODO Auto-generated method stub
		return Optional.ofNullable(category).filter(c -> !categoryRepository.existsByName(c.getName()))
				.map(categoryRepository::save)
				.orElseThrow(() -> new AlreadyExistException(category.getName() + "Already Exist!"));
	}

	@Override
	public Category updateCategory(Category category, Long id) {
		// TODO Auto-generated method stub
		return categoryRepository.findById(id).map(existingCategory -> {
			existingCategory.setName(category.getName());
			return categoryRepository.save(existingCategory);
		}).orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
	}

	@Override
	public List<Category> getAllCategories() {
		// TODO Auto-generated method stub
		return categoryRepository.findAll();
	}

	@Override
	public void deleteCategoryById(Long id) {
		// TODO Auto-generated method stub
		categoryRepository.findById(id).ifPresentOrElse(categoryRepository::delete, () -> {
			throw new ResourceNotFoundException("Category not found!");
		});

	}

}
