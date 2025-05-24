package com.project.ShopEasy.Service.image;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.ShopEasy.DTOs.ImageDto;
import com.project.ShopEasy.Exception.ResourceNotFoundException;
import com.project.ShopEasy.Model.Image;
import com.project.ShopEasy.Model.Product;
import com.project.ShopEasy.Repository.ImageRepository;
import com.project.ShopEasy.Service.product.IProductService;

@Service
public class ImageService implements IImageService {

	@Autowired
	private IProductService productService;

	@Autowired
	private ImageRepository imageRepository;

	@Override
	public Image getImageById(Long id) {
		// TODO Auto-generated method stub
		return imageRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No image found with id: " + id));
	}

	@Override
	public void deleteImageById(Long id) {
		// TODO Auto-generated method stub
		imageRepository.findById(id).ifPresentOrElse(imageRepository::delete, () -> {
			throw new ResourceNotFoundException("No image found with id: " + id);
		});

	}

	@Override
	public List<ImageDto> saveImage(List<MultipartFile> files, Long prodId) {
		// TODO Auto-generated method stub
		Product product = productService.getProductById(prodId);
		List<ImageDto> saveImageDtos = new ArrayList<>();

		for (MultipartFile file : files) {
			try {
				Image image = new Image();
				image.setFileName(file.getOriginalFilename());
				image.setFileType(file.getContentType());
				image.setImage(new SerialBlob(file.getBytes()));
				image.setProduct(product);

				String buildDownloadUrl = "/api/v1/images/image/download/";
				String downloadUrl = buildDownloadUrl + image.getId();
				image.setDownloadUrl(downloadUrl);
				Image savedImage = imageRepository.save(image);

				savedImage.setDownloadUrl(buildDownloadUrl + savedImage.getId());
				imageRepository.save(savedImage);

				ImageDto imageDto = new ImageDto();
				imageDto.setId(savedImage.getId());
				imageDto.setFileName(savedImage.getFileName());
				imageDto.setDownloadUrl(savedImage.getDownloadUrl());

				saveImageDtos.add(imageDto);
			} catch (IOException | SQLException e) {
				throw new RuntimeException(e.getMessage());
			}
		}

		return saveImageDtos;
	}

	@Override
	public void updateImage(MultipartFile file, Long imageId) {
		// TODO Auto-generated method stub
		Image oldImage = getImageById(imageId);

		try {
			oldImage.setFileName(file.getOriginalFilename());
			oldImage.setFileType(file.getContentType());
			oldImage.setImage(new SerialBlob(file.getBytes()));
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e.getMessage());
		}

	}

}
