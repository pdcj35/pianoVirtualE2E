package com.pdcj;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.testng.Reporter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Page Object para la interacción con el piano virtual.
 * Maneja la conversión de notas en notación española a teclas del piano
 * y ejecuta las acciones de reproducción correspondientes.
 */
public class VirtualPianoPage {
    private final WebDriver driver;
    // Tiempo que se mantiene presionada cada nota (en milisegundos)
    private final long NOTE_HOLD_MS = 300L;
    // Tiempo de pausa entre notas (en milisegundos)
    private final long NOTE_DELAY_MS = 500L;

    /**
     * Constructor que inicializa el Page Object con el driver de Selenium.
     * @param driver Instancia de WebDriver para controlar el navegador
     */
    public VirtualPianoPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Convierte un token de nota en español al formato data-note del piano.
     * Ejemplos: "1si" → "1b", "2do" → "2c", "1sol" → "1g"
     * 
     * @param token Nota en formato español (ej: "1si", "2do")
     * @return data-note en formato inglés (ej: "1b", "2c")
     * @throws IllegalArgumentException si el token no tiene formato válido
     */
    private String tokenToDataNote(String token) {
        // Expresión regular para extraer octava y nota (ej: "1si" → octava="1", nota="si")
        Pattern pattern = Pattern.compile("(\\d+)(do|re|mi|fa|sol|la|si)");
        Matcher matcher = pattern.matcher(token);
        
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Token inválido: " + token);
        }
        
        String octave = matcher.group(1);
        String note = matcher.group(2);
        
        // Mapeo de notas españolas a notación inglesa (do→c, re→d, mi→e, fa→f, sol→g, la→a, si→b)
        switch (note) {
            case "do": return octave + "c";
            case "re": return octave + "d";
            case "mi": return octave + "e";
            case "fa": return octave + "f";
            case "sol": return octave + "g";
            case "la": return octave + "a";
            case "si": return octave + "b";
            default: throw new IllegalArgumentException("Nota no soportada: " + note);
        }
    }

    /**
     * Convierte un data-note del piano a formato de nota española.
     * Operación inversa a tokenToDataNote().
     * Ejemplos: "1b" → "1si", "2c" → "2do"
     * 
     * @param dataNote Nota en formato data-note (ej: "1b", "2c")
     * @return Nota en formato español (ej: "1si", "2do")
     * @throws IllegalArgumentException si el data-note no tiene formato válido
     */
    private String dataToSpanishNote(String dataNote) {
        // Expresión regular para extraer octava y letra de nota (ej: "1b" → octava="1", letra="b")
        Pattern pattern = Pattern.compile("(\\d+)([a-g])");
        Matcher matcher = pattern.matcher(dataNote);
        
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Data note inválido: " + dataNote);
        }
        
        String octave = matcher.group(1);
        String note = matcher.group(2);
        
        // Mapeo inverso: notación inglesa a notas españolas (c→do, d→re, e→mi, etc.)
        switch (note) {
            case "c": return octave + "do";
            case "d": return octave + "re";
            case "e": return octave + "mi";
            case "f": return octave + "fa";
            case "g": return octave + "sol";
            case "a": return octave + "la";
            case "b": return octave + "si";
            default: throw new IllegalArgumentException("Nota no soportada: " + note);
        }
    }

    /**
     * Convierte una nota en español a la tecla correspondiente del teclado.
     * Mapeo de notas a teclas: sol→W, la→E, si→R, do→T, re→Y
     * 
     * @param token Nota en formato español (ej: "1sol", "2do")
     * @return Tecla del teclado (ej: "w", "t")
     * @throws IllegalArgumentException si el token no tiene formato válido
     */
    private String tokenToNote(String token) {
        // Expresión regular para validar y extraer la nota
        Pattern pattern = Pattern.compile("(\\d+)(do|re|mi|fa|sol|la|si)");
        Matcher matcher = pattern.matcher(token);
        
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Token inválido: " + token);
        }
        
        String note = matcher.group(2);
        
        // Mapeo de notas españolas a teclas del teclado WERTY (sol→w, la→e, si→r, do→t, re→y)
        switch (note) {
            case "sol":  // sol → W
                return "w";
            case "la":   // la → E
                return "e";
            case "si":   // si → R
                return "r";
            case "do":   // do → T
                return "t";
            case "re":   // re → Y
                return "y";
            default:
                throw new IllegalArgumentException("Nota no soportada: " + note);
        }
    }
    
    /**
     * Reproduce una nota individual en el piano virtual.
     * Proceso:
     * 1. Convierte la nota española a tecla y data-note
     * 2. Valida que la conversión sea correcta (entrada vs salida)
     * 3. Simula la pulsación de tecla con timing específico
     * 4. Registra logs detallados de todo el proceso
     * 
     * @param token Nota en formato español (ej: "1sol", "2do")
     * @throws RuntimeException si la nota de entrada no coincide con la salida
     * @throws InterruptedException si ocurre un error en los sleeps de timing
     */
    private void playNote(String token) {
        // Obtener tecla del teclado y formato data-note
        String key = tokenToNote(token);
        String dataNote = tokenToDataNote(token);
        
        Actions actions = new Actions(driver);
        
        try {
            // Pequeña pausa antes de cada nota para asegurar que el navegador está listo
            Thread.sleep(100);
            
            // Validación: convertir data-note de vuelta a español para comparar
            String spanishNote = dataToSpanishNote(dataNote);
            String inputNormalized = token == null ? "" : token.trim().toLowerCase();
            
            // Log de comparación entrada vs salida
            Reporter.log("&nbsp;&nbsp;&nbsp;📥 Entrada: " + inputNormalized + "<br/>");
            Reporter.log("&nbsp;&nbsp;&nbsp;📤 Salida:  " + spanishNote + "<br/>");
            Reporter.log("&nbsp;&nbsp;&nbsp;🔍 Verificación: entrada=" + inputNormalized + " <--> salida=" + spanishNote + "<br/>");
            
            // Lanzar error si las notas no coinciden
            if (!inputNormalized.equals(spanishNote)) {
                Reporter.log("&nbsp;&nbsp;&nbsp;❌ ERROR: Las notas no coinciden<br/>");
                throw new RuntimeException("Nota de entrada " + inputNormalized + " no coincide con nota convertida " + spanishNote);
            }
            
            Reporter.log("&nbsp;&nbsp;&nbsp;✓ Verificación exitosa<br/>");
            Reporter.log("&nbsp;&nbsp;&nbsp;⌨️  Presionando tecla: " + key + " (data-note: " + dataNote + ")<br/>");
            
            // Presionar la tecla y mantenerla por NOTE_HOLD_MS (300ms)
            actions.keyDown(key).perform();
            Thread.sleep(NOTE_HOLD_MS);
            
            Reporter.log("&nbsp;&nbsp;&nbsp;⏱️  Manteniendo presionada por " + NOTE_HOLD_MS + "ms<br/>");
            
            // Soltar la tecla
            actions.keyUp(key).perform();
            
            Reporter.log("&nbsp;&nbsp;&nbsp;🔓 Tecla liberada<br/>");
            Reporter.log("&nbsp;&nbsp;&nbsp;⏸️  Pausa entre notas: " + NOTE_DELAY_MS + "ms<br/>");
            Reporter.log("<br/>");
            
            // Esperar el delay entre notas (NOTE_DELAY_MS = 500ms)
            Thread.sleep(NOTE_DELAY_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Reproduce una secuencia completa de notas separadas por comas.
     * Valida que la entrada no sea nula o vacía antes de comenzar.
     * Ejemplo: "1si,1si,2do,2re" reproduce 4 notas consecutivas
     * 
     * @param notes Secuencia de notas separadas por comas
     */
    public void playSong(String notes) {
        // Validar que se hayan proporcionado notas
        if (notes == null || notes.trim().isEmpty()) {
            Reporter.log("⚠️ No se proporcionaron notas para reproducir<br/>");
            return;
        }
        
        // Dividir la canción en notas individuales
        String[] tokens = notes.split(",");
        
        // Log de inicio con información de la secuencia
        Reporter.log("🎵 Iniciando reproducción de canción con " + tokens.length + " notas<br/>");
        Reporter.log("📝 Secuencia: " + notes + "<br/>");
        Reporter.log("═══════════════════════════════════════════════════<br/>");
        Reporter.log("<br/>");
        
        // Reproducir cada nota de la secuencia
        int noteNumber = 1;
        for (String token : tokens) {
            token = token.trim();
            if (!token.isEmpty()) {
                Reporter.log("🎹 Nota #" + noteNumber + "/" + tokens.length + "<br/>");
                playNote(token);
                noteNumber++;
            }
        }
        
        // Log de finalización
        Reporter.log("<br/>");
        Reporter.log("═══════════════════════════════════════════════════<br/>");
        Reporter.log("✅ Reproducción completada exitosamente<br/>");
    }

}
