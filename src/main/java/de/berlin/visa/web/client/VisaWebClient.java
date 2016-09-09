package de.berlin.visa.web.client;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class VisaWebClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(VisaWebClient.class);

    private static final String FORM_FIELD_NAME = "tfFirstName";
    private static final String FORM_FIELD_LASTNAME = "tfLastName";
    private static final String FORM_FIELD_BIRTH_DAY = "cobGebDatumTag";
    private static final String FORM_FIELD_BIRTH_MONTH = "cobGebDatumMonat";
    private static final String FORM_FIELD_BIRTH_YEAR = "tfGebDatumJahr";
    private static final String FORM_FIELD_WAIT_NUMBER = "tfVorgangsnummer";
    private static final String FORM_FIELD_NEXT = "txtNextpage";

    private final WebClient webClient;

    private HtmlPage currentPage;

    public VisaWebClient() {
        webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setCssEnabled(false);
    }

    public void goTo(final String url) {
        try {
            currentPage = webClient.getPage(url);
        } catch (IOException e) {
            LOGGER.error("Failed going to " + url, e);
        }
    }

    public void clickAnchor(String anchorText) {
        try {
            final HtmlAnchor anchor = currentPage.getAnchorByText(anchorText);
            currentPage = anchor.click();
        } catch (IOException e) {
            LOGGER.error("Failed following anchor with text " + anchorText, e);
        }
    }

    public void clickButton(String buttonId) {
        try {
            HtmlElement button = (HtmlElement) currentPage.getElementById(buttonId);
            currentPage = button.click();
        } catch (IOException e) {
            LOGGER.error("Failed clicking button with id " + buttonId, e);
        }
    }

    public void fillInForm(String name, String lastName, String birthDay, String birthMonth, String birthYear, String waitNumber) {
        List<HtmlForm> forms = currentPage.getForms();
        if (forms == null || forms.size() != 1) {
            throw new RuntimeException("I expected exactly one form on this page. Please, investigate why I am wrong");

        }
        final HtmlForm form = forms.get(0);

        final HtmlTextInput nameField = form.getInputByName(FORM_FIELD_NAME);
        final HtmlTextInput lastNameField = form.getInputByName(FORM_FIELD_LASTNAME);
        final HtmlSelect birthDayField = form.getSelectByName(FORM_FIELD_BIRTH_DAY);
        final HtmlSelect birthMonthField = form.getSelectByName(FORM_FIELD_BIRTH_MONTH);
        final HtmlTextInput birthYearField = form.getInputByName(FORM_FIELD_BIRTH_YEAR);
        final HtmlTextInput waitNumberField = form.getInputByName(FORM_FIELD_WAIT_NUMBER);
        final HtmlHiddenInput button = form.getInputByName(FORM_FIELD_NEXT);

        nameField.setValueAttribute(name);
        lastNameField.setValueAttribute(lastName);
        HtmlOption option = birthDayField.getOptionByValue(birthDay);
        birthDayField.setSelectedAttribute(option, true);
        option = birthMonthField.getOptionByValue(birthMonth);
        birthMonthField.setSelectedAttribute(option, true);
        birthYearField.setValueAttribute(birthYear);
        waitNumberField.setValueAttribute(waitNumber);

        try {
            currentPage = button.click();
        } catch (IOException e) {
            LOGGER.error("Failed clicking form button with id " + "bla", e);
        }
    }

    public boolean hasAvailableDate() {
        List<HtmlAnchor> anchors = currentPage.getAnchors();
        for (HtmlAnchor anchor : anchors) {
            if (anchor.getAttribute("style").contains("color: rgb(0, 0, 255)")) {
                DomElement child = anchor.getFirstElementChild();
                int day = Integer.valueOf(child.getNodeValue());
                return true;
            }
        }
        return false;
    }
}
