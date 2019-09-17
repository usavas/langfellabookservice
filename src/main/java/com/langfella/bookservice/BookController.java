package com.langfella.bookservice;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class BookController {

    private static ArrayList<Book> BOOKS = new ArrayList<>();
    private static ArrayList<Book> BOOKS_MIN = new ArrayList<>();

    public BookController() throws IOException {
            Type listType = new TypeToken<List<Book>>() {}.getType();

            File fileJson = new File("/usr/app/books.json");
            File fileJsonMin = new File("/usr/app/books_min.json");

            BOOKS = new Gson().fromJson(FileUtils.readFileToString(fileJson), listType);
            BOOKS_MIN = new Gson().fromJson(FileUtils.readFileToString(fileJsonMin), listType);
    }

    @GetMapping("/books/{title}")
    public String getBookByFullTitle(@PathVariable String title){
        Book book = BOOKS
                .stream()
                .filter(b-> b.getTitle().equals(title))
                .findFirst()
                .get();
        return new Gson().toJson(book);
    }

    @GetMapping(value = "/books/image/{image}",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@PathVariable String image) throws IOException {
        if (!image.endsWith(".jpg")){
            image +=".jpg";
        }

        InputStream input = new FileInputStream(new File(Paths.get("/usr/app/images/", image).toString()));
        return IOUtils.toByteArray(input);
    }

    @GetMapping("/books")
    public String getBooks() throws IOException {
        return new Gson().toJson(BOOKS_MIN);
    }

    @GetMapping("/books/author/{author}")
    public String getBookByAuthor(@PathVariable String author){
        ArrayList<Book> books = (ArrayList<Book>) BOOKS_MIN
                .stream()
                .filter(b-> b.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
        return new Gson().toJson(books);
    }

    @GetMapping("/books/title/{title}")
    public String getBookByTitle(@PathVariable String title){
        ArrayList<Book> books = (ArrayList<Book>) BOOKS_MIN
                .stream()
                .filter(b-> b.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
        return new Gson().toJson(books);
    }

    @GetMapping("/books/genre/{genre}")
    public String getBooksByGenre(@PathVariable String genre) {
        ArrayList<Book> booksToReturn = (ArrayList<Book>) BOOKS_MIN
                .stream()
                .filter(b-> {
                    if (b.getGenre() != null){
                        if (b.getGenre().equals(genre)){
                            return true;
                        };
                    }
                    return false; })
                .collect(Collectors.toList());

        return new Gson().toJson(booksToReturn);
    }

    @GetMapping("/books/level/{level}")
    public String getBooksByLevel(@PathVariable String level) {
        ArrayList<Book> booksToReturn = (ArrayList<Book>) BOOKS_MIN
                .stream()
                .filter(b-> {
                    if (b.getLevel() != null){
                        if (b.getLevel().equals(level)){
                            return true;
                        };
                    }
                    return false; })
                .collect(Collectors.toList());

        return new Gson().toJson(booksToReturn);
    }

    @GetMapping("/books/levelletter/{levelLetter}")
    public String getBooksByLevelLetter(@PathVariable String levelLetter) {
        ArrayList<Book> booksToReturn = (ArrayList<Book>) BOOKS_MIN
                .stream()
                .filter(b-> {
                    if (b.getLevelLetter() != null){
                        if (b.getLevelLetter().equals(levelLetter)){
                            return true;
                        };
                    }
                    return false; })
                .collect(Collectors.toList());

        return new Gson().toJson(booksToReturn);
    }
}
