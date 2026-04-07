package com.example.testaglibrarymanager.service.book;

import com.example.testaglibrarymanager.model.request.BookRequest;
import com.example.testaglibrarymanager.model.dto.ServiceResult;
import com.example.testaglibrarymanager.config.KafkaConfig;
import com.example.testaglibrarymanager.mapper.BookMapper;
import com.example.testaglibrarymanager.messaging.BookEventProducer;
import com.example.testaglibrarymanager.repository.CategoryRepository;
import com.example.testaglibrarymanager.repository.BookRepository;
import com.example.testaglibrarymanager.model.entity.Book;
import com.example.testaglibrarymanager.util.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

import com.example.testaglibrarymanager.model.dto.BookDto;
import com.example.testaglibrarymanager.model.dto.BookEventDto;
import com.example.testaglibrarymanager.model.entity.Category;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final BookMapper bookMapper;
    private final BookEventProducer bookEventProducer;

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
            return ServiceResult.fail(ErrorCode.CATEGORY_NOT_FOUND,
                    "Không tìm thấy danh mục với ID: " + request.categoryId());
        }

        Book book = new Book();
        book.setTitle(request.title());
        book.setAuthor(request.author());
        book.setPrice(request.price());
        book.setCategory(categoryOpt.get());

        Book savedBook = bookRepository.save(book);

        BookEventDto bookEvent = new BookEventDto(
                KafkaConfig.BOOK_CREATED_TOPIC,
                savedBook.getId(),
                savedBook.getTitle(),
                savedBook.getAuthor(),
                request.categoryId(),
                LocalDateTime.now());

        bookEventProducer.publishBookEvent(KafkaConfig.BOOK_CREATED_TOPIC, bookEvent);

        return ServiceResult.success(bookMapper.toDto(savedBook));
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
            return ServiceResult.fail(ErrorCode.CATEGORY_NOT_FOUND,
                    "Không tìm thấy danh mục với ID: " + request.categoryId());
        }

        Book book = bookOpt.get();
        book.setTitle(request.title());
        book.setAuthor(request.author());
        book.setPrice(request.price());
        book.setCategory(categoryOpt.get());

        Book savedBook = bookRepository.save(book);
        BookEventDto bookEventDto = new BookEventDto(
                KafkaConfig.BOOK_UPDATED_TOPIC,
                savedBook.getId(),
                savedBook.getTitle(),
                savedBook.getAuthor(),
                request.categoryId(),
                LocalDateTime.now());
        bookEventProducer.publishBookEvent(KafkaConfig.BOOK_UPDATED_TOPIC, bookEventDto);

        return ServiceResult.success(bookMapper.toDto(savedBook));
    }

    @Override
    @Transactional
    public ServiceResult<Void> deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            return ServiceResult.fail(ErrorCode.BOOK_NOT_FOUND, "Không tìm thấy sách với ID: " + id);
        }
        try {
            Optional<Book> bookDeleteOptional = bookRepository.findById(id);
            Book bookDelete = bookDeleteOptional.orElse(null);

            BookEventDto bookEventDto = new BookEventDto(
                    KafkaConfig.BOOK_DELETED_TOPIC,
                    bookDelete.getId(),
                    bookDelete.getTitle(),
                    bookDelete.getAuthor(),
                    bookDelete.getCategory().getId(),
                    LocalDateTime.now());

            bookRepository.deleteById(id);
            bookEventProducer.publishBookEvent(KafkaConfig.BOOK_DELETED_TOPIC, bookEventDto);
            return ServiceResult.success(null);
        } catch (DataIntegrityViolationException e) {
            return ServiceResult.fail(ErrorCode.BOOK_BEING_BORROWED_CANNOT_DELETE,
                    "Sách đang được mượn, không thể xóa!");
        }
    }
}
