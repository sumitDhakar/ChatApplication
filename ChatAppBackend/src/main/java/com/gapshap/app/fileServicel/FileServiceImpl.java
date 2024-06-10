package com.gapshap.app.fileServicel;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;

@Service
public class FileServiceImpl implements IFileService {

	@Autowired
	public Cloudinary cloudinary;

	@Override
	public String uploadFileInFolder(MultipartFile myFile, String destinationPath) {
		String randomName = (UUID.randomUUID().toString() + myFile.getOriginalFilename());

		String fileName = StringUtils.cleanPath(randomName);
		Map uploadResponse;
		try {

			uploadResponse = cloudinary.uploader().upload(myFile.getBytes(),
					ObjectUtils.asMap("public_id", destinationPath + "/" + fileName));
			return (String) uploadResponse.get("secure_url");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<String> uploadFileInFolder(List<MultipartFile> file, String destinationPath) {
		// TODO Auto-generated method stub
		return null;
	}

	private String getFileExtension(String filename) {
		int lastDotIndex = filename.lastIndexOf(".");
		if (lastDotIndex >= 0) {
			return filename.substring(lastDotIndex + 1).toLowerCase();
		}
		return "";
	}

	@Override
	public String uploadFileInFolderForAnyFiles(MultipartFile file, String destinationPath) {
		String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
		String randomId = UUID.randomUUID().toString();
		String randomName = randomId.concat(originalFilename.substring(originalFilename.lastIndexOf(".")));

		try {
			Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("public_id", randomName,
					"resource_type", "auto", // auto-detect resource type
					"folder", destinationPath, // specify the folder
					"overwrite", true, // overwrite if file with the same name exists
					"invalidate", true, // invalidate CDN cache
					"use_filename", true, // use the specified filename
					"unique_filename", false, // ensure a unique filename
					"eager", Arrays.asList(
							new Transformation<Transformation>().width(100).height(100).crop("fill").gravity("face")) // specify
																														// eager
																														// transformations
																														// if
																														// needed
			));

			System.err.println(uploadResult);
			return (String) uploadResult.get("secure_url");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public void deleteImageFromCloudServer(String imageUrl) {
		// Extract public ID from the image URL
		try {
			String publicId = extractPublicId(imageUrl);

			if (publicId != null) {
				Map<?, ?> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
				System.out.println("Delete result: " + result.toString());
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Invalid image URL");
		}

	}

	// Method to extract public ID from Cloudinary image URL
	private String extractPublicId(String imageUrl) {
		// Assuming the Cloudinary image URL format is:
		// https://res.cloudinary.com/{cloudName}/image/upload/{publicId}.{format}
		// You may need to adjust this method depending on your image URL format
		String[] parts = imageUrl.split("/");
		if (parts.length >= 7) {
			String publicIdWithFormat = parts[6]; // publicId with format, e.g., "publicId.jpg"
			String[] publicIdParts = publicIdWithFormat.split("\\.");
			if (publicIdParts.length >= 2) {
				return publicIdParts[0]; // return the publicId without the format, e.g., "publicId"
			}
		}
		return null;
	}

	@Override
	public byte[] downloadImageFromCloud(String imageUrl) {
		try {

			// Open a connection to the Cloudinary URL
			URL url = new URL(imageUrl);
			URLConnection connection = url.openConnection();
			connection.connect();
			System.err.println(connection.getInputStream());

			// Read the image content from the Cloudinary URL
			try (InputStream inputStream = connection.getInputStream()) {
				byte[] imageData = inputStream.readAllBytes();
				System.err.println(imageData.length);
				return imageData;
			}
		} catch (IOException e) {
			System.err.println("there is an issue ");
			e.printStackTrace();
			return null;
		}
	}

}
