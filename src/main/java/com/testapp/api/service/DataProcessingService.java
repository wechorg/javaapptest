package com.testapp.api.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class DataProcessingService {
    
    // VULNERABILITY: Command injection - executing user input directly
    public String executeCommand(String userCommand) {
        try {
            Process process = Runtime.getRuntime().exec(userCommand);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            process.waitFor();
            return output.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
    
    // VULNERABILITY: Command injection via system ping
    public String pingHost(String hostname) {
        try {
            String command = "ping -c 4 " + hostname;
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            return output.toString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    
    // VULNERABILITY: Command injection via shell execution
    public String convertFile(String inputFile, String outputFile) {
        try {
            String[] command = {"/bin/sh", "-c", "convert " + inputFile + " " + outputFile};
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            return "File converted successfully";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    
    // VULNERABILITY: Unsafe deserialization
    public Object deserializeData(byte[] data) {
        try {
            java.io.ObjectInputStream ois = new java.io.ObjectInputStream(
                new java.io.ByteArrayInputStream(data)
            );
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
