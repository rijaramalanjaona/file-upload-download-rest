package dev.rija;

import dev.rija.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FileUploadDownloadRestApplication implements CommandLineRunner {
    @Autowired
    StorageService storageService;

    public static void main(String[] args) {
        SpringApplication.run(FileUploadDownloadRestApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        storageService.deleteAll();
        storageService.init();
    }
}
