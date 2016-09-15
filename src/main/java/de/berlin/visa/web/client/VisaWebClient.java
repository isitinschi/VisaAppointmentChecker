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

    private static final String PAGE_CALENDAR_MONTH_FIELD_NAME = "month";

    private final WebClient webClient;

    private HtmlPage currentPage;

    public VisaWebClient() {
        webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
    }

    public void goTo(final String url) {
        try {
            currentPage = webClient.getPage(url);
        } catch (IOException e) {
            LOGGER.error("Failed going to " + url, e);
        }
    }

    public void clickElementById(String elementId) {
        try {
            final DomElement element = currentPage.getElementById(elementId);
            element.mouseOver();
            currentPage = element.click();
            element.mouseOut();
        } catch (IOException e) {
            LOGGER.error("Failed clicking html page element with id " + elementId, e);
        }
    }

    public void clickAnchor(String anchorText) {
        try {
            final HtmlAnchor anchor = currentPage.getAnchorByText(anchorText);
            anchor.mouseOver();
            currentPage = anchor.click();
        } catch (IOException e) {
            LOGGER.error("Failed following anchor with text " + anchorText, e);
        }
    }

    public void fillInForm(String name, String lastName, String birthDay, String birthMonth, String birthYear, String waitNumber) {
        currentPage.executeJavaScript("initModel(); setFocus(); onLoad();");

        List<HtmlForm> forms = currentPage.getForms();
        if (forms == null || forms.size() != 1) {
            throw new RuntimeException("I expected exactly one form on this page. Please, investigate why I am wrong");
        }
        final HtmlForm form = forms.get(0);

        final HtmlInput nameField = form.getInputByName(FORM_FIELD_NAME);
        final HtmlInput lastNameField = form.getInputByName(FORM_FIELD_LASTNAME);
        final HtmlSelect birthDayField = form.getSelectByName(FORM_FIELD_BIRTH_DAY);
        final HtmlSelect birthMonthField = form.getSelectByName(FORM_FIELD_BIRTH_MONTH);
        final HtmlInput birthYearField = form.getInputByName(FORM_FIELD_BIRTH_YEAR);
        final HtmlInput waitNumberField = form.getInputByName(FORM_FIELD_WAIT_NUMBER);

        nameField.focus();
        nameField.setValueAttribute(name);
        nameField.blur();

        lastNameField.focus();
        lastNameField.setValueAttribute(lastName);
        lastNameField.blur();

        HtmlOption option = birthDayField.getOptionByValue(birthDay);
        birthDayField.setSelectedAttribute(option, true);
        option = birthMonthField.getOptionByValue(birthMonth);
        birthMonthField.setSelectedAttribute(option, true);

        birthYearField.focus();
        birthYearField.setValueAttribute(birthYear);
        birthYearField.blur();

        waitNumberField.focus();
        waitNumberField.setValueAttribute(waitNumber);
        waitNumberField.blur();
    }

    public String getAvailableDay() {
        List<HtmlAnchor> anchors = currentPage.getAnchors();
        for (HtmlAnchor anchor : anchors) {
            if (anchor.getAttribute("style").contains("color: rgb(0,0,255)")) {
                final DomNode child = anchor.getFirstElementChild().getFirstChild();
                String day = child.getNodeValue();
                final HtmlInput monthElement = currentPage.getElementByName(PAGE_CALENDAR_MONTH_FIELD_NAME);
                String month = monthElement.getValueAttribute();
                return day + " of " + month;
            }
        }
        return null;
    }
}
