package org.example.controller;

import lombok.extern.log4j.Log4j2;
import org.example.service.CSVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Log4j2
@RequestMapping(value = "/api/v1",
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class Controller {
    @Autowired
    CSVService CSVService;


    // phần này set thời gian chạy là fixedDelay: 10000 -> sẽ đổi ra giây(10s), initialDelay = 1000 -> sau khi chạy project thì 1s sau tự động chạy batch
    @Scheduled(initialDelay = 1000, fixedDelay = 60000)
    public void processJobDeliveryCsvDetail() {
        try {
            log.info("Start processJobDeliveryCsvDetail");
            CSVService.deliveryCsvDetail();
            log.info("End processJobDeliveryCsvDetail");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error processJobDeliveryCsvDetail: " + e);
        }
    }
//    @PostMapping(value = "/")
//    public String showData(Model model) {
//        List<Customer> customerList = customerRepository.findAll();
//        model.addAttribute("dataTableList", customerList);
//        return "dataTable";
//    }
}
