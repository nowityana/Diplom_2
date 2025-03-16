package steps;

import io.qameta.allure.Step;
import objects.Ingredients;
import objects.Order;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class OrderSteps {

    private final static String INGREDIENT_GET = "/api/ingredients";
    private final static String ORDER_CREATE = "/api/orders";

    @Step("Создание заказа после авторизации")
    public static Response orderCreateWithAuthorization(Order order, String accessToken) {
        return given().log().all()
                .filter(new AllureRestAssured())
                .header("Content-Type", "application/json")
                .header("authorization",accessToken)
                .body(order)
                .when()
                .post(ORDER_CREATE);
    }

    @Step("Создание заказа без авторизации")
    public static Response orderCreateWithoutAuthorization(Order order) {
        return given().log().all()
                .filter(new AllureRestAssured())
                .header("Content-Type", "application/json")
                .body(order)
                .when()
                .post(ORDER_CREATE);
    }

    @Step("Получение данных об ингредиентах")
    public Ingredients getIngredient() {
        return given()
                .header("Content-Type", "application/json")
                .log().all()
                .get(INGREDIENT_GET)
                .body()
                .as(Ingredients.class);
    }
}