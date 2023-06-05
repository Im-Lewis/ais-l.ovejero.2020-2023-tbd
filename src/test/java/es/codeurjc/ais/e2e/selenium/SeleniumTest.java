package es.codeurjc.ais.e2e.selenium;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions; 
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.delete;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.assertj.core.api.Assertions.assertThat;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SeleniumTest {

    @LocalServerPort
    int port;

    private WebDriver driver;

	@BeforeAll
	public static void setupClass() {
		WebDriverManager.firefoxdriver().setup();
	}

	@BeforeEach
	public void setupTest() {
		FirefoxOptions options = new FirefoxOptions();
		options.setHeadless(true);
        	this.driver = new FirefoxDriver(options);
	}

	@AfterEach
	public void teardown() {
		if (this.driver != null) {
            this.driver.quit();
		}

		//eliminamos la review que crea el segundo test para que no interfiera en otros tests
		delete("http://localhost:"+this.port+"/api/books/OL15358691W/review/1");
    }

//----------------------------------------------------------------------------------------------------------------------
    @Test
    @DisplayName("Check that the default topic is fantasy")
	public void checkDefaultTopic() throws Exception {

        String topic = "fantasy";

        this.driver.get("http://localhost:"+this.port+"/");

        String title = driver.findElement(By.tagName("h1")).getText();
        
        assertEquals("Books (topic="+topic+")", title);
    }

//----------------------------------------------------------------------------------------------------------------------
	@ParameterizedTest
	@ValueSource(strings = {"drama"})
	@DisplayName("Buscar \"drama\" y comprobar etiqueta")
	public void test(String topic) {
		//Given
		given();

		//When
		driver.findElement(By.id("cuadro_texto")).sendKeys(topic);

		driver.findElement(By.id("search-button")).click();

		driver.findElement(By.xpath("/html/body/div[2]/div/a[1]")).click();

		//Then
		String etiqueta_drama = driver.findElement(By.id(topic)).getText();

		assertThat(etiqueta_drama).isEqualTo(topic);
	}

//----------------------------------------------------------------------------------------------------------------------
	@ParameterizedTest
	@CsvSource({"epic fantasy,The Way of Kings"})
	@DisplayName("Buscar \"epic fantasy\" crear una review y comprobar")
	public void test2(String topic, String name) {
		//Given
		given();

		//When
		crear_review(topic, name, 1);

		//Then
		//guardar los comentarios en una lista
		WebElement comentarios = driver.findElement(By.className("comment"));
		List<WebElement> lista_comentarios = comentarios.findElements(By.className("content"));
		//acceder al ultimo de la lista y comprobar que son correctos
		String user = lista_comentarios.get(lista_comentarios.size() - 1).findElement(By.className("author")).getText();
		String content = lista_comentarios.get(lista_comentarios.size() - 1).findElement(By.className("text")).getText();

		assertThat(user).isEqualTo("Nickname Review");
		assertThat(content).isEqualTo("Content Review");
	}

//----------------------------------------------------------------------------------------------------------------------
	@ParameterizedTest
	@CsvSource({"epic fantasy,Words of Radiance"})
	@DisplayName("Buscar \"epic fantasy\" crear una review vacia y comprobar")
	public void test3(String topic, String name){
		//Given
		given();

		//When
		crear_review(topic, name, 2);

		//Then
		assertThat(driver.findElement(By.id("error_message")).getText()).
				isEqualTo("Error at saving the review: empty fields");
		assertThat(driver.findElements(By.className("content"))).isEmpty();
	}

//----------------------------------------------------------------------------------------------------------------------
//FUNCIONES AUXILIARES
	public void given(){
		driver.get("http://localhost:" + this.port + "/");
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

//Funcion que crea una review
	public void crear_review(String tema, String nombre_libro, int test){
		driver.findElement(By.id("cuadro_texto")).sendKeys(tema);

		driver.findElement(By.id("search-button")).click();

		driver.findElement(By.linkText(nombre_libro)).click();

		String nombre_usuario = "", contenido_review = "";
		if (test == 1){
			nombre_usuario = "Nickname Review";
			contenido_review = "Content Review";
		}
		else if (test == 2){
			nombre_usuario = "Nickname Review";
			contenido_review = "";
		}

		driver.findElement(By.id("Nombre_Review")).sendKeys(nombre_usuario);
		driver.findElement(By.id("Contenido_Review")).sendKeys(contenido_review);
		driver.findElement(By.id("add-review")).click();
	}
    
}
