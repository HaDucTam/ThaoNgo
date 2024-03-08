package org.example.controller;

import lombok.extern.log4j.Log4j2;
import org.example.service.CSVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class Controller {
    @Autowired
    CSVService CSVService;

    // phần này set thời gian chạy là fixedDelay: 10000 -> sẽ đổi ra giây(10s), initialDelay = 1000 -> sau khi chạy project thì 1s sau tự động chạy batch
    @Scheduled(initialDelay = 1000, fixedDelay = 10000)
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
}
