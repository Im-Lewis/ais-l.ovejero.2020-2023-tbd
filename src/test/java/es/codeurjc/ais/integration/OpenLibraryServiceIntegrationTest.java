package es.codeurjc.ais.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import es.codeurjc.ais.book.OpenLibraryService;
import es.codeurjc.ais.book.OpenLibraryService.BookData;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("OpenLibraryService integration tests")
public class OpenLibraryServiceIntegrationTest {

    OpenLibraryService OLS;
    @BeforeEach
    public void setUp(){
        OLS = new OpenLibraryService();
    }

//----------------------------------------------------------------------------------------------------------------------
    @Test
	public void obtainListOfBooksByTopicTest(){
       
        List<BookData> books = OLS.searchBooks("drama", 15);

        assertEquals(15, books.size());
  
    }

//----------------------------------------------------------------------------------------------------------------------
    @ParameterizedTest
    @CsvSource({"drama,15", "magic,15", "fantasy,15"})
    @DisplayName("Buscar un topic con limite")
    public void test(String topic, int limite){
        List<OpenLibraryService.BookData> res = OLS.searchBooks(topic, limite);

        assertThat(res.size()).isEqualTo(15);

        //pasamos todas las etiquetas a minusculas y recorremos la lista de etiquetas
        // para ver si esta en cada libro la etiqueta del tema
        for(int i=0; i<res.size(); i++){
            for (int j=0; j<res.get(i).subject.length; j++) {
                res.get(i).subject[j] = res.get(i).subject[j].toLowerCase();
            }
            assertThat(topic).isIn(Arrays.asList(res.get(i).subject));
        }
    }
    
//----------------------------------------------------------------------------------------------------------------------
    @ParameterizedTest
    @CsvSource({"OL8479867W, The Name of the Wind"})
    @DisplayName("Buscar un nombre por su identificador")
    public void test2(String id, String nombre){
        OpenLibraryService.BookData libro = OLS.getBook(id);

        assertDoesNotThrow(()->{OLS.getBook(id);});

        assertThat(libro.title).isEqualTo(nombre);
    }

}
