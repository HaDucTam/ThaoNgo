package org.example.service.impl;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.entity.CSV;
import org.example.service.CSVService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
                    readCSVFile(file, folderPath);
                }
            }
        }
    }
    public void readCSVFile(File file, String folderPath) {
        try {
            FileReader reader = new FileReader(file);
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
            List<CSV> csvs = new ArrayList<>();
            for(CSVRecord csvRecord : csvParser) {
                CSV csv = new CSV();
                csv.setFirstName(csvRecord.get(0));
                csv.setLastName(csvRecord.get(1));
                csv.setAddress(csvRecord.get(2));
                csv.setSpecificAdd(csvRecord.get(3));
                csv.setCity(csvRecord.get(4));
                csv.setPostCode(csvRecord.get(5));
                csvs.add(csv);
                System.out.println(csv);
                // add nó vào trong DB ở đây
            }
            extractExcelFiel(csvs, folderPath);
        }catch (IOException e) {
            log.info(e);
        }
    }
    public void extractExcelFiel(List<CSV> csvs, String pathFolder) {
        // Tạo file Excel
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("My Sheet");
            Row headerRow = sheet.createRow(0);
            String[] headers = {"First Name", "Last Name", "Address", "Specific Address", "City", "Post Code"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }
            // Tạo một trang tính mới
            int index = 1;
            for(CSV csv : csvs) {
                // Tạo một hàng mới
                Row body = sheet.createRow(index++);
                // Tạo các ô trong hàng
                Cell cell1 = body.createCell(0);
                cell1.setCellValue(csv.getFirstName());
                Cell cell2 = body.createCell(1);
                cell2.setCellValue(csv.getLastName());
                Cell cell3 = body.createCell(2);
                cell3.setCellValue(csv.getAddress());
                Cell cell4 = body.createCell(3);
                cell4.setCellValue(csv.getSpecificAdd());
                Cell cell5 = body.createCell(4);
                cell5.setCellValue(csv.getCity());
                Cell cell6 = body.createCell(5);
                cell6.setCellValue(csv.getPostCode());
            }
            // Lặp qua các hàng và các ô để kiểm tra dữ liệu và áp dụng định dạng
            for(int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
                Row currentRow = sheet.getRow(rowNum);
                if(currentRow != null) {
                    for(int colNum = 0; colNum < currentRow.getLastCellNum(); colNum++) {
                        Cell currentCell = currentRow.getCell(colNum);
                        if(currentCell != null && !currentCell.getStringCellValue().isEmpty()) {
                            CellStyle style = workbook.createCellStyle();
                            style.setBorderTop(BorderStyle.THIN);
                            style.setBorderBottom(BorderStyle.THIN);
                            style.setBorderLeft(BorderStyle.THIN);
                            style.setBorderRight(BorderStyle.THIN);
                            if(rowNum == 0) {
                                Font font = workbook.createFont();
                                font.setBold(true);
                                style.setFont(font);
                            }
                            currentCell.setCellStyle(style);
                        }
                    }
                }
            }
            // Lưu Workbook vào một file
            try (FileOutputStream fileOut = new FileOutputStream(pathFolder + "/" + "workbook.xlsx")) {
                workbook.write(fileOut);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
