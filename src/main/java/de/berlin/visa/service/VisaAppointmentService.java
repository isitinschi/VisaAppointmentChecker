package de.berlin.visa.service;

import de.berlin.visa.web.client.VisaWebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class VisaAppointmentService {
    @Value("${visa.page.url}")
    private String visaPageUrl;
    @Value("${visa.page.appointment.anchor.text}")
    private String visaPageAppointmentAnchorText;
    @Value("${visa.page.form.name}")
    private String visaPageFormName;
    @Value("${visa.page.form.lastname}")
    private String visaPageFormLastName;
    @Value("${visa.page.form.birth.day}")
    private String visaPageFormBirthDay;
    @Value("${visa.page.form.birth.month}")
    private String visaPageFormBirthMonth;
    @Value("${visa.page.form.birth.year}")
    private String visaPageFormBirthYear;
    @Value("${visa.page.form.wait.number}")
    private String visaPageFormWaitNumber;

    @Value("${visa.page.change.button.id}")
    private String visaPageChangeButtonId;

    @Autowired
    private VisaWebClient webClient;

    @Scheduled(cron = "0 0/5 * 1/1 * ? *")
    public boolean checkVisaAppointment() {
        webClient.goTo(visaPageUrl);
        webClient.clickAnchor(visaPageAppointmentAnchorText);
        webClient.clickButton(visaPageChangeButtonId);
        webClient.fillInForm(visaPageFormName, visaPageFormLastName, visaPageFormBirthDay,
                visaPageFormBirthMonth, visaPageFormBirthYear, visaPageFormWaitNumber);

        for (int i = 0; i < 2; ++i) {
            webClient.clickAnchor("Next");
            if (webClient.hasAvailableDate()) {
                notifyAboutAvailableDate();
                return true;
            }
        }

        return false;
    }

    private void notifyAboutAvailableDate() {

    }
}
