package ru.netology.page;

import com.codeborne.selenide.SelenideElement;


import static com.codeborne.selenide.Selenide.$;

public class CodePage {

    private final SelenideElement code = $("[data-test-id='code'] .input__control");
    private final SelenideElement actionVerify =  $("[data-test-id='action-verify']");


    public DashboardPage inputCode(String code) {
           this.code.setValue(code);
           actionVerify.click();
           return new DashboardPage();
    }

}
