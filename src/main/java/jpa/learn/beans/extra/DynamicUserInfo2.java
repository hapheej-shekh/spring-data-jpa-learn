package jpa.learn.beans.extra;

public interface DynamicUserInfo2 {

	String getFirstName();
	Address getAddress();
	
	// Dependent Entity Projection
	interface Address {
		
		String getCity();
		Integer getZipcode();
	}
}
