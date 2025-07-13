package jpa.learn.beans.extra;

public interface DynamicUserInfo1 {

	String getFirstName();
	String getLastName();
	Address getAddress();
	
	// Dependent Entity Projection
	interface Address {
		
		String getCity();
		Integer getZipcode();
	}
}
