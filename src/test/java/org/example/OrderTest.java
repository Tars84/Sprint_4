package org.example;

import static org.junit.Assert.assertTrue;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.Augmenter;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;


@RunWith(Parameterized.class)
public class OrderTest {

    public static WebDriver driver;
    public static MainPage mainPage;
    public OrderPage orderPage;
    private final int indexButton;
    // form data
    private final String name;
    private final String surname;
    private final String address;
    private final String metro;
    private final String phone;
    private final String dateOrder;
    private final String period;
    private final String color;
    private final String comment;

    public OrderTest(int indexButton, String name, String surname, String address, String metro,
                            String phone, String dateOrder, String period, String color, String comment) {
        this.indexButton = indexButton;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.metro = metro;
        this.phone = phone;
        this.dateOrder = dateOrder;
        this.period = period;
        this.color = color;
        this.comment = comment;
    }

    @Parameterized.Parameters(name = "Оформление заказа: " +
        "Способ вызова: {0}; " +
        "Имя: {1}; " +
        "Фамилия: {2}; " +
        "Адрес: {3}; " +
        "Метро: {4}; " +
        "Телефон: {5}; " +
        "Когда нужен: {6}; " +
        "Срок аренды: {7}; " +
        "Цвет: {8}; " +
        "Комментарий: {9}")
    public static Object[][] getTestData() {
        return new Object[][] {
                {0, "Тимур", "Буденного", "Москва", "Сокольники", "+79200303605", "11.06.2024", "двое суток", "grey", "Проверка 1"},
                {1, "Марина", "Циолковского", "Москва", "Лубянка", "+79200303607", "12.06.2024", "сутки", "black", "Проверка 2"}
        };
    }

    @BeforeClass
    public static void initialOrder() {
        System.setProperty("webdriver.chrome.driver","C:\\chromedriver\\chromedriver.exe");

        // Открываем страничку
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver();
        driver = new Augmenter().augment(driver);
        driver.get("https://qa-scooter.praktikum-services.ru/");
    }

    @Test
    public void testOrder() {

        driver.get("https://qa-scooter.praktikum-services.ru/");
        mainPage = new MainPage(driver);
        mainPage.waitForLoadPage();
        mainPage.clickGetCookie();

        mainPage.clickOrder(indexButton);
        orderPage = new OrderPage(driver);
        orderPage.waitForLoadOrderPage();
        orderPage.setDataFieldsAndClickNext(name, surname, address, metro, phone);
        orderPage.waitForLoadRentPage();
        orderPage.setOtherFieldsAndClickOrder(dateOrder, period, color, comment);

        assertTrue("Отсутствует сообщение об успешном завершении заказа", mainPage.isElementExist(orderPage.orderPlaced));
    }

    @AfterClass
    public static void tearDown() {
        if (driver!=null)
            driver.quit();
    }

}

