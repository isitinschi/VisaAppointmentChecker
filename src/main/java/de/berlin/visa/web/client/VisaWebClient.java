package de.berlin.visa.web.client;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class VisaWebClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(VisaWebClient.class);

    private final WebClient webClient = new WebClient(BrowserVersion.CHROME);

    private HtmlPage currentPage;

    public void goTo(final String url) {
        try {
            currentPage = webClient.getPage(url);
        } catch (IOException e) {
            LOGGER.error("Failed going to " + url, e);
        }
    }

    public void clickAnchor(String name) {
        try {
            final HtmlAnchor anchor = currentPage.getAnchorByName(name);
            currentPage = anchor.click();
        } catch (IOException e) {
            LOGGER.error("Failed following anchor with name " + name, e);
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

    public void fillInForm() {
    }

    public boolean hasAvailableDate() {
        return false;
    }
}
