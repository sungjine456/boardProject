package kr.co.person.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.person.domain.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	User findById(String id);
	User findByIdAndPassword(String id, String password);
	User findByEmail(String email);
}
