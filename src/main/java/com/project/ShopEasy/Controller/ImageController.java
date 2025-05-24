package com.project.ShopEasy.Controller;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
import org.springframework.web.multipart.MultipartFile;

import com.project.ShopEasy.DTOs.ImageDto;
import com.project.ShopEasy.Exception.ResourceNotFoundException;
import com.project.ShopEasy.Model.Image;
import com.project.ShopEasy.Response.ApiResponse;
import com.project.ShopEasy.Service.image.IImageService;

@RestController
@RequestMapping("${api.prefix}/images")
public class ImageController {

	@Autowired
	private IImageService imageService;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/upload")
	public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files, @RequestParam Long prodId) {
		try {
			List<ImageDto> imageDtos = imageService.saveImage(files, prodId);
			return ResponseEntity.ok(new ApiResponse("Upload success!", imageDtos));
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Upload Failed!", e.getMessage()));
		}
	}

	@GetMapping("/image/download/{imageId}")
	public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) throws SQLException {
		Image image = imageService.getImageById(imageId);
		ByteArrayResource resource = new ByteArrayResource(
				image.getImage().getBytes(1, (int) image.getImage().length()));
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType())).header(
				HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + URLEncoder.encode(image.getFileName(), StandardCharsets.UTF_8) + "\"")
				.body(resource);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("image/{imageId}/update")
	public ResponseEntity<ApiResponse> uploadImage(@PathVariable Long imageId, @RequestBody MultipartFile file) {
		try {
			Image image = imageService.getImageById(imageId);
			if (image != null) {
				imageService.updateImage(file, imageId);
				return ResponseEntity.ok().body(new ApiResponse("Update success1", null));
			}

		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}

		return ResponseEntity.status(INTERNAL_SERVER_ERROR)
				.body(new ApiResponse("Update failed!", INTERNAL_SERVER_ERROR));
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("image/{imageId}/delete")
	public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId, @RequestBody MultipartFile file) {
		try {
			Image image = imageService.getImageById(imageId);
			if (image != null) {
				imageService.deleteImageById(imageId);
				return ResponseEntity.ok().body(new ApiResponse("Delete success!", null));
			}

		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}

		return ResponseEntity.status(INTERNAL_SERVER_ERROR)
				.body(new ApiResponse("Delete failed!", INTERNAL_SERVER_ERROR));
	}

}
