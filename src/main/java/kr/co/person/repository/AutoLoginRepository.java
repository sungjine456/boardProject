package kr.co.person.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.person.domain.AutoLogin;

public interface AutoLoginRepository extends JpaRepository<AutoLogin, Integer> {
	AutoLogin findByUserIdxAndLoginId(int userIdx, String loginId);
}
