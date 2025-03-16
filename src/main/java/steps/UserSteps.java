package steps;

import io.qameta.allure.Step;
import objects.User;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class UserSteps {

    private final static String USER_REGISTRATION= "api/auth/register";
    private final static String USER_AUTHORITHATION = "api/auth/login";
    private final static String USER_UPDATE = "api/auth/user";
    private final static String ORDER_GET_LIST = "/api/orders";

    @Step("Создание нового пользователя")
    public static Response userCreate(User user) {
        return given()
                .log()
                .all()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(USER_REGISTRATION);
    }

    @Step("Авторизация")
    public static Response userLogin(User user) {
        return given()
                .log()
                .all()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(USER_AUTHORITHATION);
    }

    @Step("Изменение данных пользователя с авторизацией")
    public Response userUpdateWithAuthorithation(User user, String accessToken) {
        return given()
                .log()
                .all()
                .header("Content-Type", "application/json")
                .header("authorization", accessToken)
                .body(user)
                .when()
                .patch(USER_UPDATE);
    }

    @Step("Изменение данных пользователя без авторизации")
    public Response userUpdateWithoutAuthorithation(User user) {
        return given()
                .log()
                .all()
                .header("Content-Type", "application/json")
                .body(user)
                .when()
                .patch(USER_UPDATE);
    }

    @Step("Удаление пользователя")
    public void userDelete(String accessToken){
        given()
                .header("Authorization", accessToken)
                .when()
                .delete(USER_UPDATE);
    }

    @Step("Получение списка заказов пользователя с авторизацией")
    public static Response orderGetListWithAuthorithation(User user, String accessToken) {
        return given().log().all()
                .filter(new AllureRestAssured())
                .header("Content-Type", "application/json")
                .header("authorization",accessToken)
                .body(user)
                .when()
                .get(ORDER_GET_LIST);
    }

    @Step("Получение списка заказов  пользователя без авторизации")
    public static Response orderListWithoutAuthorithation(User user) {
        return given().log().all()
                .filter(new AllureRestAssured())
                .header("Content-Type", "application/json")
                .body(user)
                .when()
                .get(ORDER_GET_LIST);
    }
}