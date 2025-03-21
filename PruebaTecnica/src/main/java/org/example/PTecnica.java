package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class PTecnica {

    private WebDriver driver;

    @BeforeClass
    public void setUp() {
        // se configura diversas opciones para edge con webDriverManager
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled"); // Evita detección como bot
        driver = new EdgeDriver(options);
        driver.manage().window().maximize();
    }

    @Test
    public void testBingSearch() {
        //manejo de error
        try {
            driver.get("https://www.bing.com");

            // Esperar a que la busqueda sea visible
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("q")));

            // simular lo mas parecido a una escritura humana
            Actions actions = new Actions(driver);
            String query = "Documentacion de Selenium";
            for (char c : query.toCharArray()) {
                actions.sendKeys(String.valueOf(c)).pause(Duration.ofMillis(200)).perform();
            }

            // time de espera para enter
            Thread.sleep(2000);
            searchBox.sendKeys(Keys.RETURN);

            // resultados visibles
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("li.b_algo h2 a")));

            //screen de resultados
            takeScreenshot("bing_search_results.png");

            //contiene enlace de navegacion
            //WebElement seleniumLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(@href, 'https://www.selenium.dev/documentation/')]")));
            WebElement seleniumLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//li[@class='https://www.selenium.dev/documentation/']//h2/a")));
            seleniumLink.click();

            seleniumLink.click();

            // tiempo de espera para siguiente captura
            Thread.sleep(2000);
            takeScreenshot("selenium_documentation.png");

            // Navega por los ítems al lado izquierdo
            navigateSidebarMenu();
        } catch (Exception e) {
            System.out.println("hubo un error durante la prueba: " + e.getMessage());
            takeScreenshot("error.png");
        }
    }

    private void navigateSidebarMenu() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            //espera que sea visible el menu izquiero
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".sidebar")));

            //itera sobre los items del menú lateral
            for (int i = 1; i <= 10; i++) {
                WebElement menuItem = driver.findElement(By.cssSelector(".sidebar li:nth-child(" + i + ")"));
                menuItem.click();
                Thread.sleep(2000);
                takeScreenshot("menu_item_" + i + ".png");
            }
        } catch (Exception e) {
            System.out.println("hubo un error al navegar el menú lateral: " + e.getMessage());
            takeScreenshot("menu_error.png");
        }
    }

    private void takeScreenshot(String fileName) {
        try {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshot, new File("screenshots/" + fileName));
        } catch (IOException e) {
            System.out.println("No se pudo tomar la captura de pantalla: " + e.getMessage());
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
