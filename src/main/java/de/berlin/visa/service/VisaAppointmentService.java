package de.berlin.visa.service;

import de.berlin.visa.web.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class VisaAppointmentService {

    @Autowired
    private WebClient webClient;

    @Scheduled(cron = "0 5 * * * ? ")
    public void checkVisaAppointment() {
        webClient.goTo("https://service.berlin.de/dienstleistung/324283/en/");
        webClient.clickButton("Make an appointment");
        webClient.clickButton("Change date");
        webClient.fillInForm();
        webClient.clickButton("Next");


        if (webClient.hasAvailableDate()) {
            // ta da
        } else {
            webClient.clickButton("Next");
            if (webClient.hasAvailableDate()) {
                // ta da
            }
        }

    }
}
