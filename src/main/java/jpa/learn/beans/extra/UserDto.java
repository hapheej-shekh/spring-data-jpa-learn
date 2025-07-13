package jpa.learn.beans.extra;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {

    private String firstName;
    private String lastName;
    private String city;
    private Integer zipcode;
    
    // Class based projection need Query in Repository as well
    
    public UserDto(String firstName, String lastName, String city, Integer zipcode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.zipcode = zipcode;
    }
}
