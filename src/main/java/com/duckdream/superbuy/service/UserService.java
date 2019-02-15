package com.duckdream.superbuy.service;

import com.duckdream.superbuy.dao.UserDao;
import com.duckdream.superbuy.entity.User;
import com.duckdream.superbuy.result.CodeMsg;
import com.duckdream.superbuy.util.MD5Util;
import com.duckdream.superbuy.vo.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	UserDao userDao;

	public User getById(Long id) {
		return userDao.getById(id);
	}

	public CodeMsg login(LoginVO loginVO) {
		if(loginVO == null) {
			return CodeMsg.SERVER_ERROR;
		}
		String mobile = loginVO.getMobile();
		String formPass = loginVO.getPassword();
		//判断手机号是否存在
		User user = getById(Long.parseLong(mobile));
		if(user == null) {
			return CodeMsg.MOBILE_NOT_EXIST;
		}
		//验证密码
		String dbPass = user.getPassword();
		String saltDB = user.getSalt();
		String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
		if(!calcPass.equals(dbPass)) {
			return CodeMsg.PASSWORD_ERROR;
		}
		return CodeMsg.SUCCESS;
	}

	
}
