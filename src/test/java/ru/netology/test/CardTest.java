package ru.netology.test;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CardTest {


    public static final String SECOND_CARD_NUMBER = "5559 0000 0000 0002";
    public static final String FIRST_CARD_NUMBER = "5559 0000 0000 0001";
    public static final String NOT_EXISTENT_CARD = "5559 0000 0000 0003";

    @BeforeAll
    public static void login() {
        open("http://0.0.0.0:9999");
        $("[data-test-id='login'] .input__control")
                .setValue("vasya");
        $("[data-test-id='password'] .input__control")
                .setValue("qwerty123");
        $("[data-test-id='action-login']").click();
        $("[data-test-id='code'] .input__control")
                .setValue("12345");
        $("[data-test-id='action-verify']").click();

    }

    @Test
    public void transferFromFirstSuccess() throws TopUpPage.TopUpPageException {
        DashboardPage dashboardPage = new DashboardPage();
        dashboardPage = dashboardPage.transfer(FIRST_CARD_NUMBER, SECOND_CARD_NUMBER,
                "1000");
        assertTrue(dashboardPage.checkBalance(FIRST_CARD_NUMBER, "9000"));
        assertTrue(dashboardPage.checkBalance(SECOND_CARD_NUMBER, "11000"));
        dashboardPage.transfer(SECOND_CARD_NUMBER, FIRST_CARD_NUMBER, "1000");

    }

    @Test
    public void transferFromSecondSuccess() throws TopUpPage.TopUpPageException {
        DashboardPage dashboardPage = new DashboardPage();
        dashboardPage = dashboardPage.transfer(SECOND_CARD_NUMBER,
                FIRST_CARD_NUMBER, "1000");
        assertTrue(dashboardPage.checkBalance(SECOND_CARD_NUMBER, "9000"));
        assertTrue(dashboardPage.checkBalance(FIRST_CARD_NUMBER, "11000"));
        dashboardPage.transfer(FIRST_CARD_NUMBER, SECOND_CARD_NUMBER, "1000");

    }

    @Test
    public void transferCancel() {
        DashboardPage dashboardPage = new DashboardPage();
        dashboardPage = dashboardPage.cancel(FIRST_CARD_NUMBER, SECOND_CARD_NUMBER, "1000");
        assertTrue(dashboardPage.checkBalance(FIRST_CARD_NUMBER, "10000"));
        assertTrue(dashboardPage.checkBalance(SECOND_CARD_NUMBER, "10000"));
    }

    @Test
    public void transferWithZeroAmount() throws TopUpPage.TopUpPageException {
        DashboardPage dashboardPage = new DashboardPage();
        dashboardPage = dashboardPage.transfer(FIRST_CARD_NUMBER, SECOND_CARD_NUMBER, "0");
        assertTrue(dashboardPage.checkBalance(FIRST_CARD_NUMBER, "10000"));
        assertTrue(dashboardPage.checkBalance(SECOND_CARD_NUMBER, "10000"));
    }

    @Test
    public void transferWithEmptyAmount() throws TopUpPage.TopUpPageException {
        DashboardPage dashboardPage = new DashboardPage();
        dashboardPage = dashboardPage.transfer(FIRST_CARD_NUMBER, SECOND_CARD_NUMBER, "");
        assertTrue(dashboardPage.checkBalance(FIRST_CARD_NUMBER, "10000"));
        assertTrue(dashboardPage.checkBalance(SECOND_CARD_NUMBER, "10000"));
    }

    @Test
    public void transferWithNegativeAmount() throws TopUpPage.TopUpPageException {
        DashboardPage dashboardPage = new DashboardPage();
        dashboardPage = dashboardPage.transfer(FIRST_CARD_NUMBER,
                SECOND_CARD_NUMBER, "-1000");
        assertTrue(dashboardPage.checkBalance(FIRST_CARD_NUMBER, "9000"));
        assertTrue(dashboardPage.checkBalance(SECOND_CARD_NUMBER, "11000"));
        dashboardPage.transfer(SECOND_CARD_NUMBER, FIRST_CARD_NUMBER, "1000");

    }

    @Test
    public void transferWithSameCards() throws TopUpPage.TopUpPageException {
        DashboardPage dashboardPage = new DashboardPage();
        dashboardPage = dashboardPage.transfer(FIRST_CARD_NUMBER, FIRST_CARD_NUMBER, "1000");
        assertTrue(dashboardPage.checkBalance(FIRST_CARD_NUMBER, "10000"));
        assertTrue(dashboardPage.checkBalance(SECOND_CARD_NUMBER, "10000"));
    }


    @Test
    public void transferFromNonExistentCardToFirst() {
        DashboardPage dashboardPage = new DashboardPage();
        try {
            dashboardPage.transfer(NOT_EXISTENT_CARD, FIRST_CARD_NUMBER,
                    "1000");
        } catch (TopUpPage.TopUpPageException e) {
            dashboardPage = new TopUpPage().cancel();
            assertTrue(dashboardPage.checkBalance(FIRST_CARD_NUMBER, "10000"));
            assertTrue(dashboardPage.checkBalance(SECOND_CARD_NUMBER, "10000"));
        }

    }

    @Test
    public void transferFromFirstWithAmountOverBalance() {
        DashboardPage dashboardPage = new DashboardPage();
        assertThrows(TopUpPage.TopUpPageException.class, () -> dashboardPage.transfer(FIRST_CARD_NUMBER,
                SECOND_CARD_NUMBER, "20000"));
    }

    @Test
    public void transferFromSecondWithAmountOverBalance() {
        DashboardPage dashboardPage = new DashboardPage();
        assertThrows(TopUpPage.TopUpPageException.class, () -> dashboardPage.transfer(SECOND_CARD_NUMBER,
                FIRST_CARD_NUMBER, "20000"));
    }
}
