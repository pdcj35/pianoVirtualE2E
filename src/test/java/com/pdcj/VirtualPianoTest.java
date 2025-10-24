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
 
/**
 * Clase de pruebas para el piano virtual.
 * Ejecuta tests automatizados para verificar la reproducción de canciones
 * en la interfaz del piano virtual de musicca.com/es/piano
 */
public class VirtualPianoTest {
 
    public static WebDriver driver;
    public final static int TIMEOUT = 10;
    
    // Canción de prueba: Himno a la Alegría (Oda a la Alegría)
    public final static String SONG = "1si,1si,2do,2re,2re,2do,1si,1la,1sol,1sol,1la,1si,1si,1la,1la";
 
    /**
     * Configuración previa a cada test.
     * Inicializa el navegador Chrome y carga la página del piano virtual.
     */
    @BeforeMethod
    public void beforeTest() {
        // Configurar opciones de Chrome para el test
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        
        // Inicializar el driver y maximizar ventana
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        
        // Navegar a la página del piano virtual
        driver.get("https://www.musicca.com/es/piano");
        
        // Configurar timeouts
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TIMEOUT));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(TIMEOUT));
        
        // Esperar a que el piano se cargue completamente
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".white-key")));
    }

    /**
     * Test 1: Reproduce la canción básica (Himno a la Alegría).
     * Verifica que todas las notas se reproduzcan correctamente.
     */
    @Test(priority = 1)
    public void test1() {
        VirtualPianoPage pianoPage = new VirtualPianoPage(driver);
        pianoPage.playSong(SONG);
    }

    /**
     * Test 2: Reproduce la canción dos veces consecutivas.
     * Verifica que la reproducción funcione correctamente con canciones más largas.
     */
    @Test(priority = 2)
    public void test2() {
        VirtualPianoPage pianoPage = new VirtualPianoPage(driver);
        pianoPage.playSong(SONG + "," + SONG);
    }

    /**
     * Test 3: Reproduce una versión extendida de la canción.
     * Verifica que la aplicación pueda manejar secuencias de notas más complejas.
     */
    @Test(priority = 3)
    public void test3() {
        VirtualPianoPage pianoPage = new VirtualPianoPage(driver);
        pianoPage.playSong("1si,1si,2do,2re,2re,2do,1si,1la,1sol,1sol,1la,1si,1la,1sol,1sol,1la,1si,1sol,1la,1si,2do,1si,1sol,1la,1si,2do,1si,1sol,1sol,1la,2re," + "," + SONG);
    }

    /**
     * Limpieza después de cada test.
     * Cierra el navegador para asegurar que cada test sea independiente.
     */
    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
 
}