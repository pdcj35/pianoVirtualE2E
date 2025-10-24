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

## 🏗️ Arquitectura del Proyecto

### Patrón Page Object Model (POM)

El proyecto implementa el **Page Object Model**, un patrón de diseño ampliamente utilizado en pruebas de automatización que promueve la reutilización de código y facilita el mantenimiento.

```
┌─────────────────────────────────────┐
│     VirtualPianoTest.java           │
│     (Test Layer)                    │
│  - test1(), test2(), test3()        │
│  - @BeforeMethod: Setup Chrome      │
│  - @AfterMethod: Cleanup Browser    │
└──────────────┬──────────────────────┘
               │ uses
               ▼
┌─────────────────────────────────────┐
│     VirtualPianoPage.java           │
│     (Page Object Layer)             │
│  - playSong(String notes)           │
│  - playNote(String token)           │
│  - tokenToDataNote()                │
│  - tokenToNote()                    │
│  - dataToSpanishNote()              │
└──────────────┬──────────────────────┘
               │ uses
               ▼
┌─────────────────────────────────────┐
│     Selenium WebDriver              │
│     (Automation Framework)          │
│  - ChromeDriver                     │
│  - Actions API (keyboard events)    │
│  - WebDriverWait                    │
└──────────────┬──────────────────────┘
               │ controls
               ▼
┌─────────────────────────────────────┐
│     Chrome Browser                  │
│     (Target Application)            │
│  - Virtual Piano Web App            │
│  - https://virtualpiano.net         │
└─────────────────────────────────────┘
```

### Componentes Principales

#### 1. **VirtualPianoTest** (Capa de Pruebas)
- **Responsabilidad**: Definir escenarios de prueba y ciclo de vida del navegador
- **Funciones**:
  - Inicializar ChromeDriver con configuraciones específicas
  - Navegar a la aplicación del piano virtual
  - Ejecutar múltiples escenarios de reproducción
  - Gestionar lifecycle del navegador (@BeforeMethod/@AfterMethod)
  - Configurar timeouts y waits

#### 2. **VirtualPianoPage** (Page Object)
- **Responsabilidad**: Encapsular toda la lógica de interacción con el piano
- **Funciones**:
  - Conversión de notación española a formato data-note
  - Mapeo de notas a teclas del teclado (WERTY)
  - Simulación de eventos de teclado con timing preciso
  - Validación de conversiones (entrada vs salida)
  - Generación de logs detallados para reportes

#### 3. **TestNG Framework** (Orquestación)
- **Responsabilidad**: Gestionar ejecución y reporting de pruebas
- **Funciones**:
  - Ordenamiento de tests mediante `priority` y `preserve-order`
  - Generación de reportes HTML
  - Integración con Maven Surefire
  - Reporter API para logs personalizados

### Flujo de Ejecución

```
1. @BeforeMethod
   ├─ Inicializar ChromeDriver
   ├─ Configurar opciones (headless, no-sandbox)
   ├─ Navegar a virtualpiano.net
   └─ Configurar esperas implícitas y explícitas

2. @Test (test1, test2, test3)
   ├─ Crear instancia de VirtualPianoPage
   ├─ Llamar playSong(SONG)
   │   └─ Para cada nota:
   │       ├─ tokenToDataNote("1si") → "1b"
   │       ├─ tokenToNote("1si") → "r"
   │       ├─ Validar: dataToSpanishNote("1b") → "1si"
   │       ├─ Actions.keyDown("r") + sleep(300ms)
   │       ├─ Actions.keyUp("r") + sleep(500ms)
   │       └─ Reporter.log() → Registro detallado
   └─ Verificar reproducción exitosa

3. @AfterMethod
   └─ driver.quit() → Cerrar navegador
```

## 🎯 Escenarios de Prueba

### Escenario 1: Reproducción Básica (`test1`)
**Objetivo**: Verificar que el sistema puede reproducir una melodía completa correctamente

**Precondiciones**:
- Navegador Chrome disponible
- Conexión a internet activa
- Piano virtual accesible en virtualpiano.net

**Pasos**:
1. Inicializar navegador y navegar a la aplicación
2. Crear instancia de VirtualPianoPage
3. Reproducir el Himno a la Alegría (15 notas)
4. Validar cada nota (entrada vs salida)

**Resultado Esperado**:
- ✅ Las 15 notas se reproducen sin errores
- ✅ Cada validación entrada/salida es exitosa
- ✅ Los logs muestran progreso correcto (Nota #1/15... Nota #15/15)
- ✅ El reporte HTML contiene todos los detalles

**Datos de Prueba**:
```java
SONG = "1si,1si,2do,2re,2re,2do,1si,1la,1sol,1sol,1la,1si,1si,1la,1la"
```

---

### Escenario 2: Reproducción Doble (`test2`)
**Objetivo**: Verificar que el sistema puede ejecutar la misma melodía múltiples veces consecutivamente

**Precondiciones**:
- Test ejecutado después de test1 (priority=2)
- Navegador recién inicializado (@BeforeMethod)

**Pasos**:
1. Reproducir la melodía completa (primera vez)
2. Reproducir la misma melodía inmediatamente después (segunda vez)
3. Validar ambas ejecuciones

**Resultado Esperado**:
- ✅ Ambas reproducciones completan sin errores
- ✅ Total de 30 notas procesadas (15 + 15)
- ✅ Sin degradación de rendimiento entre ejecuciones
- ✅ Validaciones exitosas en ambas iteraciones

**Propósito**: Detectar posibles problemas de estado o memory leaks

---

### Escenario 3: Secuencia Extendida (`test3`)
**Objetivo**: Verificar robustez del sistema con secuencias más largas y repetitivas

**Precondiciones**:
- Test ejecutado al final (priority=3)
- Sistema validado en escenarios anteriores

**Pasos**:
1. Reproducir una secuencia extendida con múltiples repeticiones
2. Validar manejo de secuencias largas
3. Verificar timing consistente a lo largo de toda la ejecución

**Resultado Esperado**:
- ✅ Todas las notas se reproducen correctamente
- ✅ No hay timeout ni errores de timing
- ✅ Logs mantienen formato consistente
- ✅ Validaciones exitosas para toda la secuencia

**Propósito**: Probar límites y estabilidad del sistema

---

### Matriz de Cobertura de Escenarios

| Escenario | Funcionalidad | Validación | Timing | Reporting |
|-----------|---------------|------------|--------|-----------|
| test1     | ✅ Básica     | ✅ Si     | ✅ Si  | ✅ Si    |
| test2     | ✅ Repetición | ✅ Si     | ✅ Si  | ✅ Si    |
| test3     | ✅ Extendida  | ✅ Si     | ✅ Si  | ✅ Si    |

## ✨ Buenas Prácticas Implementadas

### 1. **Page Object Model (POM)**
✅ **Implementación**: Separación completa entre lógica de pruebas (`VirtualPianoTest`) y lógica de página (`VirtualPianoPage`)

**Beneficios**:
- Reutilización de código
- Mantenimiento simplificado
- Abstracción de detalles de implementación
- Tests más legibles y expresivos

**Ejemplo**:
```java
// ❌ Sin POM
driver.findElement(By.cssSelector("[data-note='1b']")).click();

// ✅ Con POM
pianoPage.playSong("1si,1si,2do");
```

---

### 2. **DRY (Don't Repeat Yourself)**
✅ **Implementación**: Constantes reutilizables y métodos helper

**Ejemplo**:
```java
// Constante reutilizable en múltiples tests
public static final String SONG = "1si,1si,2do,2re...";

// Reutilización en diferentes tests
@Test(priority = 1)
public void test1() { pianoPage.playSong(SONG); }

@Test(priority = 2)
public void test2() { 
    pianoPage.playSong(SONG);
    pianoPage.playSong(SONG); // Reutilización
}
```

---

### 3. **Explícitas sobre Implícitas (Waits)**
✅ **Implementación**: Uso de `WebDriverWait` para condiciones específicas

```java
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".piano-container")));
```

**Ventajas**:
- Tiempos de espera optimizados
- Reducción de flakiness
- Mejor manejo de elementos dinámicos

---

### 4. **Gestión de Lifecycle del Navegador**
✅ **Implementación**: `@BeforeMethod` y `@AfterMethod` para aislamiento de tests

```java
@BeforeMethod
public void beforeTest() {
    // Navegador limpio para cada test
    driver = new ChromeDriver(options);
}

@AfterMethod
public void tearDown() {
    // Limpieza garantizada
    if (driver != null) {
        driver.quit();
    }
}
```

**Beneficios**:
- Tests independientes
- Sin contaminación entre ejecuciones
- Estado predecible

---

### 5. **Validación Automática (Fail-Fast)**
✅ **Implementación**: Validación de entrada vs salida con excepciones

```java
if (!inputNormalized.equals(spanishNote)) {
    throw new RuntimeException("Nota de entrada " + inputNormalized + 
                               " no coincide con nota convertida " + spanishNote);
}
```

**Ventajas**:
- Detección temprana de errores
- Feedback inmediato
- Prevención de falsos positivos

---

### 6. **Logging Detallado (Observability)**
✅ **Implementación**: Reporter API de TestNG con logs estructurados y HTML

```java
Reporter.log("🎹 Nota #" + noteNumber + "/" + total + "<br/>");
Reporter.log("&nbsp;&nbsp;&nbsp;📥 Entrada: " + input + "<br/>");
Reporter.log("&nbsp;&nbsp;&nbsp;📤 Salida: " + output + "<br/>");
```

**Beneficios**:
- Debugging facilitado
- Reportes profesionales
- Trazabilidad completa

---

### 7. **Configuración Externalizada**
✅ **Implementación**: TestNG.xml para configuración de suite

```xml
<suite name="Piano Virtual Suite" preserve-order="true">
    <test name="Piano Tests" preserve-order="true">
        <classes>
            <class name="com.pdcj.VirtualPianoTest">
                <methods>
                    <include name="test1"/>
                    <include name="test2"/>
                    <include name="test3"/>
                </methods>
            </class>
        </classes>
    </test>
</suite>
```

**Ventajas**:
- Configuración centralizada
- Fácil modificación sin cambiar código
- Ejecución ordenada garantizada

---

### 8. **Naming Conventions**
✅ **Implementación**: Nombres descriptivos y consistentes

```java
// Métodos de conversión claros
tokenToDataNote()      // 1si → 1b
dataToSpanishNote()    // 1b → 1si
tokenToNote()          // 1si → r

// Constantes con significado
NOTE_HOLD_MS = 300L;
NOTE_DELAY_MS = 500L;
```

---

### 9. **Separation of Concerns**
✅ **Implementación**: Responsabilidades bien definidas

| Clase | Responsabilidad |
|-------|-----------------|
| `VirtualPianoTest` | Orquestación de tests, setup/teardown |
| `VirtualPianoPage` | Interacción con el piano, conversiones |
| `Actions` (Selenium) | Simulación de eventos de teclado |
| `Reporter` (TestNG) | Generación de reportes |

---

### 10. **Version Control & Documentation**
✅ **Implementación**: 
- JavaDoc en todos los métodos públicos
- Comentarios inline explicativos
- README completo con ejemplos
- Git con commits descriptivos

**Ejemplo de JavaDoc**:
```java
/**
 * Reproduce una secuencia completa de notas separadas por comas.
 * Ejemplo: "1si,1si,2do,2re" reproduce 4 notas consecutivas
 * 
 * @param notes Secuencia de notas separadas por comas
 */
public void playSong(String notes) { ... }
```

---

### 11. **Configuración Robusta de Chrome**
✅ **Implementación**: Options para estabilidad

```java
ChromeOptions options = new ChromeOptions();
options.addArguments("--no-sandbox");           // Evitar problemas de permisos
options.addArguments("--disable-dev-shm-usage"); // Evitar problemas de memoria
// options.addArguments("--headless");          // Disponible para CI/CD
```

---

### 12. **Test Ordering Strategy**
✅ **Implementación**: Prioridades explícitas + preserve-order

```java
@Test(priority = 1) public void test1() { ... } // Test básico primero
@Test(priority = 2) public void test2() { ... } // Test intermedio
@Test(priority = 3) public void test3() { ... } // Test complejo al final
```

**Estrategia**: De simple a complejo para debugging eficiente

---

### Checklist de Buenas Prácticas

- [x] Page Object Model implementado
- [x] Tests independientes y aislados
- [x] Waits explícitas en lugar de sleeps arbitrarios
- [x] Validaciones automáticas (fail-fast)
- [x] Logging detallado y estructurado
- [x] Nombres descriptivos y consistentes
- [x] Configuración externalizada (TestNG.xml)
- [x] Constantes en lugar de magic numbers
- [x] Documentación completa (JavaDoc + README)
- [x] Manejo apropiado de excepciones
- [x] Cleanup de recursos (@AfterMethod)
- [x] DRY - Código reutilizable

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
