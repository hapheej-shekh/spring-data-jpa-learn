package jpa.learn.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jpa.learn.beans.Comment;
import jpa.learn.repos.CommentRepository;

@Service
public class CommentService {

	@Autowired
	private CommentRepository commentRepo;
	
	
	@Transactional(isolation=Isolation.READ_COMMITTED, 
			propagation=Propagation.REQUIRED, timeout=2)
	public Comment addComment(Comment comment) {
		
		return commentRepo.save(comment);
	}

	@Transactional(isolation=Isolation.READ_COMMITTED, 
			propagation=Propagation.REQUIRED, timeout=2)
	public int updateComment(Comment comment) {
		
		return commentRepo.updateCommentById(comment.getDesc(), comment.getId(), 
				comment.getArticle().getId());
	}

	@Transactional(isolation=Isolation.READ_COMMITTED, 
			propagation=Propagation.REQUIRED, timeout=2)
	public int deleteComment(int articleId, int commentId) {
		
		return commentRepo.deleteCommentById(commentId, articleId);
	}

	@Transactional(isolation=Isolation.READ_COMMITTED, 
			propagation=Propagation.NOT_SUPPORTED, timeout=2, readOnly=true)
	public Optional<Comment> findById(int id) {

		return commentRepo.findById(id);
	}

	@Transactional(isolation=Isolation.READ_COMMITTED, 
			propagation=Propagation.SUPPORTS, timeout=2, readOnly=true)
	public List<Comment> findByIds(List<Integer> ids) {
		
		return commentRepo.findAllById(ids);
	}

	@Transactional(isolation=Isolation.READ_COMMITTED, 
			propagation=Propagation.SUPPORTS, timeout=2, readOnly=true)
	public List<Comment> findAll() {

		return commentRepo.findAll();
	}
}
