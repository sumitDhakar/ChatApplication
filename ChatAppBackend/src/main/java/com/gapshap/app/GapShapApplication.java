package com.gapshap.app;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.gapshap.app.constants.AppConstants;
import com.gapshap.app.model.Roles;
import com.gapshap.app.repository.RoleRepository;

@SpringBootApplication
public class GapShapApplication implements CommandLineRunner{


	@Autowired
	private RoleRepository roleRepository;
	
	
	public static void main(String[] args) {
		SpringApplication.run(GapShapApplication.class, args);
		
		
		
		
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		// adding default roles
		
		
		
				Roles role = new Roles();
				role.setId(1);
				role.setTitle("USER");
				if(!this.roleRepository.existsById(AppConstants.USER_ROLE_ID))
				this.roleRepository.save(role);
			
				this.createFolders();
	}
	
	public void createFolders() {
		String [] folders = {"profileImages"};
		File file = new File(System.getProperty("user.dir")+"\\src\\main\\resources\\static\\uploads");
		if(!file.exists()) {
			file.mkdir();
		}
		for(String s:folders) {
			file =new File(System.getProperty("user.dir")+"\\src\\main\\resources\\static\\uploads\\"+s);
			if(!file.exists()) {
				file.mkdir();
			}
			
		}
	}
	
	
	
	

}
