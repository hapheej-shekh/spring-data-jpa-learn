package jpa.learn.api.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/audit")
public class UserAuditDemoApi {
	
	/* This is done in User class using @CreatedDate & @CreatedBy annotation
	 * It can also be done from SecurityConfig by taking user info from ApplicationContext */
}
