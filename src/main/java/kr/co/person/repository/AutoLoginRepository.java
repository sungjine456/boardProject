package kr.co.person.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.person.domain.AutoLogin;

public interface AutoLoginRepository extends JpaRepository<AutoLogin, Integer> {
	AutoLogin findByUserIdxAndRegIpAndRegDate(int userIdx, String regId, Date regDate);
}
