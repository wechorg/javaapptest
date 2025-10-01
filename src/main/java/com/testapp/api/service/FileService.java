package com.testapp.api.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class FileService {
    
    private static final String BASE_DIRECTORY = "/tmp/uploads/";
    
    // VULNERABILITY: Path traversal - no validation of file paths
    public String readFile(String filename) {
        try {
            File file = new File(BASE_DIRECTORY + filename);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            reader.close();
            return content.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // VULNERABILITY: Path traversal - directly using user input in file path
    public boolean writeFile(String filename, String content) {
        try {
            FileWriter writer = new FileWriter(BASE_DIRECTORY + filename);
            writer.write(content);
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // VULNERABILITY: Path traversal and arbitrary file deletion
    public boolean deleteFile(String filename) {
        File file = new File(BASE_DIRECTORY + filename);
        return file.delete();
    }
    
    // VULNERABILITY: Arbitrary file access without sanitization
    public byte[] downloadFile(String filepath) {
        try {
            return Files.readAllBytes(Paths.get(filepath));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // VULNERABILITY: Directory traversal
    public String[] listFiles(String directory) {
        File dir = new File(BASE_DIRECTORY + directory);
        return dir.list();
    }
}
