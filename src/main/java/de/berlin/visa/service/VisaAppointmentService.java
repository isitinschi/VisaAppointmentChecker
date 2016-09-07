package de.berlin.visa.service;

import de.berlin.visa.web.client.VisaWebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class VisaAppointmentService {
    @Value("${userBucket.path}")
    private String url;

    @Autowired
    private VisaWebClient webClient;

    @Scheduled(cron = "0 0/5 * 1/1 * ? *")
    public void checkVisaAppointment() {
        webClient.goTo(url);
        webClient.clickAnchor("Make an appointment");
        webClient.clickButton("Change date");
        webClient.fillInForm();
        webClient.clickAnchor("Next");


        if (webClient.hasAvailableDate()) {
            notifyAboutAvailableDate();
        } else {
            webClient.clickAnchor("Next");
            if (webClient.hasAvailableDate()) {
                notifyAboutAvailableDate();
            }
        }

    }

    private void notifyAboutAvailableDate() {

    }
}
