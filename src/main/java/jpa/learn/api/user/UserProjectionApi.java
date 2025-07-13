package jpa.learn.api.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jpa.learn.beans.extra.DynamicUserInfo1;
import jpa.learn.beans.extra.DynamicUserInfo2;
import jpa.learn.beans.extra.UserDto;
import jpa.learn.beans.extra.UserInfo1;
import jpa.learn.beans.extra.UserInfo2;
import jpa.learn.services.user.UserProjectionService;

@RestController
@RequestMapping("/api/users/projections")
public class UserProjectionApi {
	
	@Autowired
	private UserProjectionService projectionService;


	@GetMapping("closed")
	public ResponseEntity<List<UserInfo1>> getProjectedUsersClosed(){
		
		List<UserInfo1> res = projectionService.fetchUserInfoByClosed();
		
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	@GetMapping("open")
	public ResponseEntity<List<UserInfo2>> getProjectedUsersOpen(){
		
		List<UserInfo2> res = projectionService.fetchUserInfoByOpen();
		
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	@GetMapping("class-based")
	public ResponseEntity<List<UserDto>> getProjectedUsersClassBased(){
		
		List<UserDto> res = projectionService.fetchUserInfoByClassBased();
		
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	@GetMapping("dynamic1")
	public ResponseEntity<List<DynamicUserInfo1>> getProjectedUsersDynamic1() {
		
		List<DynamicUserInfo1> res = projectionService.findAllByUserInfo1(DynamicUserInfo1.class);
		
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@GetMapping("dynamic2")
	public ResponseEntity<List<DynamicUserInfo2>> getProjectedUsersDynamic2() {
		
		List<DynamicUserInfo2> res = projectionService.findAllByUserInfo2(DynamicUserInfo2.class);
		
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@GetMapping(params= {"name", "city"})
	public ResponseEntity<List<DynamicUserInfo1>> getProjectedUsersDynamic(String name, String city) {
		
		List<DynamicUserInfo1> res = projectionService.getUserProjection(name, city);
		
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
}
