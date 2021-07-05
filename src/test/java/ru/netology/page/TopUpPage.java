package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.$;

public class TopUpPage {


    private final SelenideElement amount = $("[data-test-id='amount'] .input__control");
    private final SelenideElement from = $("[data-test-id='from'] .input__control");
    private final SelenideElement actionTransfer = $("[data-test-id='action-transfer']");
    private final SelenideElement actionCancel = $("[data-test-id='action-cancel']");

    public DashboardPage transferSuccess(String amount, String cardNum) {
        fillAllFields(amount, cardNum);
        actionTransfer.click();
        $("[data-test-id='error-notification'].notification_visible")
                    .shouldNot(Condition.exist);
        return new DashboardPage();
    }

    public DashboardPage transferError(String amount, String cardNum) {
        fillAllFields(amount, cardNum);
        actionTransfer.click();
        $("[data-test-id='error-notification'].notification_visible")
                .should(Condition.exist);
        actionCancel.click();
        return new DashboardPage();
    }

    private void fillAllFields(String amount, String cardNum) {
        clear(this.amount);
        this.amount.setValue(amount);
        clear(from);
        from.setValue(cardNum);
    }

    private void clear(SelenideElement element) {
        while(!element.getValue().isBlank()) {
            element.click();
            element.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME, Keys.BACK_SPACE));
        }

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

}


