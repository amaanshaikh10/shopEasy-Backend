package com.project.ShopEasy.Service.category;

import java.util.List;

import com.project.ShopEasy.Model.Category;

public interface ICategoryService {

	Category getCategoryById(Long id);

	Category getCategoryByName(String name);

	Category addCategory(Category category);

	Category updateCategory(Category category, Long id);

	List<Category> getAllCategories();

	void deleteCategoryById(Long id);
}
