package jpa.learn.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import jpa.learn.beans.Article;

public interface ArticleRepository extends CrudRepository<Article, Integer> {

	/*	--- Named Query Access Way ---	*/
	
	// Option A: Method name matches convention (e.g., entity name + named query name)
    // Optional<Article> findByTitle(String title);
	
	// Option B: Explicitly reference the named query using @Query
    @Query(name="findByTitle") // Referencing the named query by its name
    Optional<Article> findArticleBySpecificTitle(String title);
	
	
    /* --- Named Parameters- @Param ---	*/
    
	//@Modifying: Indicates that the query is an update or delete query
	@Modifying
    @Query("UPDATE Article a SET a.title=:title, a.desc=:desc WHERE a.id=:id")
    int updateArticleById(@Param("title") String title, 
    		@Param("desc") String desc, @Param("id") Integer id);
	
	@Modifying
    @Query("UPDATE Article a SET a.desc = :newDesc WHERE a.id = :id")
    int updateArticleDescription(@Param("newDesc") String newDesc,
                                 @Param("id") Integer id);
		
	// Assuming description might not be unique
    List<Article> findByDesc(String desc);
    
    // --- By Multiple Fields (AND / OR) ---
    Optional<Article> findByAuthorAndTitle(String author, String title);
    List<Article> findByAuthorOrTitle(String author, String title);
    List<Article> findByAuthorAndDesc(String author, String desc);
    List<Article> findByAuthorOrDesc(String author, String desc);

    // --- By String Operations (for 'title' and 'desc') ---
    List<Article> findByDescContaining(String keyword);
    List<Article> findByAuthorContaining(String keyword);
        

    // --- Combinations with String Operations ---
    List<Article> findByAuthorAndTitleContaining(String author, String keyword);
    List<Article> findByAuthorIgnoreCaseAndTitleContainingIgnoreCase(String author, String keyword);

    // --- Ordering Results ---
    List<Article> findByAuthorOrderByTitleAsc(String author);
    List<Article> findByAuthor(String author, Sort sort);
    
    //OrderBy- Desc wont work since Desc is both Key & property in class
    //List<Article> findByAuthorOrderByDescDesc(String author);

    // --- Limiting Results ---
    Optional<Article> findFirstByAuthorOrderByTitleAsc(String author); // Find the first article by author, ordered by title

    // --- Custom Queries (if method name becomes too long or complex) ---
    // @Query("SELECT a FROM Article a WHERE a.author = :author AND a.title LIKE %:keyword%")
    // List<Article> searchArticlesByAuthorAndTitleKeyword(@Param("author") String author, @Param("keyword") String keyword);
    
    
    /*	Delete Methods by Fields	*/
    
    /**
     * Deletes all articles by a specific author.
     * @return The number of entities deleted.
     */
    @Modifying
    int deleteByAuthor(String author);

    /**
     * Deletes an article by its title.
     * If multiple articles have the same title, all will be deleted.
     * Consider using findByTitle and then deleteById if you expect unique titles
     * and want more control or error handling for non-existence.
     * @return The number of entities deleted.
     */
    @Modifying
    int deleteByTitle(String title);

    /**
     * Deletes articles by author and title.
     * @return The number of entities deleted.
     */
    @Modifying
    int deleteByAuthorAndTitle(String author, String title);

    /**
     * Deletes articles by description containing a keyword.
     * @param keyword The keyword to search in the description.
     * @return The number of entities deleted.
     */
    @Modifying
    int deleteByDescContaining(String keyword);

    // You can also use custom @Query for deletes if needed:
    // @Modifying
    // @Query("DELETE FROM Article a WHERE a.author = :author AND a.title LIKE %:titleKeyword%")
    // int deleteByAuthorAndTitleKeyword(@Param("author") String author, @Param("titleKeyword") String titleKeyword);

}
