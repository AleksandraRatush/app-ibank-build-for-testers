package ru.netology.test;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;


import java.time.Duration;

import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {

    private final ElementsCollection cards = $$(".list__item")
            .shouldBe(CollectionCondition.size(2), Duration.ofSeconds(10));
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    public DashboardPage transfer(String cardFrom, String cardTo, String amount) throws TopUpPage.TopUpPageException {
         SelenideElement element = getCardElement(cardTo);
         element.find(".button__text").click();
         TopUpPage topUpPage = new TopUpPage();
         return topUpPage.transfer(amount, cardFrom);
    }


    public DashboardPage cancel(String cardFrom, String cardTo, String amount) {
        SelenideElement element = getCardElement(cardTo);
        element.find(".button__text").click();
        TopUpPage topUpPage = new TopUpPage();
        return topUpPage.cancel(amount, cardFrom);
    }

    public boolean checkBalance(String cardNum, String balance) {
        SelenideElement element = getCardElement(cardNum);
        String id = element.find("div").getAttribute("data-test-id");
        return String.valueOf(getCardBalance(id)).equals(balance);
    }

    public int getCardBalance(String id) {
        for (SelenideElement element : cards) {
            if (element.find("div").getAttribute("data-test-id").equals(id)) {
                return extractBalance(element.text());
            }
        }
        throw new IllegalArgumentException("Card with id " + id + "not found");
    }

    private int extractBalance(String text) {
        val start = text.indexOf(balanceStart);
        val finish = text.indexOf(balanceFinish);
        val value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

    private SelenideElement getCardElement(String cardNum) {
         for (SelenideElement element : cards) {
            if (extractLastCardDigit(element.text()).equals(extractLastCardDigit(cardNum))){
                return element;
            }
        }
        throw new IllegalArgumentException("Card with num " + cardNum + " not found");
    }

    private String extractLastCardDigit(String text) {
       return text.substring(15,19);
    }



}

