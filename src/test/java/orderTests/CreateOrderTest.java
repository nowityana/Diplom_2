package orderTests;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import objects.Ingredients;
import objects.Order;
import objects.User;
import steps.OrderSteps;
import steps.UserSteps;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import java.util.ArrayList;
import java.util.List;

public class CreateOrderTest {

    private UserSteps userSteps;
    private String accessToken;
    private OrderSteps orderSteps;
    private List<String> ingredient;
    private Order order;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        String name = "Yana";
        String email = "yana123yana@gmail.com";
        String password = "qwerty123";
        userSteps = new UserSteps();
        orderSteps = new OrderSteps();
        User user = new User(name, email, password);
        UserSteps.userLogin(user);
        accessToken = UserSteps.userCreate(user).then().extract().path("accessToken");
        ingredient = new ArrayList<>();
        order = new Order(ingredient);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    @Description("Проверка успешного создания заказа с авторизацией")
    public void orderCreateWithAuthorithationTest() {
        Ingredients ingredients = orderSteps.getIngredient();
        ingredient.add(ingredients.getData().get(0).get_id());
        ingredient.add(ingredients.getData().get(1).get_id());
        ingredient.add(ingredients.getData().get(3).get_id());
        Response response = OrderSteps.orderCreateWithAuthorization(order, accessToken);
        response.then().log().all()
                .assertThat().statusCode(200)
                .and().body("success", Matchers.is(true))
                .and().body("name", Matchers.notNullValue())
                .and().body("order.number", Matchers.notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Проверка создания заказа без авторизации")
    public void orderCreateWithoutAuthorithationTest() {
        Ingredients ingredients = orderSteps.getIngredient();
        ingredient.add(ingredients.getData().get(0).get_id());
        ingredient.add(ingredients.getData().get(2).get_id());
        ingredient.add(ingredients.getData().get(3).get_id());
        Response response = OrderSteps.orderCreateWithoutAuthorization(order);
        response.then().log().all()
                .assertThat()
                .statusCode(200);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Проверка создания заказа без ингредиентов")
    public void orderCreateWithoutIngredientTest() {
        Response response = OrderSteps.orderCreateWithAuthorization(order, accessToken);
        response.then().log().all()
                .assertThat().statusCode(400)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("Проверка создания заказа с неверным хешем ингредиентов")
    public void orderCreateWithWrongHashTest() {
        Ingredients ingredients = orderSteps.getIngredient();
        ingredient.add(ingredients.getData().get(0).get_id() + (1 + (int) (Math.random() * 3)));
        ingredient.add(ingredients.getData().get(2).get_id() + (1 + (int) (Math.random() * 3)));
        Response response = OrderSteps.orderCreateWithAuthorization(order, accessToken);
        response.then().log().all()
                .statusCode(500);
    }

    @After  // Удаление созданного пользователя
    public void tearDown() {
        if (accessToken != null) {
            userSteps.userDelete(accessToken);
        }
    }
}