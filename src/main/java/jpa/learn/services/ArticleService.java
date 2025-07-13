package jpa.learn.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jpa.learn.beans.Article;
import jpa.learn.beans.Comment;
import jpa.learn.repos.ArticleRepository;

@Service
public class ArticleService {

	@Autowired
	private ArticleRepository articleRepo;
	
	
	@Transactional(isolation=Isolation.READ_COMMITTED, 
			propagation=Propagation.REQUIRED, timeout=2)
	public Article createArticle(Article article) {
		
		setCommentRelationships(article, article.getComments());
		
		return articleRepo.save(article);
	}
	
	@Transactional(isolation=Isolation.READ_COMMITTED, 
			propagation=Propagation.REQUIRED, timeout=2)
	public int updateArticle(Article article) {
		
		if(articleRepo.existsById(article.getId()))
			return articleRepo.updateArticleById(article.getTitle(), article.getDesc(), article.getId());
		else
			return -1;
	}
	
	@Transactional(isolation=Isolation.READ_COMMITTED, 
			propagation=Propagation.REQUIRED, timeout=2)
    public Article updateArticle(Integer articleId, String newTitle, String newDesc) {
        
		Optional<Article> optionalArticle = articleRepo.findById(articleId);

        if (optionalArticle.isPresent()) {
            Article existingArticle = optionalArticle.get();
            existingArticle.setTitle(newTitle);
            existingArticle.setDesc(newDesc);
            return articleRepo.save(existingArticle);
        } else {
            System.err.println("Article with ID " + articleId + " not found for update.");
            return null;
        }
    }
	
	@Transactional(isolation=Isolation.READ_COMMITTED, 
			propagation=Propagation.REQUIRED, timeout=2)
	public boolean deleteArticle(int id) {
		
		if(articleRepo.existsById(id)) {
			articleRepo.deleteById(id);
			return true;
		}
		return false;
	}
	
	@Transactional(readOnly=true)
	public Optional<Article> findById(int id) {
		
		return articleRepo.findById(id);
	}
	
	@Transactional(readOnly=true)
	public List<Article> findByIds(List<Integer> ids) {
		
		List<Article> articles = new ArrayList<>();
		
		Iterable<Article> outcome = articleRepo.findAllById(ids);
		
		var itr = outcome.iterator();
		
		while(itr.hasNext())
			articles.add(itr.next());
		
		return articles;
	}

	@Transactional(readOnly=true)
	public List<Article> findAll() {
		
		List<Article> articles = new ArrayList<>();
		
		Iterable<Article> outcome = articleRepo.findAll();
		
		var itr = outcome.iterator();
		
		while(itr.hasNext())
			articles.add(itr.next());
		
		return articles;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
            timeout = 2, readOnly = true)
	public List<Article> getArticlesByAuthorOrderedByDesc(String author) {
		
		// Create a Sort object to specify descending order for the 'desc' property
		Sort sortByDesc = Sort.by("desc").descending();
		return articleRepo.findByAuthor(author, sortByDesc);
	}
	
	private void setCommentRelationships(Article article, List<Comment> comments) {
		
	    setCommentRelationships(article, comments, null);
	}
	
	private void setCommentRelationships(Article article, List<Comment> comments, Comment parentComment) {
        
		if (comments == null || comments.isEmpty())
            return;

        for (Comment comment : comments) {
        	
            // All comments, whether top-level or replies, must belong to an Article.
            // This line fixes the 'Comment.article' null issue.
            comment.setArticle(article);

            // Set the parent Comment reference. This will be null for top-level comments.
            comment.setParentComment(parentComment);

            // Recursively process replies, passing the current comment as their parent.
            if (comment.getReplies() != null && !comment.getReplies().isEmpty())
            	setCommentRelationships(article, comment.getReplies(), comment);
        }
    }
}
