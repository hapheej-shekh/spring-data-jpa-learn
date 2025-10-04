package jpa.learn.beans;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name="users") // Good practice to explicitly name tables
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@NamedQuery(name="findUserByCity", //JPQL Named Query
	query="SELECT u FROM User u JOIN u.address a WHERE a.city = :city")
/* @NamedQuery(name="findUserByCity", 
 * query="SELECT u FROM User u WHERE u.address.city = :cityParam") */
public class User extends BaseEntity {

	private String firstName;
	private String lastName;
	
	@CreatedDate	// Auditing Support
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy") // Default yyyy-MM-dd
	private Date joinDate;
	@CreatedBy	// Auditing Support
	private Integer createdBy;

	/**
	 * Establish a One-to-One relationship with Address
	 * CascadeType.ALL: Operations (persist, merge, remove, refresh, detach) on User will cascade to Address
	 * orphanRemoval=true: If an Address is disassociated from a User, it will be deleted
	 * @JoinColumn: Specifies foreign key column in 'users' table that refers to the 'address' table
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name="address_id", referencedColumnName="id")
	private Address address;

	
	// Helper constructor
	public User(Address address) {
		this.address = address;
	}
	
	// Helper method
	public void updateJoiningDate() {
		
		this.joinDate = new Date();
	}
	
	
	public String toString() {
		
		return "{"+this.firstName+", "+this.lastName+", "+this.getAddress().getCity()+"}";
	}
	
	
	
	/*	--- Entity Life cycle Methods--- */
	
	@PrePersist
    public void prePersist() {
        System.out.println("Before inserting: " + this.toString());
    }

    @PostPersist
    public void postPersist() {
        System.out.println("Inserted: " + this.toString());
    }

    @PreUpdate
    public void preUpdate() {
        System.out.println("Before updating: " + this.toString());
    }
    
    @PostUpdate
    public void postUpdate() {
        System.out.println("Before updating: " + this.toString());
    }
    
    @PreRemove
    public void preRemove() {
        System.out.println("Before updating: " + this.toString());
    }
    
    @PostRemove
    public void postRemove() {
        System.out.println("Before updating: " + this.toString());
    }

    @PostLoad
    public void postLoad() {
        System.out.println("Loaded from DB: " + this.toString());
    }
}
