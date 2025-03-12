package com.sparta.levelup_backend.domain.s3.service;

import static com.sparta.levelup_backend.enums.ErrorCode.*;
import static java.nio.charset.StandardCharsets.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.sparta.levelup_backend.exception.s3.S3Exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Service {

	private final AmazonS3 amazonS3;

	@Value("${cloud.aws.s3.bucket-name}")
	private String bucketName;

	public String upload(MultipartFile image) {

		if (image.isEmpty() || Objects.isNull(image.getOriginalFilename())) {
			throw new S3Exception(EMPTY_FILE_EXCEPTION);
		}

		checkFileSize(image);
		return this.uploadImage(image);
	}

	private String uploadImage(MultipartFile image) {
		this.validateImageFileExtention(image.getOriginalFilename());
		try {
			return this.uploadImageToS3(image);
		} catch (IOException e) {
			throw new S3Exception(IO_EXCEPTION_ON_IMAGE_UPLOAD);
		}
	}

	// 확장자 확인
	private void validateImageFileExtention(String filename) {
		int lastDotIndex = filename.lastIndexOf(".");
		if (lastDotIndex == -1) {
			throw new S3Exception(NO_FILE_EXTENTION);
		}

		String extention = filename.substring(lastDotIndex + 1).toLowerCase();
		List<String> allowedExtentionList = Arrays.asList("jpg", "jpeg", "png");

		if (!allowedExtentionList.contains(extention)) {
			throw new S3Exception(INVALID_FILE_EXTENTION);
		}
	}

	private String uploadImageToS3(MultipartFile image) throws IOException {
		String originalFilename = image.getOriginalFilename(); //원본 파일 명
		String extention = originalFilename.substring(originalFilename.lastIndexOf(".")); //확장자 명

		String s3FileName = UUID.randomUUID().toString().substring(0, 10) + originalFilename; //변경된 파일 명

		InputStream is = image.getInputStream();
		byte[] bytes = IOUtils.toByteArray(is);

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType("image" + extention);
		metadata.setContentLength(bytes.length);
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

		try {
			PutObjectRequest putObjectRequest =
				new PutObjectRequest(bucketName, s3FileName, byteArrayInputStream, metadata);
			amazonS3.putObject(putObjectRequest); // put image to S3
		} catch (Exception e) {
			log.warn("s3 사진 업로드중 예외 발생: {}", e.getMessage());
			throw new S3Exception(PUT_OBJECT_EXCEPTION);
		} finally {
			byteArrayInputStream.close();
			is.close();
		}

		return amazonS3.getUrl(bucketName, s3FileName).toString();
	}

	public void deleteImageFromS3(String imageAddress) {
		String key = getKeyFromImageAddress(imageAddress);
		try {
			amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
		} catch (Exception e) {
			log.error("error from delete Image: {}", e.getMessage());
			throw new S3Exception(IO_EXCEPTION_ON_IMAGE_DELETE);
		}
	}

	private String getKeyFromImageAddress(String imageAddress) {
		try {
			URL url = new URL(imageAddress);
			String decodingKey = URLDecoder.decode(url.getPath(), UTF_8);
			return decodingKey.substring(1); // 맨 앞의 '/' 제거
		} catch (MalformedURLException e) {
			log.error("new error from getKeyFromImageAddress: {}", e.getMessage());
			throw new S3Exception(INVALID_IMAGE_URL);
		}
	}

	private void checkFileSize(MultipartFile image){
		// 5MB
		long MAX_FILE_SIZE = 5 * 1024 * 1024;
		if (image.getSize() > MAX_FILE_SIZE){
			throw new S3Exception(FILE_TOO_LARGE);
		}
	}
}