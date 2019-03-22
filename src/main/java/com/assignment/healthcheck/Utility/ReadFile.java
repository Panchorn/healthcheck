package com.assignment.healthcheck.Utility;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReadFile {

    public ReadFile() {
    }

    public static List<String> convertMultipartFileToList(MultipartFile file) {
        List<String> websites = new LinkedList<>();
        if (!file.getContentType().equals("text/csv")) {
            return websites;
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            websites = reader.lines()
                    .skip(1) //skip header
                    .filter(s -> s != null && !Strings.isEmpty(s.trim()))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Read file fail " + e.getLocalizedMessage());
        }
        return websites;
    }

}
