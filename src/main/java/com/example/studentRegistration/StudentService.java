package com.example.studentRegistration;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

	@Autowired
	StudentRepository repo;
	@Autowired
	PasswordEncoder passwordEncoder;

	public Student save(Student s) {
		return repo.save(s);

	}

	public Optional<Student> getUserById(Long id) {
		return repo.findById(id);
	}

	public void updateUser(Student s) {
		repo.save(s);
	}

	public Optional<Student> findByUsername(String userName) {
		return repo.findByUserName(userName);
	}

	public Student updateExistingUser(Optional<Student> existingStudentOpt, Student s) {
		Optional<Student> existingStudentById = getUserById(s.getId());
		if (existingStudentById.isPresent()) {
			Student existingStudent = existingStudentById.get();

			// Check for username uniqueness
			if (existingStudentOpt.isPresent() && !existingStudentOpt.get().getId().equals(existingStudent.getId())) {
				throw new RuntimeException("Username already exist.");
			}

			existingStudent.setUserName(s.getUserName()); // Allow username update
			existingStudent.setEmail(s.getEmail());
			existingStudent.setMobileNumber(s.getMobileNumber());
			if (s.getPassword() != null && !s.getPassword().isEmpty()) {
				existingStudent.setPassword(passwordEncoder.encode(s.getPassword()));
			}
			return repo.save(existingStudent);
		} else {
			throw new RuntimeException("User not found.");
		}
	}

}
