package de.berlin.visa.service;

import de.berlin.visa.web.client.VisaWebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    @Value("${visa.page.language.change.div.id}")
    private String visaPageLanguageChangeDivId;
    @Value("${visa.page.form.next.anchor.id}")
    private String visaPageFormNextAnchorId;
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
    @Value("${visa.page.calendar.next.anchor.id}")
    private String visaPageCalendarNextAnchorId;
    @Value("${visa.page.change.button.id}")
    private String visaPageChangeButtonId;

    @Autowired
    private VisaWebClient webClient;

    private static final Logger LOGGER = LoggerFactory.getLogger(VisaAppointmentService.class);

    @Scheduled(cron = "0 0/5 * 1/1 * ? *")
    public boolean checkVisaAppointment() {
        webClient.goTo(visaPageUrl);
        webClient.clickAnchor(visaPageAppointmentAnchorText);

        webClient.clickElementById(visaPageLanguageChangeDivId);
        webClient.clickElementById(visaPageChangeButtonId);

        webClient.fillInForm(visaPageFormName, visaPageFormLastName, visaPageFormBirthDay,
                visaPageFormBirthMonth, visaPageFormBirthYear, visaPageFormWaitNumber);

        webClient.clickElementById(visaPageFormNextAnchorId);

        for (int i = 0; i < 5; ++i) {
            String availableDay = webClient.getAvailableDay();
            if (availableDay != null) {
                notifyAboutAvailableDate(availableDay);
                return true;
            }
            webClient.clickElementById(visaPageCalendarNextAnchorId);
        }

        return false;
    }

    /**
     * Notifies interested people about available appointment date
     *
     * @param availableDay available day and month in following format: "<day> <month>";
     */
    private void notifyAboutAvailableDate(String availableDay) {
        LOGGER.info("!!!Yohoo!!! Found available date!!! It is " + availableDay);
    }
}
