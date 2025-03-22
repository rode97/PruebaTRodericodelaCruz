package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

// Importaciones para grabación de pantalla
import org.monte.media.Format;
import org.monte.media.FormatKeys.MediaType;
import org.monte.media.math.Rational;
import java.awt.*;
import static org.monte.media.AudioFormatKeys.*;
import static org.monte.media.VideoFormatKeys.*;

public class PTecnicaTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private org.monte.screenrecorder.ScreenRecorder screenRecorder;

    @BeforeClass
    public void setUp() throws Exception {
        // Iniciar la grabación de pantalla
        startRecording();

        // Configurar EdgeDriver con WebDriverManager
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled"); // Evita detección como bot
        driver = new EdgeDriver(options);
        driver.manage().window().maximize();

        // Configurar esperas globales
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }


    @Test
    public void testBingSearch() throws IOException{
        try {
            // Navegar a Google
            driver.get("https://www.google.com");

            // Espera que la barra de busqueda sea visible
            WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("q")));

            // Simular escritura humana
            Actions actions = new Actions(driver);
            String query = "Documentacion de Selenium";
            for (char c : query.toCharArray()) {
                actions.sendKeys(String.valueOf(c)).pause(Duration.ofMillis(200)).perform();
            }

            //Enter buscador
            Thread.sleep(2000);
            searchBox.sendKeys(Keys.RETURN);

            //esperar resultados de busqueda
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h3")));

            //screen de resultados
            takeScreenshot("google_search_results.png");

            //enlace de la documentación de Selenium
            WebElement seleniumLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//h3[contains(text(), 'Selenium')]")));
            seleniumLink.click();

            // Espera que cargue la página de documentacion de Selenium
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".sidebar")));

            // Captura de pantalla de la documentación de Selenium
            takeScreenshot("selenium_documentation.png");

            // Navegar sobre los items a la izquierda
            //navigateSidebarMenu();
        } catch (Exception e) {
            String mainWindowHadle = driver.getWindowHandle();
            takeScreenshot("1.Documentation.png");
            WebElement frameSelector = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@id='m-documentationoverview']")));
            frameSelector.click();
            takeScreenshot("2.overview.png");
            frameSelector = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@id='m-documentationwebdriver']")));
            frameSelector.click();
            takeScreenshot("3.WebDriver.png");
            frameSelector = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@id='m-documentationselenium_manager']")));
            frameSelector.click();
            takeScreenshot("4.SeleniumManager.png");
            frameSelector = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@id='m-documentationgrid']")));
            frameSelector.click();
            takeScreenshot("5.GRID.png");
            frameSelector = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@id='m-documentationie_driver_server']")));
            frameSelector.click();
            takeScreenshot("6.IE_Driver_srvr.png");
            frameSelector = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@id='m-documentationide']")));
            frameSelector.click();
            takeScreenshot("7.IDE.png");
            frameSelector = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@id='m-documentationtest_practices']")));
            frameSelector.click();
            takeScreenshot("8.Test_PRactices.png");
            frameSelector = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@id='m-documentationlegacy']")));
            frameSelector.click();
            takeScreenshot("9.Legacy.png");
            frameSelector = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@id='m-documentationabout']")));
            frameSelector.click();
            takeScreenshot("10.About.png");
        }
    }


        public void takeScreenshot(String fileName) throws IOException {
            // Castear el WebDriver a TakesScreenshot
            TakesScreenshot ts = (TakesScreenshot) driver;

            //se obtiene screen y se guarda
            File source = ts.getScreenshotAs(OutputType.FILE);
            //se crea la carpeta screenshots
            File destination = new File("./screenshots/" + fileName);

            // Guardar el archivo en el directorio deseado
            FileHandler.copy(source, destination);
        }

    @AfterClass
    public void tearDown() throws Exception {
        //detener grabacion de pantalla
        stopRecording();

        //cerrar el navegador
        if (driver != null) {
            driver.quit();
        }
    }

    //metodos para la grabación de pantalla y creacion de carpeta
    public void startRecording() throws Exception {
        File file = new File("./Grabacion/");
        if (!file.exists()) {
            file.mkdirs();
        }

        //se evitan ambigüedades con los nombres de clases
        java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        java.awt.Rectangle captureSize = new java.awt.Rectangle(0, 0, screenSize.width, screenSize.height);

        GraphicsConfiguration gc = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration();

        screenRecorder = new org.monte.screenrecorder.ScreenRecorder(gc, captureSize,
                new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
                new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_AVI_MJPG,
                        CompressorNameKey, ENCODING_AVI_MJPG,
                        DepthKey, 24, FrameRateKey, Rational.valueOf(30),
                        QualityKey, 1.0f, KeyFrameIntervalKey, 15 * 60),
                new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black", FrameRateKey, Rational.valueOf(30)),
                null, file);

        screenRecorder.start();
        System.out.println("Grabacion iniciada...");
    }

    public void stopRecording() throws Exception {
        if (screenRecorder != null) {
            screenRecorder.stop();
            System.out.println("Grabación detenida.");
        }
    }
}