package orderTests;

import objects.User;
import steps.UserSteps;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GetOrderTest {
    protected String email;
    protected String password;
    protected String name;
    private UserSteps userSteps;
    private String accessToken;
    private User user;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        name = "Yana";
        email = "yana123yana@gmail.com";
        password = "qwerty123";
        userSteps = new UserSteps();
        user = new User(name, email, password);
        UserSteps.userCreate(user);
        accessToken = UserSteps.userLogin(user).then().extract().path("accessToken");
    }

    @Test
    @DisplayName("Получение списка заказов после авторизации")
    @Description("Проверка получения списка заказов после авторизации")
    public void userGetOrderWithAuthorithationTest() {
        Response response = UserSteps.orderGetListWithAuthorithation(user, accessToken);
        response.then().log().all()
                .assertThat().statusCode(200).and().body("success", Matchers.is(true))
                .and().body("orders", Matchers.notNullValue())
                .and().body("total", Matchers.any(Integer.class))
                .and().body("totalToday", Matchers.any(Integer.class));
    }

    @Test
    @DisplayName("Получение списка заказов пользователем без авторизации")
    @Description("Проверка получения списка заказов пользователем без авторизации")
    public void userGetOrderWithoutAuthorithationTest() {
        Response response = UserSteps.orderListWithoutAuthorithation(user);
        response.then().log().all()
                .assertThat().statusCode(401)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("You should be authorised"));
    }

    @After    // Удаление созданного пользователя
    public void tearDown() {
        if (accessToken != null) {
            userSteps.userDelete(accessToken);
        }
    }
}