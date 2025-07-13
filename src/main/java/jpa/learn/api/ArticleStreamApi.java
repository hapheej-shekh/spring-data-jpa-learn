package jpa.learn.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jpa.learn.beans.Article;
import jpa.learn.services.ArticleStreamService;

@RestController
@RequestMapping("/api/articles")
public class ArticleStreamApi {

	@Autowired
	private ArticleStreamService streamService;
	
	
	/**
	 * Handles GET requests to retrieve Articles by both author and title.
	 * URI: GET /api/articles?author={name}&title={title}
	 *
	 * @param author The author's name from the request parameter.
	 * @param title The article's title from the request parameter.
	 * @return ResponseEntity with a list of Articles and HttpStatus.OK (200) if found,
	 * or HttpStatus.NOT_FOUND (404) if no articles are found for the given criteria.
	 */
	@GetMapping(params={"author", "title"})
	public ResponseEntity<List<Article>> getArticleByAuthorAndTitle(
			@RequestParam("author") String author, @RequestParam("title") String title) {
		
		List<Article> response = streamService.getArticleByAuthorAndTitle(author, title);
		
		if (response != null && !response.isEmpty())
			return new ResponseEntity<>(response, HttpStatus.OK);

		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}
	
	/**
	 * Handles GET requests to retrieve all Articles with non-null titles.
	 * URI: GET /api/articles/by-title-not-null
	 *
	 * @return ResponseEntity with a list of Articles and HttpStatus.OK (200) if found,
	 * or HttpStatus.OK with an empty list if no articles match.
	 */
	@GetMapping("/by-title-not-null") // Unique path to avoid ambiguity
	public ResponseEntity<List<Article>> getArticlesByTitleNotNull() {
		
		List<Article> response = streamService.getArticlesByTitleNotNull();

		// Always return 200 OK for a collection query, even if the list is empty,
		// An empty list is a valid response for "no results found".
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
