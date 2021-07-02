package ru.netology.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.ElementNotFound;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.$;

public class TopUpPage {

    private final SelenideElement amount = $("[data-test-id='amount'] .input__control");
    private final SelenideElement from = $("[data-test-id='from'] .input__control");
    private final SelenideElement actionTransfer = $("[data-test-id='action-transfer']");
    private final SelenideElement actionCancel = $("[data-test-id='action-cancel']");

    public DashboardPage transfer(String amount, String cardNum) throws TopUpPageException {
        fillAllFields(amount, cardNum);
        actionTransfer.click();
        try {
            $("[data-test-id='error-notification'].notification_visible")
                    .shouldBe(Condition.exist);
        } catch (ElementNotFound ex) {
            return new DashboardPage();
        }
        throw new TopUpPageException();

    }

    private void fillAllFields(String amount, String cardNum) {
        this.amount.click();
        String os = System.getProperty("os.name");
        this.amount.sendKeys(Keys.chord("Mac OS X".equals(os) ? Keys.COMMAND : Keys.CONTROL, "a"),
                Keys.BACK_SPACE);
        this.amount.setValue(amount);
        from.click();
        from.sendKeys(Keys.chord(Keys.COMMAND, "a"), Keys.BACK_SPACE);
        from.setValue(cardNum);
    }

    public DashboardPage cancel(String amount, String cardNum) {
        fillAllFields(amount, cardNum);
        actionCancel.click();
        return new DashboardPage();
    }

    public DashboardPage cancel() {
        actionCancel.click();
        return new DashboardPage();
    }


    public class TopUpPageException extends Exception {

    }

}


