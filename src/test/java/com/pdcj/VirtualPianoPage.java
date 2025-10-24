package com.pdcj;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.testng.Reporter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VirtualPianoPage {
    private final WebDriver driver;
    private final long NOTE_HOLD_MS = 300L;    // Tiempo que se mantiene presionada la nota
    private final long NOTE_DELAY_MS = 500L;   // Tiempo entre notas

    public VirtualPianoPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    private String tokenToDataNote(String token) {
        Pattern pattern = Pattern.compile("(\\d+)(do|re|mi|fa|sol|la|si)");
        Matcher matcher = pattern.matcher(token);
        
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Token inválido: " + token);
        }
        
        String octave = matcher.group(1);
        String note = matcher.group(2);
        
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

    private String dataToSpanishNote(String dataNote) {
        Pattern pattern = Pattern.compile("(\\d+)([a-g])");
        Matcher matcher = pattern.matcher(dataNote);
        
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Data note inválido: " + dataNote);
        }
        
        String octave = matcher.group(1);
        String note = matcher.group(2);
        
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

    private String tokenToNote(String token) {
        Pattern pattern = Pattern.compile("(\\d+)(do|re|mi|fa|sol|la|si)");
        Matcher matcher = pattern.matcher(token);
        
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Token inválido: " + token);
        }
        
        String note = matcher.group(2);
        
        switch (note) {
            case "sol":  // sol -> w
                return "w";
            case "la":   // la -> e
                return "e";
            case "si":   // si -> r
                return "r";
            case "do":   // do -> t
                return "t";
            case "re":   // re -> y
                return "y";
            default:
                throw new IllegalArgumentException("Nota no soportada: " + note);
        }
    }
    
    private void playNote(String token) {
        String key = tokenToNote(token);
        String dataNote = tokenToDataNote(token);
        
        Actions actions = new Actions(driver);
        
        try {
            // Pequeña pausa antes de cada nota para asegurar que el navegador está listo
            Thread.sleep(100);
            
            String spanishNote = dataToSpanishNote(dataNote);
            // Comparación entre la nota de entrada y la nota derivada del data-note
            String inputNormalized = token == null ? "" : token.trim().toLowerCase();
            Reporter.log("&nbsp;&nbsp;&nbsp;📥 Entrada: " + inputNormalized + "<br/>");
            Reporter.log("&nbsp;&nbsp;&nbsp;📤 Salida:  " + spanishNote + "<br/>");
            Reporter.log("&nbsp;&nbsp;&nbsp;🔍 Verificación: entrada=" + inputNormalized + " <--> salida=" + spanishNote + "<br/>");
            
            if (!inputNormalized.equals(spanishNote)) {
                Reporter.log("&nbsp;&nbsp;&nbsp;❌ ERROR: Las notas no coinciden<br/>");
                throw new RuntimeException("Nota de entrada " + inputNormalized + " no coincide con nota convertida " + spanishNote);
            }
            
            Reporter.log("&nbsp;&nbsp;&nbsp;✓ Verificación exitosa<br/>");
            Reporter.log("&nbsp;&nbsp;&nbsp;⌨️  Presionando tecla: " + key + " (data-note: " + dataNote + ")<br/>");
            
            // Presionar la tecla y mantenerla
            actions.keyDown(key).perform();
            Thread.sleep(NOTE_HOLD_MS);
            
            Reporter.log("&nbsp;&nbsp;&nbsp;⏱️  Manteniendo presionada por " + NOTE_HOLD_MS + "ms<br/>");
            
            // Soltar la tecla
            actions.keyUp(key).perform();
            
            Reporter.log("&nbsp;&nbsp;&nbsp;🔓 Tecla liberada<br/>");
            Reporter.log("&nbsp;&nbsp;&nbsp;⏸️  Pausa entre notas: " + NOTE_DELAY_MS + "ms<br/>");
            Reporter.log("<br/>");
            
            // Esperar el delay entre notas
            Thread.sleep(NOTE_DELAY_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public void playSong(String notes) {
        if (notes == null || notes.trim().isEmpty()) {
            Reporter.log("⚠️ No se proporcionaron notas para reproducir<br/>");
            return;
        }
        
        String[] tokens = notes.split(",");
        Reporter.log("🎵 Iniciando reproducción de canción con " + tokens.length + " notas<br/>");
        Reporter.log("📝 Secuencia: " + notes + "<br/>");
        Reporter.log("═══════════════════════════════════════════════════<br/>");
        Reporter.log("<br/>");
        
        int noteNumber = 1;
        for (String token : tokens) {
            token = token.trim();
            if (!token.isEmpty()) {
                Reporter.log("🎹 Nota #" + noteNumber + "/" + tokens.length + "<br/>");
                playNote(token);
                noteNumber++;
            }
        }
        
        Reporter.log("<br/>");
        Reporter.log("═══════════════════════════════════════════════════<br/>");
        Reporter.log("✅ Reproducción completada exitosamente<br/>");
    }

}
