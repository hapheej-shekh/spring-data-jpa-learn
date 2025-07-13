package jpa.learn.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jpa.learn.beans.Comment;
import jpa.learn.services.CommentService;

@RestController
@RequestMapping("/api/comments")
public class CommentApi {

	@Autowired
	private CommentService commentService;
	
	/*	Comment having article detail for article_id	*/
	
	@PostMapping
	public ResponseEntity<Comment> addComment(@RequestBody Comment comment) {
		
		var outcome = commentService.addComment(comment);
		
		if(outcome!=null && outcome.getId()!=null)
			return new ResponseEntity<Comment>(outcome, HttpStatus.CREATED);
		
		return new ResponseEntity<Comment>(comment, HttpStatus.BAD_REQUEST);
	}
	
	@PutMapping
	public ResponseEntity<Comment> updateComment(@RequestBody Comment comment) {
		
		if(comment.getId()==null || comment.getArticle().getId()==null)
			return new ResponseEntity<Comment>(comment, HttpStatus.PARTIAL_CONTENT);
		
		int count = commentService.updateComment(comment);
		
		if(count>0)
			return new ResponseEntity<Comment>(comment, HttpStatus.CREATED);
		
		return new ResponseEntity<Comment>(comment, HttpStatus.BAD_REQUEST);
	}
	
	@DeleteMapping("/{articleId}/{commentId}")
	public ResponseEntity<String> deleteComment(@PathVariable("articleId") int articleId, 
			@PathVariable("commentId") int commentId) {
		
		int count = commentService.deleteComment(articleId, commentId);
		
		if(count>0)
			return new ResponseEntity<String>("Comment deleted, id: "+commentId+", articleId: "
					+articleId, HttpStatus.OK);
		
		return new ResponseEntity<String>("Failed to delete comment", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Comment> getComment(@PathVariable("id") int id) {
		
		var response = commentService.findById(id);
		
		if(response.isPresent())
			return new ResponseEntity<>(response.get(), HttpStatus.FOUND);
		
		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}
		
	@GetMapping(params="ids")
	public ResponseEntity<List<Comment>> getCommentByIds(@RequestParam("ids") List<Integer> ids) {
		
		List<Comment> response = commentService.findByIds(ids);
		
		if(response!=null)
			return new ResponseEntity<>(response, HttpStatus.FOUND);
		
		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}
	
	@GetMapping
	public ResponseEntity<List<Comment>> getAllComments() {
		
		List<Comment> response = commentService.findAll();
		
		if(response!=null)
			return new ResponseEntity<>(response, HttpStatus.FOUND);
		
		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}
}
