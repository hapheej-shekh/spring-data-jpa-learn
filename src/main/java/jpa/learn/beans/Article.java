package jpa.learn.beans;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@NamedQuery(name="findByTitle", // The name of the query
	query="SELECT a FROM Article a WHERE a.title = :title")
public class Article {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="articleId", nullable=false)
	private Integer id;
	private String author;
	private String title;
	private String desc;
	
	@OneToMany(mappedBy="article", cascade=CascadeType.ALL, orphanRemoval=true, fetch=FetchType.LAZY)
	@JsonManagedReference
    private List<Comment> comments = new ArrayList<>(); // Initialize to prevent NullPointerException

    // Helper methods to maintain bidirectional relationship consistency
    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setArticle(this); // Set the article reference in the comment
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setArticle(null); // Remove the article reference in the comment
    }
	
	public String toString() {
		
		return "{Id: "+this.id+", Auther: "+this.author+", Title: "
				+this.title+", Desc: "+this.desc+"}";
	}
}
