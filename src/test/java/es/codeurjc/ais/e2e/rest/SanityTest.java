package es.codeurjc.ais.e2e.rest;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Sanity Test")
public class SanityTest {

    String host;

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

//----------------------------------------------------------------------------------------------------------------------
    @ParameterizedTest
    @ValueSource(strings = {"OL27479W"})
    @DisplayName("Test que busca un libro por id")
    public void sanity_test(String ident) throws JSONException{
        //Recibimos el parametro del host donde esta lanzada la aplicacion
        String actual_host = System.getProperty("host");
        assertNotNull(actual_host);
        this.host = actual_host;

        //Obtenemos el libro correspondiente al id que pasamos como parametro
        Response libro =
            given().
                pathParam("id_libro", ident).
            when()
                .get(actual_host + "/books/{id_libro}").
            then()
                .assertThat()
                    .statusCode(200).
                    extract().response().andReturn();

        //Obtenemos la descripcion del libro con el id correspondiente
        String descripcion = from(libro.getBody().asString()).getString("description");

        //Comprueba que la longitud de la descripcion del libro es menor o igual que 953
        assertTrue(descripcion.length() <= 953);
    }
}
