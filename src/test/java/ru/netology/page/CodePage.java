package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.User;

import static com.codeborne.selenide.Selenide.$;

public class CodePage {

    private final SelenideElement code = $("[data-test-id='code'] .input__control");
    private final SelenideElement actionVerify =  $("[data-test-id='action-verify']");


    public DashboardPage inputCode(User user) {
           code.setValue(user.getCode());
           actionVerify.click();
           return new DashboardPage();
    }

}
