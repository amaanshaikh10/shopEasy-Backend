package com.project.ShopEasy.Service.image;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.project.ShopEasy.DTOs.ImageDto;
import com.project.ShopEasy.Model.Image;

public interface IImageService {

	Image getImageById(Long id);

	void deleteImageById(Long id);

	List<ImageDto> saveImage(List<MultipartFile> files, Long prodId);

	void updateImage(MultipartFile file, Long imageId);

}
