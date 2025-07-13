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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Comment {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="commentId", nullable=false)
	private Integer id;
	private String desc;
	private String author;

	/**
     * @ManyToOne: Indicates that this is the owning side of the relationship
     *             (holds the foreign key).
     *
     * @JoinColumn(name = "article_id"): Specifies the foreign key column name in the 'comments' table
     * that references the 'articles' table.
     *
     * nullable = false: Ensures that a comment must always be associated with an article.
     *
     * fetch = FetchType.LAZY: The associated Article will be loaded only when it's explicitly accessed.
     * This is the default for @ManyToOne and is generally recommended.
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="article_id", nullable=false)
    @JsonBackReference // This side will NOT be serialized
    private Article article;

    /**
     * Self-referencing Many-to-One relationship for parent comments.
     * A Comment can have one parent Comment (if it's a reply).
     *
     * @JoinColumn(name = "parent_comment_id"): Specifies the foreign key column name in the
     * 'comments' table that references another 'comment' (its parent).
     *
     * nullable = true: A comment might not have a parent (it could be a top-level comment on an article).
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="parent_comment_id") // Can be null if it's a top-level comment
    @JsonBackReference("comment-replies") // Use a unique value for self-referencing
    private Comment parentComment;

    /**
     * Self-referencing One-to-Many relationship for replies.
     * A Comment can have multiple child Comments (replies).
     *
     * mappedBy = "parentComment": Indicates that the 'parentComment' field in the child Comment entity
     * is the owning side of this relationship.
     *
     * cascade = CascadeType.ALL: If a parent comment is removed, its replies will also be removed.
     *
     * orphanRemoval = true: If a reply is removed from the 'replies' list of a parent comment,
     * it will be automatically deleted from the database.
     */
    @OneToMany(mappedBy="parentComment", cascade=CascadeType.ALL, orphanRemoval=true, fetch=FetchType.LAZY)
    @JsonManagedReference("comment-replies") // Use a unique value for self-referencing
    private List<Comment> replies = new ArrayList<>(); // Initialize to prevent NullPointerException

    // Helper methods to maintain bidirectional relationship consistency for replies
    public void addReply(Comment reply) {
        replies.add(reply);
        reply.setParentComment(this); // Set the parent comment reference in the reply
    }

    public void removeReply(Comment reply) {
        replies.remove(reply);
        reply.setParentComment(null); // Remove the parent comment reference in the reply
    }
	
	public String toString() {
		
		return "{Id: "+this.id+", Desc: "+this.desc+", Auther: "+this.author+"}";
	}
}
