package de.berlin.visa.service;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VisaAppointmentServiceIT {
    @Autowired
    private VisaAppointmentService visaAppointmentService;

    @Test
    public void checkVisaAppointment() {
        Assertions.assertThat(visaAppointmentService.checkVisaAppointment()).isTrue();
    }
}