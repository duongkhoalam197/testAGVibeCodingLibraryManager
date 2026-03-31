package com.example.testaglibrarymanager.service.book;

import com.example.testaglibrarymanager.model.request.BookRequest;
import com.example.testaglibrarymanager.model.dto.ServiceResult;
import com.example.testaglibrarymanager.mapper.BookMapper;
import com.example.testaglibrarymanager.repository.CategoryRepository;
import com.example.testaglibrarymanager.repository.BookRepository;
import com.example.testaglibrarymanager.model.entity.Book;
import com.example.testaglibrarymanager.util.exception.ErrorCode;
import com.example.testaglibrarymanager.model.dto.BookDto;
import com.example.testaglibrarymanager.model.entity.Category;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final BookMapper bookMapper;

    public BookServiceImpl(BookRepository bookRepository, 
                           CategoryRepository categoryRepository,
                           BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public ServiceResult<List<BookDto>> getAllBooks(Long categoryId) {
        List<Book> books;
        if (categoryId != null) {
            books = bookRepository.findByCategoryId(categoryId);
        } else {
            books = bookRepository.findAll();
        }
        return ServiceResult.success(books.stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList()));
    }

    @Override
    public ServiceResult<BookDto> getBookById(Long id) {
        return bookRepository.findById(id)
                .map(book -> ServiceResult.success(bookMapper.toDto(book)))
                .orElseGet(() -> ServiceResult.fail(ErrorCode.BOOK_NOT_FOUND, "Không tìm thấy sách với ID: " + id));
    }

    @Override
    @Transactional
    public ServiceResult<BookDto> createBook(BookRequest request) {
        Optional<Category> categoryOpt = categoryRepository.findById(request.categoryId());
        if (categoryOpt.isEmpty()) {
            return ServiceResult.fail(ErrorCode.CATEGORY_NOT_FOUND, "Không tìm thấy danh mục với ID: " + request.categoryId());
        }

        Book book = new Book();
        book.setTitle(request.title());
        book.setAuthor(request.author());
        book.setPrice(request.price());
        book.setCategory(categoryOpt.get());

        return ServiceResult.success(bookMapper.toDto(bookRepository.save(book)));
    }

    @Override
    @Transactional
    public ServiceResult<BookDto> updateBook(Long id, BookRequest request) {
        Optional<Book> bookOpt = bookRepository.findById(id);
        if (bookOpt.isEmpty()) {
            return ServiceResult.fail(ErrorCode.BOOK_NOT_FOUND, "Không tìm thấy sách với ID: " + id);
        }

        Optional<Category> categoryOpt = categoryRepository.findById(request.categoryId());
        if (categoryOpt.isEmpty()) {
            return ServiceResult.fail(ErrorCode.CATEGORY_NOT_FOUND, "Không tìm thấy danh mục với ID: " + request.categoryId());
        }

        Book book = bookOpt.get();
        book.setTitle(request.title());
        book.setAuthor(request.author());
        book.setPrice(request.price());
        book.setCategory(categoryOpt.get());

        return ServiceResult.success(bookMapper.toDto(bookRepository.save(book)));
    }

    @Override
    @Transactional
    public ServiceResult<Void> deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            return ServiceResult.fail(ErrorCode.BOOK_NOT_FOUND, "Không tìm thấy sách với ID: " + id);
        }
        try {
            bookRepository.deleteById(id);
            return ServiceResult.success(null);
        } catch (DataIntegrityViolationException e) {
            return ServiceResult.fail(ErrorCode.BOOK_BEING_BORROWED_CANNOT_DELETE, "Sách đang được mượn, không thể xóa!");
        }
    }
}

