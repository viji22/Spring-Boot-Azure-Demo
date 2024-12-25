package com.example.studentRegistration;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentController {
	
	@Autowired
	StudentService service;
	
	@Autowired  
	PasswordEncoder passwordEncoder;
	

    @PostMapping("/register")
    public ResponseEntity<Long> registerOrUpdate(@RequestBody Student s) {
        Optional<Student> existingStudent = service.findByUsername(s.getUserName());
        Student savedStudent;
        if (s.getId() != null) {
            // Update existing student
        	savedStudent= service.updateExistingUser(existingStudent,s);
        	return ResponseEntity.status(HttpStatus.CREATED).body(savedStudent.getId());
            } 
         else {
            // Register new student
            if (existingStudent.isPresent()) {
                throw new RuntimeException("Username already exists.");
            }
            s.setPassword(passwordEncoder.encode(s.getPassword()));
           savedStudent=service.save(s);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedStudent.getId());
        }
    }

    @PostMapping("/login")
	public String login(@RequestBody Student s) {
		Optional<Student> existingUser = service.findByUsername(s.getUserName());
		if (existingUser.isPresent()) {
			if (passwordEncoder.matches(s.getPassword(), existingUser.get().getPassword())) {
				return "Login Successful";
			} else {
				return "UserName or Password is incorrect";
			}
		} else {
			return "User doesn't exist";
		}
	}
    
    @GetMapping("/getUser/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
    	return service.getUserById(id).<ResponseEntity<?>>map(student -> new ResponseEntity<>(student, HttpStatus.OK)) 
    			.orElseGet(() -> new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND)); 
    	}

    
    

}
