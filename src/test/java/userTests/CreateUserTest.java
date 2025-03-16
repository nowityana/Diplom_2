package userTests;

import objects.User;
import steps.UserSteps;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class CreateUserTest {
    private String name;
    private String email;
    private String password;
    private User user;
    private UserSteps userSteps;

    @Before
    public void setUp(){
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        name = "Yana"+ RandomStringUtils.randomAlphabetic(3);
        email = "Yana"+ RandomStringUtils.randomAlphabetic(3)+ "@gmail.com";
        password = "Password" + RandomStringUtils.randomAlphabetic(3);
        userSteps = new UserSteps();
        user = new User();
    }

    @Test
    @DisplayName("Регистрация нового пользователя")
    @Description("Проверка создания нового пользователя")
    public void userCreateTest() {
        user = new User(name, email, password);
        Response response = UserSteps.userCreate(user);
        response.then().log().all().assertThat()
                .statusCode(200)
                .and().body("success", Matchers.is(true))
                .and().body("accessToken", Matchers.notNullValue())
                .and().body("refreshToken", Matchers.notNullValue());
    }

    @Test
    @DisplayName("Регистрация зарегистрированного пользователя")
    @Description("Проверка создания зарегистрированного пользователя")
    public void userCreateDublicatTest() {
        user = new User(name, email, password);
        UserSteps.userCreate(user);
        Response response = UserSteps.userCreate(user);
        response.then().log().all().assertThat()
                .statusCode(403)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("User already exists"));
    }

    @Test
    @DisplayName("Регистрация пользователя без почты")
    @Description("Проверка создания пользователя без обязательного поля email")
    public void userCreateWithoutEmailTest() {
        user = new User(name, null, password);
        Response response = UserSteps.userCreate(user);
        response.then().log().all().assertThat()
                .statusCode(403)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Регистрация пользователя без пароля")
    @Description("Проверка создания пользователя без обязательного поля password")
    public void userCreateWithoutPasswordTest() {
        user = new User(name, email, null);
        Response response = UserSteps.userCreate(user);
        response.then().log().all().assertThat()
                .statusCode(403)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("Email, password and name are required fields"));
    }

    @After  // Удаление созданного пользователя
    public void tearDown(){
        String accessToken = UserSteps.userLogin(user).then().extract().path("accessToken");
        if (accessToken !=null) {
            userSteps.userDelete(accessToken);
        }
    }
}