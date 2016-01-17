package xyz.hollysys.spring.mybatis_super.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xyz.hollysys.spring.mybatis_super.dao.CouponDao;
import xyz.hollysys.spring.mybatis_super.dao.UserDao;
import xyz.hollysys.spring.mybatis_super.model.Coupon;
import xyz.hollysys.spring.mybatis_super.model.User;

@Service("userService")
public class UserService {
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private CouponDao couponDao;
	
	@Transactional
	public void save(User user,Coupon coupon){
		userDao.save(user);
		couponDao.save(coupon);
	}
}
