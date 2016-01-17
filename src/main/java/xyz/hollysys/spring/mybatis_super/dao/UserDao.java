package xyz.hollysys.spring.mybatis_super.dao;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import xyz.hollysys.spring.mybatis_super.model.User;

@Repository
public class UserDao {

	@Resource(name="jdbcTemplateA")
	private JdbcTemplate jdbcTemplate;
	
	public void save(User user){
		jdbcTemplate.update("insert into user2(username,password) values(?,?)",user.getUsername(),user.getPassword());
	}
}
