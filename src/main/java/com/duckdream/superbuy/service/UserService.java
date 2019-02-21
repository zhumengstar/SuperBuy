package com.duckdream.superbuy.service;

import com.duckdream.superbuy.dao.UserDao;
import com.duckdream.superbuy.entity.User;
import com.duckdream.superbuy.exception.GlobalException;
import com.duckdream.superbuy.redis.RedisService;
import com.duckdream.superbuy.redis.UserKey;
import com.duckdream.superbuy.result.CodeMsg;
import com.duckdream.superbuy.util.MD5Util;
import com.duckdream.superbuy.util.UUIDUtil;
import com.duckdream.superbuy.vo.LoginVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserService {

	public static final String COOKIE_NAME_TOKEN = "token";

	@Autowired
	UserDao userDao;

	@Autowired
	RedisService redisService;

	public User getById(Long id) {
		return userDao.getById(id);
	}

	public String login(HttpServletResponse response, LoginVO loginVO) {
		if(loginVO == null) {
			throw new GlobalException(CodeMsg.SERVER_ERROR);
			//出现异常就抛
		}
		String mobile = loginVO.getMobile();
		String formPass = loginVO.getPassword();
		//判断手机号是否存在
		User user = getById(Long.parseLong(mobile));
		if(user == null) {
			throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
		}
		//验证密码
		String dbPass = user.getPassword();
		String saltDB = user.getSalt();
		String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
		if(!calcPass.equals(dbPass)) {
			throw new GlobalException(CodeMsg.PASSWORD_ERROR);
		}
		//生成token,将token作为cookie
		String token = UUIDUtil.uuid();
		addCookie(response, token, user);
		return token;
	}

	public User getByToken(HttpServletResponse response, String token) {
		if(StringUtils.isEmpty(token)) {
			return null;
		}
		User user = redisService.get(UserKey.token, token, User.class);
		//延长有效期
		if(user != null) {
			addCookie(response, token, user);
		}
		return user;
	}

	public void addCookie(HttpServletResponse response, String token, User user) {
		redisService.set(UserKey.token, token, user); //存储在redis中
		Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token); //生成cookie
		cookie.setMaxAge(UserKey.token.expireSeconds()); //设置生存期
		cookie.setPath("/");
		response.addCookie(cookie);
	}


}
