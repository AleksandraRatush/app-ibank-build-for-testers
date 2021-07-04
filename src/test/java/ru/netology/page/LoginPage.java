package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.User;

import static com.codeborne.selenide.Selenide.$;


public class LoginPage {

    private final SelenideElement login = $("[data-test-id='login'] .input__control");
    private final SelenideElement password = $("[data-test-id='password'] .input__control");
    private final SelenideElement actionLogin = $("[data-test-id='action-login']");



    public CodePage login(User user) {
        login.setValue(user.getName());
        password.setValue(user.getPassword());
        actionLogin.click();
        return new CodePage();
    }
}
