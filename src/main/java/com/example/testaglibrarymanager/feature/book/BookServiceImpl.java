package com.example.testaglibrarymanager.feature.book;

import com.example.testaglibrarymanager.exception.ResourceNotFoundException;
import com.example.testaglibrarymanager.feature.book.dto.BookRequest;
import com.example.testaglibrarymanager.feature.book.dto.BookResponse;
import com.example.testaglibrarymanager.feature.category.Category;
import com.example.testaglibrarymanager.feature.category.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    public BookServiceImpl(BookRepository bookRepository, CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<BookResponse> getAllBooks(Long categoryId) {
        List<Book> books;
        if (categoryId != null) {
            books = bookRepository.findByCategoryId(categoryId);
        } else {
            books = bookRepository.findAll();
        }
        return books.stream()
                .map(BookResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
        return BookResponse.fromEntity(book);
    }

    @Override
    @Transactional
    public BookResponse createBook(BookRequest request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.categoryId()));

        Book book = new Book();
        book.setTitle(request.title());
        book.setAuthor(request.author());
        book.setPrice(request.price());
        book.setCategory(category);

        return BookResponse.fromEntity(bookRepository.save(book));
    }

    @Override
    @Transactional
    public BookResponse updateBook(Long id, BookRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.categoryId()));

        book.setTitle(request.title());
        book.setAuthor(request.author());
        book.setPrice(request.price());
        book.setCategory(category);

        return BookResponse.fromEntity(bookRepository.save(book));
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book", "id", id);
        }
        bookRepository.deleteById(id);
    }
}
