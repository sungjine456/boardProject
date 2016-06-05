package kr.co.person.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kr.co.person.domain.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	@Query(value="select id from user where id = ?1", nativeQuery=true)
	String userIdCheck(String id);

	@Query(value="select email from user where email = ?1", nativeQuery=true)
	String userEmialCheck(String emial);
	
	User findByIdAndPassword(String id, String password);
	User findByEmail(String email);
}
