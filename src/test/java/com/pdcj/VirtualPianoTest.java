package com.pdcj;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
 
import java.time.Duration;
 
public class VirtualPianoTest {
 
    public static WebDriver driver;
    public final static int TIMEOUT = 10;
    public final static String SONG = "1si,1si,2do,2re,2re,2do,1si,1la,1sol,1sol,1la,1si,1si,1la,1la";
 
    @BeforeMethod
    public void beforeTest() {
        ChromeOptions options = new ChromeOptions();
    options.addArguments("--remote-allow-origins=*");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.get("https://www.musicca.com/es/piano");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TIMEOUT));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(TIMEOUT));
        
        // Esperar a que el piano se cargue completamente
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".white-key")));
    }

    @Test(priority = 1)
    public void test1() {
        VirtualPianoPage pianoPage = new VirtualPianoPage(driver);
        pianoPage.playSong(SONG);
    }

    @Test(priority = 2)
    public void test2() {
        VirtualPianoPage pianoPage = new VirtualPianoPage(driver);
        pianoPage.playSong(SONG + "," + SONG);
    }

    @Test(priority = 3)
    public void test3() {
        VirtualPianoPage pianoPage = new VirtualPianoPage(driver);
        pianoPage.playSong("1si,1si,2do,2re,2re,2do,1si,1la,1sol,1sol,1la,1si,1la,1sol,1sol,1la,1si,1sol,1la,1si,2do,1si,1sol,1la,1si,2do,1si,1sol,1sol,1la,2re," + "," + SONG);
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
 
}