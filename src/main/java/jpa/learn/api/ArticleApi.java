package jpa.learn.api;

import java.util.List;
import java.util.Optional;

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

import jpa.learn.beans.Article;
import jpa.learn.services.ArticleService;

@RestController
@RequestMapping("/api/articles")
public class ArticleApi {

	@Autowired
	private ArticleService articleService;
	

	/**
	 * Handles POST requests to create a new Article.
	 * URI: POST /api/articles
	 *
	 * @param article The Article object from the request body.
	 * @return ResponseEntity with the created Article and HttpStatus.CREATED (201) on success,
	 * or HttpStatus.BAD_REQUEST (400) if creation fails (e.g., ID is unexpectedly null).
	 */
	@PostMapping
	public ResponseEntity<Article> addArticle(@RequestBody Article article) {
		
		Article createdArticle = articleService.createArticle(article);

		if (createdArticle != null && createdArticle.getId() != null)
			return new ResponseEntity<>(createdArticle, HttpStatus.CREATED);
		
		return new ResponseEntity<>(article, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handles PUT requests to update an existing Article.
	 * URI: PUT /api/articles
	 *
	 * @param article The updated Article object from the request body.
	 * @return ResponseEntity with the updated Article and HttpStatus.OK (200) on success,
	 * HttpStatus.NOT_FOUND (404) if the article does not exist,
	 * or HttpStatus.BAD_REQUEST (400) for invalid input.
	 */
	@PutMapping
	public ResponseEntity<Article> updateArticle(@RequestBody Article article) {

		if (article.getId() == null)
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

		Article updatedArticle = articleService
				.updateArticle(article.getId(), article.getTitle(), article.getDesc());

		if (updatedArticle != null)
			return new ResponseEntity<>(updatedArticle, HttpStatus.OK);

		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}

	/**
	 * Handles DELETE requests to delete an Article by ID.
	 * URI: DELETE /api/articles/{id}
	 *
	 * @param id The ID of the article to delete.
	 * @return ResponseEntity with HttpStatus.NO_CONTENT (204) on successful deletion,
	 * or HttpStatus.NOT_FOUND (404) if the article does not exist.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteArticle(@PathVariable("id") int id) {
		
		boolean deleted = articleService.deleteArticle(id);

		if (deleted)
			return new ResponseEntity<>("Article with id: " + id + " deleted", HttpStatus.NO_CONTENT);

		return new ResponseEntity<>("Article with id: " + id + " not found", HttpStatus.NOT_FOUND);
	}

	/**
	 * Handles GET requests to retrieve a single Article by ID.
	 * URI: GET /api/articles/{id}
	 *
	 * @param id The ID of the article to retrieve.
	 * @return ResponseEntity with the Article and HttpStatus.OK (200) if found,
	 * or HttpStatus.NOT_FOUND (404) if not found.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Article> getArticle(@PathVariable("id") int id) {
		
		Optional<Article> response = articleService.findById(id);

		if (response.isPresent())
			return new ResponseEntity<>(response.get(), HttpStatus.OK);

		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}

	/**
	 * Handles GET requests to retrieve multiple Articles by a list of IDs.
	 * URI: GET /api/articles?ids=1,2,3
	 *
	 * @param ids A comma-separated list of article IDs from the request parameter.
	 * @return ResponseEntity with a list of Articles and HttpStatus.OK (200) if found,
	 * or HttpStatus.NOT_FOUND (404) if no articles are found for the given IDs.
	 */
	@GetMapping(params="ids") // Differentiates this mapping: requires 'ids' parameter
	public ResponseEntity<List<Article>> getArticleByIds(@RequestParam("ids") List<Integer> ids) {
		
		List<Article> response = articleService.findByIds(ids);

		if (response != null && !response.isEmpty())
			return new ResponseEntity<>(response, HttpStatus.OK);

		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}

	/**
	 * Handles GET requests to retrieve Articles by author, ordered by description descending.
	 * URI: GET /api/articles?author={name}
	 *
	 * @param author The author's name from the request parameter.
	 * @return ResponseEntity with a list of Articles and HttpStatus.OK (200) if found,
	 * or HttpStatus.NOT_FOUND (404) if no articles are found for the author.
	 */
	@GetMapping(params="author") // Differentiates this mapping: requires 'author' parameter
	public ResponseEntity<List<Article>> getArticleByAutherDescOrder(@RequestParam("author") String author) {
		
		List<Article> response = articleService.getArticlesByAuthorOrderedByDesc(author);

		if (response != null && !response.isEmpty())
			return new ResponseEntity<>(response, HttpStatus.OK);

		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}
	
	/**
	 * Handles GET requests to retrieve all Articles.
	 * URI: GET /api/articles (when no other query parameters are present)
	 *
	 * @return ResponseEntity with a list of all Articles and HttpStatus.OK (200) if found,
	 * or HttpStatus.NOT_FOUND (404) if no articles exist.
	 */
	@GetMapping
	public ResponseEntity<List<Article>> getAllArticles() {
		
		List<Article> response = articleService.findAll();

		if (response != null && !response.isEmpty())
			return new ResponseEntity<>(response, HttpStatus.OK);

		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}
}
