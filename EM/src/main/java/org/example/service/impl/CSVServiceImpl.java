package org.example.service.impl;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.example.entity.CSV;
import org.example.service.CSVService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@Service
@Log4j2
public class CSVServiceImpl implements CSVService {
    @Override
    public void deliveryCsvDetail() {
        // Đường dẫn đến folder chứa file csv
        String folderPath = "./csvFile";
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        if(files != null) {
            for(File file : files) {
                if(file.isFile() && file.getName().endsWith(".csv")) {
                    System.out.println(file.getName());
                    readCSVFile(file);
                }
            }
        }
    }
    public void readCSVFile(File file) {
        try {
            FileReader reader = new FileReader(file);
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
            CSV csv = new CSV();
            for(CSVRecord csvRecord : csvParser) {
                csv.setFirstName(csvRecord.get(0));
                csv.setLastName(csvRecord.get(1));
                csv.setAddress(csvRecord.get(2));
                csv.setSpecificAdd(csvRecord.get(3));
                csv.setCity(csvRecord.get(4));
                csv.setPostCode(csvRecord.get(5));
                System.out.println(csv);
                // em add nó vào trong DB ở đây
            }
        }catch (IOException e) {
            log.info(e);
        }
    }
}
