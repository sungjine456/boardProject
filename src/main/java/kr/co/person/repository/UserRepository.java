package kr.co.person.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kr.co.person.domain.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	@Query(value="select id from user where id = ?1", nativeQuery=true)
	String userIdCheck(String id);

	@Query(value="select email from user where email = ?1", nativeQuery=true)
	String userEmialCheck(String emial);
	
	@Query(value="select idx, id, email, name, password, reg_date, up_date from user where id = ?1 and password = ?2", nativeQuery=true)
	User loginCheck(String id, String password);
	
	@Query(value="select idx, id, email, name, password, reg_date, up_date from user where email = ?1", nativeQuery=true)
	User passwordCheck(String email);
}
