package es.codeurjc.ais.unitary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import es.codeurjc.ais.notification.NotificationService;
import es.codeurjc.ais.review.Review;
import es.codeurjc.ais.review.ReviewRepository;
import es.codeurjc.ais.review.ReviewService;

import es.codeurjc.ais.book.Book;
import es.codeurjc.ais.book.BookDetail;
import es.codeurjc.ais.book.BookService;
import es.codeurjc.ais.book.OpenLibraryService;
import es.codeurjc.ais.book.OpenLibraryService.BookData;
import es.codeurjc.ais.notification.NotificationService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.web.client.HttpClientErrorException;

@DisplayName("BookService Unitary tests")
public class ReviewServiceUnitaryTest {

    BookService BS;
    OpenLibraryService OLS;
    NotificationService NS;

    @BeforeEach
    public void setUp() {
        OLS = mock(OpenLibraryService.class);
        NS = mock(NotificationService.class);
        this.BS = new BookService(OLS, NS);
    }

//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    public void getReviewsByIdUnitTest(){

        Review review1 = new Review();
        review1.setBookId("123A");
        review1.setNickname("nickname1");
        review1.setContent("content1");

        Review review2 = new Review();
        review2.setBookId("123A");
        review2.setNickname("nickname2");
        review2.setContent("content2");

        ReviewRepository reviewRepository = mock(ReviewRepository.class);
        when(reviewRepository.findByBookId("123A")).thenReturn(Arrays.asList(review1, review2));
        NotificationService notificationService = mock(NotificationService.class);
        ReviewService reviewService = new ReviewService(reviewRepository, notificationService);

        assertEquals(2, reviewService.findByBookId("123A").size());


    }

//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @ParameterizedTest
    @ValueSource(strings = {"magic"})
    @DisplayName("Se utiliza el metodo findAll")
    public void test_FindAll_BookService(String topic) {
        //Lista para la salida del metodo findAll
        List<Book> libros = new ArrayList<>();

        //Lista de datos de los libros
        List<BookData> datos_libros = new ArrayList<>();
        BookData libro1 = new BookData ("titulo_1", "00/00/01", "descripcion_1", null, null);
        BookData libro2 = new BookData ("titulo_2", "00/00/02", "descripcion_2", null, null);
        datos_libros.add(libro1);
        datos_libros.add(libro2);

        when(OLS.searchBooks(topic, 10)).thenReturn(datos_libros);

        //When
        libros = BS.findAll(topic);

        //Then
        assertThat(libros.size()).isEqualTo(2);

        verify(NS).info("The books have been loaded: "+libros.size()+" books | query: "+topic);
    }

//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @ParameterizedTest
    @ValueSource(strings = {"00/00/00"})
    @DisplayName("Se utiliza el metodo findById")
    public void test_FindById_BookService(String ident) {
        Optional<BookDetail> libro;

        when(OLS.getBook(ident)).thenThrow(HttpClientErrorException.class);

        //When
        libro =  BS.findById(ident);

        //Then
        assertThat(libro).isEqualTo(Optional.empty());

        verify(NS).error("The book ("+ident+") has not been loaded due "+ anyString());
    }

}
