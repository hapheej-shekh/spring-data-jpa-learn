package jpa.learn.beans.extra;

import org.springframework.beans.factory.annotation.Value;

/* Interface based Projection to specific fields
 * Open Projections- Combining multiple fields using @Value of Entity
 */
public interface UserInfo2 {

	@Value("#{target.firstName + ' ' + target.lastName}")	//Matches Entity fields
	String getFullname();
	Address getAddress();
	
	// Dependent Entity Projection
	interface Address {
		
		String getCity();
		Integer getZipcode();
	}
}
