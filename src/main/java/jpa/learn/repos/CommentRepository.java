package jpa.learn.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jpa.learn.beans.Article;
import jpa.learn.beans.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

	@Modifying
	@Query("UPDATE Comment c SET c.desc = :desc WHERE c.id = :commentId AND c.article.id = :articleId")
	int updateCommentById(@Param("desc") String desc, 
			@Param("commentId") Integer commentId, @Param("articleId") Integer articleId);
	
	@Modifying
	@Query("DELETE FROM Comment c WHERE c.id = :commentId AND c.article.id = :articleId")
	int deleteCommentById(@Param("commentId") Integer commentId, @Param("articleId") Integer articleId);
	
    // --- By Single Field ---
    Optional<Comment> findById(Integer id);
    List<Comment> findByAuthor(String author);
    List<Comment> findByDesc(String desc);
    
    
    /* --- By Multiple Fields (AND / OR) ---	*/
    List<Comment> findByAuthorAndDesc(String author, String desc);
    List<Comment> findByAuthorOrDesc(String author, String desc);

    /* --- By String Operations (for 'desc' and 'author') ---	*/
    List<Comment> findByDescContaining(String keyword);
    List<Comment> findByAuthorContainingIgnoreCase(String keyword);

    
    /* --- By Relationship Fields (Navigating through 'article' and 'parentComment') --- */
    
    // Comments belonging to a specific Article
    List<Comment> findByArticle(Article article); // Pass the Article object
    List<Comment> findByArticleId(Integer articleId); // Pass the Article's ID directly

    // Comments by author within a specific Article
    List<Comment> findByArticleAndAuthor(Article article, String author);
    List<Comment> findByArticleIdAndAuthor(Integer articleId, String author);

    // Top-level comments (comments with no parent) for an Article
    List<Comment> findByArticleAndParentCommentIsNull(Article article);
    List<Comment> findByArticleIdAndParentCommentIsNull(Integer articleId);

    // Replies to a specific parent Comment
    List<Comment> findByParentComment(Comment parentComment); // Pass the parent Comment object
    List<Comment> findByParentCommentId(Integer parentCommentId); // Pass the parent Comment's ID directly

    // Comments by author that are replies to a specific parent Comment
    List<Comment> findByParentCommentAndAuthor(Comment parentComment, String author);
    List<Comment> findByParentCommentIdAndAuthor(Integer parentCommentId, String author);

    // --- Combinations across relationships and fields ---
    // Find comments by author, belonging to a specific article, and containing a keyword in desc
    List<Comment> findByArticleIdAndAuthorAndDescContaining(Integer articleId, String author, String keyword);

    // Find replies to a specific comment by author and description keyword
    List<Comment> findByParentCommentIdAndAuthorAndDescContaining(Integer parentCommentId, String author, String keyword);

    
    
    /* --- Custom Queries (for more complex scenarios or joins) ---	*/
    // Example: Get comments for an article where the comment's author is also the article's author
    // @Query("SELECT c FROM Comment c JOIN c.article a WHERE a.id = :articleId AND c.author = a.author")
    // List<Comment> findArticleAuthorCommentsForArticle(@Param("articleId") Integer articleId);

    // Example: Get all comments (top-level and replies) for a given article, ordered by creation date
    // (assuming a 'createdAt' field exists in Comment)
    // @Query("SELECT c FROM Comment c WHERE c.article.id = :articleId ORDER BY c.createdAt ASC")
    // List<Comment> findAllCommentsForArticleOrderedByDate(@Param("articleId") Integer articleId);

    
    
    /*	--- Delete Methods by Fields ---	*/
    
    /**
     * Deletes all comments by a specific author.
     * @return The number of entities deleted.
     */
    @Modifying
    int deleteByAuthor(String author);

    /**
     * Deletes all comments with a specific description.
     * @return The number of entities deleted.
     */
    @Modifying
    int deleteByDesc(String desc);

    /**
     * Deletes comments by author and description.
     * @return The number of entities deleted.
     */
    @Modifying
    int deleteByAuthorAndDesc(String author, String desc);

    /**
     * Deletes comments belonging to a specific article.
     * @param articleId The ID of the article whose comments should be deleted.
     * @return The number of entities deleted.
     */
    @Modifying
    int deleteByArticleId(Integer articleId);

    /**
     * Deletes comments by author within a specific article.
     * @return The number of entities deleted.
     */
    @Modifying
    int deleteByArticleIdAndAuthor(Integer articleId, String author);

    /**
     * Deletes replies to a specific parent comment.
     * @param parentCommentId The ID of the parent comment whose replies should be deleted.
     * @return The number of entities deleted.
     */
    @Modifying
    int deleteByParentCommentId(Integer parentCommentId);

    // You can also use custom @Query for deletes if needed, especially for complex joins:
    // @Modifying
    // @Query("DELETE FROM Comment c WHERE c.author = :author AND c.article.id = :articleId")
    // int deleteCommentsByAuthorAndArticle(@Param("author") String author, @Param("articleId") Integer articleId);

}
