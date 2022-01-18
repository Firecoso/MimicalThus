package com.km.mimicalthus.controllers;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.km.mimicalthus.entities.User;
import com.km.mimicalthus.model.DatapointType;
import com.km.mimicalthus.model.RegistrationResponseWrapper;
import com.km.mimicalthus.model.ResponseWrapper;
import com.km.mimicalthus.model.UserDTO;
import com.km.mimicalthus.model.UserRegistrationForm;
import com.km.mimicalthus.services.UserService;

@RestController
@RequestMapping("users")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/registration")
	public ResponseEntity<RegistrationResponseWrapper> registration(@RequestBody UserRegistrationForm form) {
		try {
			User user = userService.registration(form);
			return ResponseEntity.ok(new RegistrationResponseWrapper(user, null));

		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new RegistrationResponseWrapper(null, e.getMessage()));

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new RegistrationResponseWrapper(null, "Unexpected error :" + e.getMessage()));
		}

	}

	@GetMapping("/behaviors")
	public ResponseEntity<ResponseWrapper> getSimilarBehaviours(@RequestParam String email,
			@RequestParam Integer offset, @RequestParam Integer limit) {
		return getSimilar(email, offset, limit, DatapointType.BEHAVIOR);
	}

	@GetMapping("/passions")
	public ResponseEntity<ResponseWrapper> getSimilarPassions(@RequestParam String email, @RequestParam Integer offset,
			@RequestParam Integer limit) {
		return getSimilar(email, offset, limit, DatapointType.PASSION);
	}

	private ResponseEntity<ResponseWrapper> getSimilar(String email, Integer offset, Integer limit,
			DatapointType dType) {
		try {
			List<UserDTO> result = userService.findSimilarByEmail(email, dType);
			// pagination
			result = result.subList(Math.min(result.size(), offset), Math.min(result.size(), offset + limit));
			return ResponseEntity.ok(new ResponseWrapper(result, null));

		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseWrapper(null, "User not found"));

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseWrapper(null, "Unexpected error :" + e.getMessage()));
		}
	}

	@GetMapping("/all")
	public List<User> getAllUsers() {
		return userService.findAll();
	}
	
	@GetMapping("/profile")
	public UserDTO getProfileByEmail(@RequestParam String email) {
		return userService.findProfileByEmail(email);
	}

}
