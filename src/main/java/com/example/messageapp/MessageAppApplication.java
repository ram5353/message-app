package com.example.messageapp;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.example.messageapp.emailslist.EmailsList;
import com.example.messageapp.emailslist.EmailsListPrimaryKey;
import com.example.messageapp.emailslist.EmailsListRepository;
import com.example.messageapp.folders.Folder;
import com.example.messageapp.folders.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import java.nio.file.Path;
import java.util.Arrays;

@SpringBootApplication
public class MessageAppApplication {

	@Autowired
	private FolderRepository folderRepository;

	@Autowired
	private EmailsListRepository emailsListRepository;

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

		for (int i = 0; i < 10; i++) {
			EmailsListPrimaryKey key = new EmailsListPrimaryKey();
			key.setUserId("ram5353");
			key.setLabel("Inbox");
			key.setTimeId(Uuids.timeBased());

			EmailsList item = new EmailsList();
			item.setKey(key);
			item.setTo(Arrays.asList("ram5353"));
			item.setSubject("Subject " + i);
			item.setUnread(true);
			emailsListRepository.save(item);
		}
	}

}
