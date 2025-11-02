package com.example.social.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class FileStorageService {
    private final Path root = Paths.get("uploads");

    public String saveImage(MultipartFile file) {
        if(file == null || file.isEmpty()) {
            throw  new IllegalArgumentException("Image is required!");
        }

        try{
            Files.createDirectories(root);
            String extension = getExtension(file.getOriginalFilename());
            String folder = LocalDateTime.now().toString().substring(0,7);
            Path dir = root.resolve(folder);
            Files.createDirectories(dir);
            String fileName = UUID.randomUUID().toString() + (extension.isEmpty() ? "" : "." + extension);
            Path target = dir.resolve(fileName);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            return "/files/" + folder + "/" + fileName;
        }
        catch (IOException e){
            throw new IllegalStateException("Failed to store file", e);
        }
    }

    private static String getExtension(String filename){
        if (filename == null) return "";
        String f = StringUtils.cleanPath(filename);
        int i = f.lastIndexOf('.');
        return (i >= 0 ? f.substring(i+1) : "");
    }
}
