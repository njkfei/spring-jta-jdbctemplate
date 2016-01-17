package xyz.hollysys.spring.mybatis_super.dao;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import xyz.hollysys.spring.mybatis_super.model.Coupon;
import xyz.hollysys.spring.mybatis_super.model.User;

@Repository
public class CouponDao {

	@Resource(name="jdbcTemplateB")
	private JdbcTemplate jdbcTemplate;
	
	public void save(Coupon coupon){
		jdbcTemplate.update("insert into coupon(username,type,value) values(?,?,?)",coupon.getUsername(),coupon.getType(),coupon.getValue());
	}
}
