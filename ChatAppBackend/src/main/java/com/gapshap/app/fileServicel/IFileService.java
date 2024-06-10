package com.gapshap.app.fileServicel;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface IFileService {
	public String uploadFileInFolder(MultipartFile file, String destinationPath);
	public String uploadFileInFolderForAnyFiles(MultipartFile file, String destinationPath);

		public List<String> uploadFileInFolder(List<MultipartFile> file, String destinationPath);

		
	 public void  deleteImageFromCloudServer(String imageUrl) ;
	 
	 public byte[] downloadImageFromCloud(String imageUrl);
			
}
