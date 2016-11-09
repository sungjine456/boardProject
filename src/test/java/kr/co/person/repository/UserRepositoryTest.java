package kr.co.person.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import kr.co.person.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
@Sql(scripts="classpath:/testDataSql/testData.sql")
public class UserRepositoryTest {

	@Autowired private UserRepository userRepository;
    // 비밀번호 123123을 암호화한 형태
    private String password = "96cae35ce8a9b0244178bf28e4966c2ce1b8385723a96a6b838858cdd6ca0a1epersonProject";
 
    @Test
    public void testFindById() {
    	String id = "sungjin";
    	String findId = userRepository.findById(id).getId();
    	Assert.assertThat(findId, is(id));
    }
    
	@Test
	public void testFindOne() {
		User user = userRepository.findOne(1);
		Assert.assertThat(user.getIdx(), is(1));
		Assert.assertThat(user.getId(), is("sungjin"));
		Assert.assertThat(user.getEmail(), is("sungjin@naver.com"));
		Assert.assertThat(user.getName(), is("hong"));
		Assert.assertThat(user.getAccess(), is("Y"));
		Assert.assertThat(user.getAdmin(), is("N"));
		Assert.assertThat(user.getPassword(), is(password));
	}
    
    @Test
    public void testFindByIdAndPassword() {
    	User user = userRepository.findByIdAndPassword("sungjin", "123123");
    	Assert.assertThat(user, is(nullValue()));
    	user = userRepository.findByIdAndPassword("sungjin", password);
    	Assert.assertThat(user.getName(), is("hong"));
    	Assert.assertThat(user.getEmail(), is("sungjin@naver.com"));
    	Assert.assertThat(user.getId(), is("sungjin"));
    	Assert.assertThat(user.getAccess(), is("Y"));
		Assert.assertThat(user.getAdmin(), is("N"));
    	Assert.assertThat(user.getPassword(), is(password));
    }
    
    @Test
    public void testFindByEmail(){
    	User user = userRepository.findByEmail("sungjin@naver.com");
    	Assert.assertThat(user.getName(), is("hong"));
    	Assert.assertThat(user.getEmail(), is("sungjin@naver.com"));
    	Assert.assertThat(user.getId(), is("sungjin"));
    	Assert.assertThat(user.getPassword(), is(password));
    	
    	user = userRepository.findByEmail("ass@naver.com");
    	Assert.assertThat(user, is(nullValue()));
    }
    
    @Test
    public void testSave(){
    	DateTime date = new DateTime();
    	Assert.assertThat(userRepository.findById("test"), is(nullValue()));
    	User user = new User("test", "test@naver.com", "123", "test", "img", date ,date);
    	userRepository.save(user);
    	user = userRepository.findById("test");
    	Assert.assertThat("test", is(user.getId()));
    	Assert.assertThat("test@naver.com", is(user.getEmail()));
    	Assert.assertThat("123", is(user.getPassword()));
    	Assert.assertThat("test", is(user.getName()));
    }
}
