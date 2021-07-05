package ru.netology.test;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;
import ru.netology.page.TopUpPage;

import java.util.Map;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

public class CardTest {

    private final DataHelper.User user = DataHelper.getUser();
    private final DataHelper.Card card = DataHelper.getCard();
    private DashboardPage dashboardPage;
    private Map<String, Integer> cardBalances;


    @BeforeEach
    public void login() {
        open("http://localhost:9999/");
        dashboardPage = new LoginPage().login(user).inputCode(user.getCode());
    }

    @Test
    public void transferFromFirstSuccess() {
        String cardFrom = card.getCards()[0];
        String cardTo = card.getCards()[1];
        transferSuccess(cardFrom, cardTo);
    }

    @Test
    public void transferFromSecondSuccess() {
        String cardFrom = card.getCards()[1];
        String cardTo = card.getCards()[0];
        transferSuccess(cardFrom, cardTo);
    }

    private void transferSuccess(String cardFrom, String cardTo) {
        storeBalances(cardFrom, cardTo);
        int amount = cardBalances.get(cardFrom) / 2;
        TopUpPage topUpPage = dashboardPage.topUp(cardFrom, cardTo,
                String.valueOf(amount));
        dashboardPage = topUpPage.transferSuccess(String.valueOf(amount), cardFrom);
        Map<String, Integer> cardBalancesAfterTransfer = dashboardPage.getCardBalances(cardFrom, cardTo);
        assertEquals(cardBalances.get(cardFrom) - amount, cardBalancesAfterTransfer.get(cardFrom));
        assertEquals(cardBalances.get(cardTo) + amount, cardBalancesAfterTransfer.get(cardTo));
        topUpPage = dashboardPage.topUp(cardTo, cardFrom, String.valueOf(amount));
        topUpPage.transferSuccess(String.valueOf(amount), cardTo);
    }

    private void storeBalances(String cardFrom, String cardTo) {
        cardBalances = dashboardPage.getCardBalances(cardFrom, cardTo);
    }

    @Test
    public void transferCancel() {
        String cardFrom = card.getCards()[0];
        String cardTo = card.getCards()[1];
        storeBalances(cardFrom, cardTo);
        String amount = String.valueOf(cardBalances.get(cardFrom) / 2);
        TopUpPage topPage = dashboardPage.cancel(cardFrom, cardTo,
                amount);
        dashboardPage = topPage.cancel(amount, cardFrom);
        assertEquals(cardBalances.get(cardFrom), dashboardPage.getCardBalance(cardFrom));
        assertEquals(cardBalances.get(cardTo), dashboardPage.getCardBalance(cardTo));
    }

    @Test
    public void transferWithZeroAmount() {
        String cardFrom = card.getCards()[0];
        String cardTo = card.getCards()[1];
        storeBalances(cardFrom, cardTo);
        String amount = "0";
        TopUpPage topUpPage = dashboardPage.topUp(cardFrom, cardTo, amount);
        topUpPage.transferSuccess(amount, cardFrom);
        checkBalanceNotChanged(cardFrom, cardTo);
    }

    @Test
    public void transferWithEmptyAmount() {
        String cardFrom = card.getCards()[0];
        String cardTo = card.getCards()[1];
        storeBalances(cardFrom, cardTo);
        String amount = "";
        TopUpPage topUpPage = dashboardPage.topUp(cardFrom, cardTo, amount);
        dashboardPage = topUpPage.transferSuccess(amount, cardFrom);
        checkBalanceNotChanged(cardFrom, cardTo);
    }

    private void checkBalanceNotChanged(String cardFrom, String cardTo) {
        assertEquals(cardBalances.get(cardFrom), dashboardPage.getCardBalance(cardFrom));
        assertEquals(cardBalances.get(cardTo), dashboardPage.getCardBalance(cardTo));
    }

    @Test
    public void transferWithNegativeAmount() {
        String cardFrom = card.getCards()[0];
        String cardTo = card.getCards()[1];
        storeBalances(cardFrom, cardTo);
        int amount = -cardBalances.get(cardTo) / 2;
        TopUpPage topUpPage = dashboardPage.topUp(cardFrom, cardTo,
                String.valueOf(amount));
        dashboardPage = topUpPage.transferSuccess(String.valueOf(amount), cardFrom);
        Map<String, Integer> cardBalancesAfterTransfer = dashboardPage.getCardBalances(cardFrom, cardTo);
        assertEquals(cardBalances.get(cardFrom) + amount, cardBalancesAfterTransfer.get(cardFrom));
        assertEquals(cardBalances.get(cardTo) - amount, cardBalancesAfterTransfer.get(cardTo));
        dashboardPage.topUp(cardTo, cardFrom, String.valueOf(amount));

    }

    @Test
    public void transferWithSameCards() {
        String cardFrom = card.getCards()[0];
        String cardTo = card.getCards()[1];
        storeBalances(cardFrom, cardTo);
        String amount = String.valueOf(cardBalances.get(cardFrom) / 2);
        TopUpPage topUpPage = dashboardPage.topUp(cardFrom, cardFrom,
                amount);
        topUpPage.transferSuccess(amount, cardFrom);
        checkBalanceNotChanged(cardFrom, cardTo);
    }

    @Test
    public void transferFromNonExistentCardToFirst() {
        String firstCard = card.getCards()[0];
        String cardTo = card.getCards()[1];
        String fakeCard = card.getFakeCard();
        storeBalances(firstCard, cardTo);
        String amount = String.valueOf(cardBalances.get(firstCard) / 2);
        TopUpPage topUpPage = dashboardPage.topUp(fakeCard, cardTo,
                amount);
        topUpPage.transferError(amount, fakeCard);
    }

    @Test
    public void transferFromFirstWithAmountOverBalance() {
        String cardFrom = card.getCards()[0];
        String cardTo = card.getCards()[1];
        storeBalances(cardFrom, cardTo);
        storeBalances(cardFrom, cardTo);
        String amount = String.valueOf(cardBalances.get(cardFrom) * 2);
        TopUpPage topUpPage = dashboardPage.topUp(cardFrom,
                cardTo, amount);
        topUpPage.transferError(amount, cardFrom);
    }

    @Test
    public void transferFromSecondWithAmountOverBalance() {
        String cardFrom = card.getCards()[1];
        String cardTo = card.getCards()[0];
        storeBalances(cardFrom, cardTo);
        String amount = String.valueOf(cardBalances.get(cardFrom) * 2);
        TopUpPage topUpPage = dashboardPage.topUp(cardFrom,
                cardTo, amount);
        topUpPage.transferError(amount, cardFrom);
    }

}
