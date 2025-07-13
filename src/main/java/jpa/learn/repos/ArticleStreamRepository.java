package jpa.learn.repos;

import java.util.stream.Stream;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;
import org.springframework.transaction.annotation.Transactional;

import jpa.learn.beans.Article;

public interface ArticleStreamRepository  extends CrudRepository<Article, Integer> {

	/*	--- Spring Data Jpa Streamable Examples ----	*/
	
	// Assuming author might not be unique
	Streamable<Article> findByAuthor(String author);
	
	// Assuming title is unique
	Streamable<Article> findByTitle(String title);
	
    Streamable<Article> findByTitleContaining(String keyword); // LIKE %keyword%
    Streamable<Article> findByTitleContainingIgnoreCase(String keyword); // Case-insensitive LIKE %keyword%
    Streamable<Article> findByTitleStartingWith(String prefix); // LIKE prefix%
    Streamable<Article> findByTitleEndingWith(String suffix); // LIKE %suffix

    /*	--- Java 8 Stream Examples ----	*/
    
    /**
     * IMPORTANT: This method MUST be called within a transactional context,
     * and the Stream MUST be consumed within that same context.
     */
    @Transactional(readOnly=true)
    Stream<Article> readAllByTitleNotNull();
}
