package kr.co.person.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.person.domain.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
