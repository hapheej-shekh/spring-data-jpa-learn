package jpa.learn.services;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jpa.learn.beans.Article;
import jpa.learn.repos.ArticleStreamRepository;

@Service
public class ArticleStreamService {

	@Autowired
	private ArticleStreamRepository articleStreamRepo;
	
	
	
	/**
	 * @param keyword, Articles title contains keywords like %Winter%
	 */
	public List<Article> getArticleByTitle(String keyword) {
		
		return articleStreamRepo.findByTitleContaining(keyword).toList();
	}
	
	/**
	 * @param keyword, Articles title contains keywords like %Winter%, case insensitive
	 */
	public List<Article> getArticleByTitleIgnoreCase(String keyword) {
		
		return articleStreamRepo.findByTitleContainingIgnoreCase(keyword).toList();
	}
	
	/**
	 * @param keyword, Articles title starts% & %ends with keyword
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
            timeout = 2, readOnly = true)
	public List<Article> getArticleByTitleCombination(String keyword) {
		
		Streamable<Article> res = articleStreamRepo.findByTitleStartingWith(keyword)
				.and(articleStreamRepo.findByTitleEndingWith(keyword));
		
		return res.toList();
	}
	
	/**
	 * @param auther, find Article by author along with
	 * @param title, find Article with title
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
            timeout = 2, readOnly = true)
	public
	List<Article> getArticleByAuthorAndTitle(String author, String title){
	
		Streamable<Article> res = articleStreamRepo.findByAuthor(author)
				.and(articleStreamRepo.findByTitle(title));
		
		return res.toList();
	}
	
    /**
     * Retrieves all articles where the title is not null.
     * The Stream is consumed and converted to a List within this transactional method
     * to ensure the underlying database resources are properly closed.
     *
     * @return A list of Article entities with non-null titles.
     */
    @Transactional(isolation = Isolation.READ_COMMITTED,
                   propagation = Propagation.REQUIRED,
                   timeout = 5, // Increased timeout slightly as stream consumption might take longer
                   readOnly = true)
    public List<Article> getArticlesByTitleNotNull() {
    	
        /* It's crucial to consume the stream within the transactional context,
           Using try-with-resources ensures the stream is closed properly */
    	
        try (Stream<Article> articleStream = articleStreamRepo.readAllByTitleNotNull()) {
            
        	return articleStream.collect(Collectors.toList());
        }
    }

}
