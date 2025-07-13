package jpa.learn.beans;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name="users") // Good practice to explicitly name tables
@NoArgsConstructor
@NamedQuery(name="findUserByCity", //JPQL Named Query
	query="SELECT u FROM User u JOIN u.address a WHERE a.city = :city")
/* @NamedQuery(name="findUserByCity", 
 * query="SELECT u FROM User u WHERE u.address.city = :cityParam") */
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)	// Use IDENTITY for auto-incrementing IDs in most databases
	private Integer id;
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
	
	public void updateJoiningDate() {
		
		this.joinDate = new Date();
	}
}
