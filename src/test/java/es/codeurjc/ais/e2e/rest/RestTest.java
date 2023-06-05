package es.codeurjc.ais.e2e.rest;

import static io.restassured.RestAssured.when;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;
import static io.restassured.path.json.JsonPath.from;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("REST tests")
public class RestTest {

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

//----------------------------------------------------------------------------------------------------------------------
    @Test
	public void getAllBooks() throws Exception {

        when()
            .get("/api/books/?topic=drama").
        then()
            .assertThat()
                .statusCode(200)
                .contentType("application/json");
    
    }

//----------------------------------------------------------------------------------------------------------------------
    @ParameterizedTest
    @DisplayName("Test que comprueba el tamaño de la respuesta al buscar por tema")
    @CsvSource({"drama, 10"})
    public void test(String tema, int tamanyo){
        //Busqueda de los libros por tema
        Response respuesta = libros_tema(tema);

        //Tamaño de la lista de libros al buscar por un tema
        int tamanyo_respuesta = from(respuesta.getBody().asString()).getList("$").size();

        //Comprobacion del tamaño de la lista de libros devueltos
        assertThat(tamanyo_respuesta).isEqualTo(tamanyo);
    }

    @ParameterizedTest
    @ValueSource(strings = {"fantasy"})
    @DisplayName("Test que busca un tema comprueba si se crean las reviews en un libro")
    public void test2(String tema) throws JSONException {
        //Busqueda de los libros por tema
        Response respuesta = libros_tema(tema);

        String[] r = from(respuesta.getBody().asString()).getString("id").substring(1).split(",");
        String primer_libro = r[0];

        anyadir_review(primer_libro);

        tamanyo_reviews(primer_libro, 1);
    }

//----------------------------------------------------------------------------------------------------------------------
    @ParameterizedTest
    @ValueSource(strings = {"OL15358691W"})
    @DisplayName("Test que busca un libro por id y comprueba que se borra una review")
    public void test3(String ident) throws JSONException{
        //Añadimos la nueva review
        Response respuesta = anyadir_review(ident);
        int ident_review= from(respuesta.getBody().asString()).get("id");

        //Borramos la nueva review que acabamos de añadir
        given().
                pathParams("id_libro",ident,"ident_review", ident_review).
                when().
                delete("/api/books/{id_libro}/review/{ident_review}").
                then().
                statusCode(204);

        tamanyo_reviews(ident, 0);
    }

//----------------------------------------------------------------------------------------------------------------------
//FUNCIONES AUXILIARES
    //Funcion que devuelve los libros de un tema
    public Response libros_tema(String tema){
        return (
                given().
                        pathParam("topic", tema).
                        when().
                        get("/api/books/?topic={topic}").
                        then().
                        statusCode(200).
                        extract().response().andReturn());
    }

//Funcion que crea y añade una review a un libro
    public Response anyadir_review(String libro_ident) throws JSONException{
        //Nueva review que queremos insertar
        JSONObject body_review = new JSONObject();
        //nickname
        body_review.put("nickname", "nick");
        //review
        body_review.put("content", "content review");
        //id del libro
        body_review.put("id_libro", libro_ident);

        //Añadimos la nueva review
        return(
                given().
                        request().
                        body(body_review.toString()).
                        contentType(ContentType.JSON).
                        pathParam("id_libro", libro_ident).
                        when().
                        post("/api/books/{id_libro}/review").
                        then().
                        statusCode(201).
                        body("nickname", equalTo("nick")).
                        body("content", equalTo("content review")).
                        body("bookId", equalTo(libro_ident)).
                        extract().response().andReturn()
        );
    }

//Funcion que comprueba el tamaño de la lista de reviews de un libro
    public void tamanyo_reviews(String libro_ident, int tamanyo){
        //comprobamos que la lista de review del libro es nula para saber que se ha eliminado la nueva review
        given().
                pathParam("id_libro",libro_ident).
                when().
                get("http://localhost:" + this.port + "/api/books/{id_libro}").
                then().
                assertThat().body("reviews", hasSize(tamanyo));
    }
    
}