package ru.netology.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.User;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;
import ru.netology.page.TopUpPage;

import java.util.Map;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

public class CardTest {

    private final User user = User.getInstance();
    private DashboardPage dashboardPage;
    private Map<String, Integer> cardBalances;


    @BeforeEach
    public void login() {
        open("http://localhost:9999/");
        dashboardPage = new LoginPage().login(user).inputCode(user);
    }

    @Test
    public void transferFromFirstSuccess() throws TopUpPage.TopUpPageException {
        String cardFrom = user.getCards()[0];
        String cardTo = user.getCards()[1];
        transferSuccess(cardFrom, cardTo);
    }

    @Test
    public void transferFromSecondSuccess() throws TopUpPage.TopUpPageException {
        String cardFrom = user.getCards()[1];
        String cardTo = user.getCards()[0];
        transferSuccess(cardFrom, cardTo);
    }

    private void transferSuccess(String cardFrom, String cardTo) throws TopUpPage.TopUpPageException {
        storeBalances(cardFrom, cardTo);
        int amount = cardBalances.get(cardFrom) / 2;
        dashboardPage = dashboardPage.transfer(cardFrom, cardTo,
                String.valueOf(amount));
        Map<String, Integer> cardBalancesAfterTransfer = dashboardPage.getCardBalances(cardFrom, cardTo);
        assertEquals(cardBalances.get(cardFrom) - amount, cardBalancesAfterTransfer.get(cardFrom));
        assertEquals(cardBalances.get(cardTo) + amount, cardBalancesAfterTransfer.get(cardTo));
        dashboardPage.transfer(cardTo, cardFrom, String.valueOf(amount));
    }

    private void storeBalances(String cardFrom, String cardTo) {
        cardBalances = dashboardPage.getCardBalances(cardFrom, cardTo);
    }

    @Test
    public void transferCancel() {
        String cardFrom = user.getCards()[0];
        String cardTo = user.getCards()[1];
        storeBalances(cardFrom, cardTo);
        dashboardPage = dashboardPage.cancel(cardFrom, cardTo,
                String.valueOf(cardBalances.get(cardFrom) / 2));
        assertEquals(cardBalances.get(cardFrom), dashboardPage.getCardBalance(cardFrom));
        assertEquals(cardBalances.get(cardTo), dashboardPage.getCardBalance(cardTo));
    }

    @Test
    public void transferWithZeroAmount() throws TopUpPage.TopUpPageException {
        String cardFrom = user.getCards()[0];
        String cardTo = user.getCards()[1];
        storeBalances(cardFrom, cardTo);
        dashboardPage = dashboardPage.transfer(cardFrom, cardTo, "0");
        checkBalanceNotChanged(cardFrom, cardTo);
    }

    @Test
    public void transferWithEmptyAmount() throws TopUpPage.TopUpPageException {
        String cardFrom = user.getCards()[0];
        String cardTo = user.getCards()[1];
        storeBalances(cardFrom, cardTo);
        dashboardPage = dashboardPage.transfer(cardFrom, cardTo, "");
        checkBalanceNotChanged(cardFrom, cardTo);
    }

    private void checkBalanceNotChanged(String cardFrom, String cardTo) {
        assertEquals(cardBalances.get(cardFrom), dashboardPage.getCardBalance(cardFrom));
        assertEquals(cardBalances.get(cardTo), dashboardPage.getCardBalance(cardTo));
    }

    @Test
    public void transferWithNegativeAmount() throws TopUpPage.TopUpPageException {
        String cardFrom = user.getCards()[0];
        String cardTo = user.getCards()[1];
        storeBalances(cardFrom, cardTo);
        int amount = -cardBalances.get(cardTo) / 2;
        dashboardPage = dashboardPage.transfer(cardFrom, cardTo,
                String.valueOf(amount));
        Map<String, Integer> cardBalancesAfterTransfer = dashboardPage.getCardBalances(cardFrom, cardTo);
        assertEquals(cardBalances.get(cardFrom)  + amount, cardBalancesAfterTransfer.get(cardFrom));
        assertEquals(cardBalances.get(cardTo) - amount, cardBalancesAfterTransfer.get(cardTo));
        dashboardPage.transfer(cardTo, cardFrom, String.valueOf(amount));

    }

    @Test
    public void transferWithSameCards() throws TopUpPage.TopUpPageException {
        String cardFrom = user.getCards()[0];
        String cardTo = user.getCards()[1];
        storeBalances(cardFrom, cardTo);
        dashboardPage = dashboardPage.transfer(cardFrom, cardFrom, String.valueOf(cardBalances.get(cardFrom) / 2));
        checkBalanceNotChanged(cardFrom, cardTo);
    }


    @Test
    public void transferFromNonExistentCardToFirst() {
        String firstCard = user.getCards()[0];
        String cardTo = user.getCards()[1];
        String fakeCard = user.getFakeCard();
        storeBalances(firstCard, cardTo);
        try {
            dashboardPage.transfer(fakeCard, cardTo,
                    String.valueOf(cardBalances.get(firstCard) / 2));
        } catch (TopUpPage.TopUpPageException e) {
            dashboardPage = new TopUpPage().cancel();
            checkBalanceNotChanged(firstCard, cardTo);
        }

    }

    @Test
    public void transferFromFirstWithAmountOverBalance() {
        String cardFrom = user.getCards()[0];
        String cardTo = user.getCards()[1];
        storeBalances(cardFrom, cardTo);
        assertThrows(TopUpPage.TopUpPageException.class, () -> dashboardPage.transfer(cardFrom,
                cardTo, String.valueOf(cardBalances.get(cardFrom) * 2)));
    }

    @Test
    public void transferFromSecondWithAmountOverBalance() {
        String cardFrom = user.getCards()[1];
        String cardTo = user.getCards()[0];
        storeBalances(cardFrom, cardTo);
        assertThrows(TopUpPage.TopUpPageException.class, () -> dashboardPage.transfer(cardFrom,
                cardTo, String.valueOf(cardBalances.get(cardFrom) * 2)));
    }

}
