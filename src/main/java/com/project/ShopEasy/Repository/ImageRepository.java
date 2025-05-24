package com.project.ShopEasy.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.ShopEasy.Model.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

	List<Image> findByProductId(Long id);
}
