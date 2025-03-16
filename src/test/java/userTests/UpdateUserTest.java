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

public class UpdateUserTest {
    private UserSteps userSteps;
    private User user;
    private String accessToken;

    private final String updName = "Yana"+ RandomStringUtils.randomAlphabetic(3);
    private final String updEmail = "Yana"+ RandomStringUtils.randomAlphabetic(3)+ "@gmail.com";
    private final String updPassword = "Password" + RandomStringUtils.randomAlphabetic(3);
    User updUser = new User();


    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        String name = "Yana";
        String email = "yana123yana@gmail.com";
        String password = "qwerty123";
        userSteps = new UserSteps();
        user = new User(name, email, password);
        UserSteps.userCreate(user);
        accessToken = UserSteps.userLogin(user).then().extract().path("accessToken");
    }

    @Test
    @DisplayName("Изменение имени пользователя после авторизации")
    @Description("Проверка успешного изменения имени пользователя после авторизации")
    public void userUpdateNameWithAuthorithationTest() {
        updUser.setName(updName);
        user.setName(updName);
        Response response = userSteps.userUpdateWithAuthorithation(updUser, accessToken);
        response.then().log().all().assertThat()
                .statusCode(200)
                .and().body("success", Matchers.is(true));
    }

    @Test
    @DisplayName("Изменение логина пользователя после авторизации")
    @Description("Проверка успешного изменения логина пользователя с авторизацией.")
    public void userUpdateLoginWithAuthorithationTest() {
        updUser.setEmail(updEmail);
        user.setEmail(updEmail);
        Response response = userSteps.userUpdateWithAuthorithation(updUser, accessToken);
        response.then().log().all().assertThat()
                .statusCode(200)
                .and().body("success", Matchers.is(true));
    }

    @Test
    @DisplayName("Изменение пароля пользователя после авторизации")
    @Description("Проверка успешного изменения пароля пользователя после авторизации")
    public void userUpdatePasswordWithAuthorithationTest() {
        updUser.setPassword(updPassword);
        user.setPassword(updPassword);
        Response response = userSteps.userUpdateWithAuthorithation(updUser, accessToken);
        response.then().log().all().assertThat()
                .statusCode(200)
                .and().body("success", Matchers.is(true));
    }

    @Test
    @DisplayName("Изменение логина пользователя без авторизации")
    @Description("Проверка попытки изменения логина пользователя без авторизации")
    public void userUpdateLoginWithoutAuthorithationTest() {
        updUser.setEmail(updEmail);
        user.setEmail(updEmail);
        Response response = userSteps.userUpdateWithoutAuthorithation(updUser);
        response.then().log().all().assertThat()
                .statusCode(401)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("You should be authorised"));
    }

    @Test
    @DisplayName("Изменение пароля пользователя без авторизации")
    @Description("Проверка попытки изменения пароля пользователя без авторизации")
    public void userUpdatePasswordWithoutAuthorithationTest() {
        updUser.setPassword(updPassword);
        user.setPassword(updPassword);
        Response response = userSteps.userUpdateWithoutAuthorithation(updUser);
        response.then().log().all().assertThat()
                .statusCode(401)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("You should be authorised"));
    }

    @After
    public void tearDown() {
        // Удаление созданного пользователя
        if (accessToken != null) {
            userSteps.userDelete(accessToken);
        }
    }
}