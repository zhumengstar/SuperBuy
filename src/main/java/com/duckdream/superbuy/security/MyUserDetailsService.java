package com.duckdream.superbuy.security;

import com.duckdream.superbuy.service.UserService;
import com.duckdream.superbuy.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.duckdream.superbuy.entity.User user = userService.getById(Long.valueOf(username).longValue());


        Long mobile = userService.getById(Long.valueOf(username).longValue()).getId();


        logger.info("mobile：" + mobile);
        logger.info("username：" + user.getId());
        logger.info("password:" + user.getPassword());

//        String dbPass = user.getPassword();
//        String saltDB = user.getSalt();
//        String calcPass = MD5Util.formPassToDBPass(dbPass, saltDB);


        String password = passwordEncoder.encode(user.getPassword());
        logger.info("数据库密码是：" + password);
        return new User(String.valueOf(mobile), password,
                true, true, true, true,
                AuthorityUtils.commaSeparatedStringToAuthorityList(username));
//        return null;

    }
}
