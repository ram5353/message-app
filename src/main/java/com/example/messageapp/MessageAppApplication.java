package com.example.messageapp;

import com.example.messageapp.folders.Folder;
import com.example.messageapp.folders.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import java.nio.file.Path;

@SpringBootApplication
public class MessageAppApplication {

	@Autowired
	private FolderRepository folderRepository;

	public static void main(String[] args) {
		SpringApplication.run(MessageAppApplication.class, args);
	}

	@Bean
	public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProperties) {
		Path bundle = astraProperties.getSecureConnectBundle().toPath();
		return builder -> builder.withCloudSecureConnectBundle(bundle);
	}

	@PostConstruct
	public void init() {
		folderRepository.save(new Folder("ram5353", "Inbox", "Blue"));
		folderRepository.save(new Folder("ram5353", "Sent", "black"));
		folderRepository.save(new Folder("ram5353", "Important", "yellow"));
	}

}
