package dev.rija.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StorageServiceImpl implements StorageService {
    private final Path rootLocation = Paths.get("upload-dir");

    @Override
    public void store(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), rootLocation.resolve(file.getOriginalFilename()));
        } catch (IOException e) {
            throw new RuntimeException("Store file fail.");
        }
    }

    @Override
    public Resource loadFile(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Resource not found.");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Resource fail.");
        }
    }

    @Override
    public List<String> getFiles() {
        try (Stream<Path> walk = Files.walk(rootLocation)) {
            return walk.filter(Files::isRegularFile)
                    .map(filename -> filename.getFileName().toString()).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("getFiles fail.");
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Init storage fail.");
        }
    }
}
