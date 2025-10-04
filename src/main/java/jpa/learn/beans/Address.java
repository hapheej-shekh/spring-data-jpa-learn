package jpa.learn.beans;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Table(name="address") // Good practice to explicitly name tables
public class Address extends BaseEntity {

	private String city;
	private String country = "India";
	private Integer zipcode;
}
