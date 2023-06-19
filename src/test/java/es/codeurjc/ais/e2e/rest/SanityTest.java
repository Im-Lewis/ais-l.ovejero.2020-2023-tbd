package es.codeurjc.ais.e2e.rest;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
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
    private static final int INTENTOS = 8;
    private static final long TIEMPO_ESPERA = 10000;

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

        boolean desplegada = false;
        for(int i=0; i<INTENTOS; i++){
            Response response = given()
                    .pathParam("id_libro", ident)
                    .when()
                    .get("/books/{id_libro}")
                    .then()
                    .extract().response();

            if(response.getStatusCode() == 200){
                desplegada = true;
                break;
            }

            try{
                Thread.sleep(TIEMPO_ESPERA);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }

        // Comprobar que esta desplegada
        assertTrue(desplegada);

        //Obtenemos el libro correspondiente al id que pasamos como parametro
        Response libro =
            given().
                pathParam("id_libro", ident).
            when()
                .get("/books/{id_libro}").
            then()
                .assertThat()
                    .statusCode(200).
                    extract().response();

        String respuesta = libro.getBody().asString();
        System.out.println("Cuerpo de respuesta: " + respuesta);

        JsonPath jsonpath = new JsonPath(respuesta); 
        String descripcion = jsonpath.getString("description");

        //Obtenemos la descripcion del libro con el id correspondiente
        //String descripcion = from(libro.getBody().asString()).getString("description");

        //Comprueba que la longitud de la descripcion del libro es menor o igual que 953
        assertTrue(descripcion.length() <= 3000);
    }
}
