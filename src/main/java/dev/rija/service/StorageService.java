package dev.rija.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StorageService {
    public void store(MultipartFile file);

    public Resource loadFile(String filename);

    public List<String> getFiles();

    public void deleteAll();

    public void init();
}
