# Piano Virtual E2E Testing

Proyecto de automatización de pruebas end-to-end para un piano virtual web utilizando Selenium WebDriver y TestNG.

## 📋 Descripción

Este proyecto implementa pruebas automatizadas que reproducen melodías en un piano virtual mediante la simulación de eventos de teclado. Utiliza notación musical en español (do, re, mi, fa, sol, la, si) y la mapea a teclas del teclado.

## 🎵 Características

- **Notación española**: Utiliza nombres de notas en español (1sol, 2do, 1si, etc.)
- **Mapeo de teclas**: Conversión automática a teclas del teclado
- **Validación de notas**: Comparación entrada/salida para garantizar conversión correcta
- **Timing preciso**: Control de duración de pulsación (300ms) y pausas entre notas (500ms)
- **Reportes HTML detallados**: Logs completos con emojis y formato HTML
- **Reutilización de código**: Constantes de canciones reutilizables entre tests

## 🎹 Mapeo de Notas

| Nota Española | Tecla | Data-Note |
|---------------|-------|-----------|
| sol           | W     | g         |
| la            | E     | a         |
| si            | R     | b         |
| do            | T     | c         |
| re            | Y     | d         |

## 🚀 Tecnologías

- **Java 11+**
- **Maven 3.x**
- **Selenium WebDriver 4.37.0**
- **TestNG 7.11.0**
- **ChromeDriver**

## 📦 Estructura del Proyecto

```
e2epiano/
├── src/
│   ├── main/java/com/pdcj/
│   │   └── App.java
│   └── test/java/com/pdcj/
│       ├── VirtualPianoPage.java    # Page Object con lógica de piano
│       └── VirtualPianoTest.java    # Suite de tests
├── TestNG.xml                        # Configuración de TestNG
├── pom.xml                          # Dependencias Maven
└── README.md
```

## 🔧 Instalación

1. **Clonar el repositorio**
```bash
git clone https://github.com/pdcj35/pianoVirtualE2E.git
cd pianoVirtualE2E
```

2. **Instalar dependencias**
```bash
mvn clean install
```

3. **Verificar ChromeDriver**
- Asegúrate de tener Chrome instalado
- Selenium WebDriver descargará automáticamente el driver compatible

## ▶️ Ejecución de Tests

### Ejecutar todos los tests
```bash
mvn test
```

### Ejecutar un test específico
```bash
mvn test -Dtest=VirtualPianoTest#test1
```

### Ejecutar con TestNG.xml
```bash
mvn test -DsuiteXmlFile=TestNG.xml
```

## 📊 Reportes

Los reportes HTML se generan en:
```
target/surefire-reports/index.html
```

Los reportes incluyen:
- ✅ Logs detallados de cada nota reproducida
- 📥 Comparación entrada vs salida
- ⏱️ Información de timing
- 🎵 Progreso de la secuencia musical

## 🎼 Ejemplo de Uso

```java
// Definir una melodía (Himno a la Alegría - Beethoven)
public static final String SONG = "1si,1si,2do,2re,2re,2do,1si,1la,1sol,1sol,1la,1si,1si,1la,1la";

// Reproducir la melodía
VirtualPianoPage pianoPage = new VirtualPianoPage(driver);
pianoPage.playSong(SONG);
```

## 🧪 Tests Incluidos

| Test | Descripción |
|------|-------------|
| test1 | Reproducción básica del Himno a la Alegría |
| test2 | Reproducción doble de la melodía completa |
| test3 | Secuencia extendida con repeticiones |

## ⚙️ Configuración

### Timing de Notas
```java
NOTE_HOLD_MS = 300L;   // Duración de pulsación de cada nota
NOTE_DELAY_MS = 500L;  // Pausa entre notas
```

### Chrome Options
- Headless mode disponible
- Opciones: `--no-sandbox`, `--disable-dev-shm-usage`

## 🔍 Validación

El sistema valida automáticamente:
1. ✅ Formato de token de entrada (ej: "1si")
2. ✅ Conversión correcta a data-note (ej: "1b")
3. ✅ Conversión inversa coincide con entrada
4. ❌ Lanza `RuntimeException` si hay discrepancia

## 📝 Logging

Ejemplo de log en reporte:
```
🎹 Nota #1/15
   📥 Entrada: 1si
   📤 Salida:  1si
   🔍 Verificación: entrada=1si <--> salida=1si
   ✓ Verificación exitosa
   ⌨️  Presionando tecla: r (data-note: 1b)
   ⏱️  Manteniendo presionada por 300ms
   🔓 Tecla liberada
   ⏸️  Pausa entre notas: 500ms
```

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Por favor:
1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 👤 Autor

**Pablo C** - [pdcj35](https://github.com/pdcj35)

## 🔗 Enlaces

- [Repositorio GitHub](https://github.com/pdcj35/pianoVirtualE2E)
- [Selenium Documentation](https://www.selenium.dev/documentation/)
- [TestNG Documentation](https://testng.org/doc/documentation-main.html)

---

⭐ Si este proyecto te fue útil, considera darle una estrella en GitHub!
