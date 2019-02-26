package com.duckdream.superbuy.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.duckdream.superbuy.entity.User;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserUtil {

    private static void createUser(int count) throws Exception{
		List<User> users = new ArrayList<User>(count);
		//生成用户
		for(int i = 0; i < count; i++) {
			User user = new User();
			user.setId(13000000000L + i);
			user.setNickname("user" + i);
			user.setSalt("1a2b3c");
			user.setPassword(MD5Util.inputPassToDBPass("123456", user.getSalt()));
			user.setRegisterDate(new Date());
			user.setLoginCount(1);
			users.add(user);
		}
		System.out.println("create user");
		//插入数据库
//		Connection conn = DBUtil.getConn();
//		String sql = "insert into tb_user(id, nickname, password, salt, register_data, login_count)values(?,?,?,?,?,?)";
//		PreparedStatement pstmt = conn.prepareStatement(sql);
//		for(int i = 0; i< users.size(); i++) {
//			User user = users.get(i);
//			pstmt.setLong(1, user.getId());
//			pstmt.setString(2, user.getNickname());
//			pstmt.setString(3, user.getPassword());
//			pstmt.setString(4, user.getSalt());
//			pstmt.setTimestamp(5, new Timestamp(user.getRegisterDate().getTime()));
//			pstmt.setInt(6, user.getLoginCount());
//			pstmt.addBatch();
//		}
//		pstmt.executeBatch();
//		pstmt.close();
//		conn.close();
//		System.out.println("insert to db");
		//登录，生成token
		String urlString = "http://localhost:8080/login/do_login";
		File file = new File("/home/hehanyue/tokens.txt");
		if(file.exists()) {
			file.delete();
		}
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		file.createNewFile();
		raf.seek(0);
		for(int i = 0; i < users.size(); i++) {
			User user = users.get(i);
			URL url = new URL(urlString);
			HttpURLConnection co = (HttpURLConnection)url.openConnection();
			co.setRequestMethod("POST");
			co.setDoOutput(true);
			OutputStream out = co.getOutputStream();
			String params = "mobile=" + user.getId() + "&password=" + MD5Util.inputPassToFormPass("123456");
			out.write(params.getBytes());
			out.flush();
			InputStream inputStream = co.getInputStream();
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			byte buff[] = new byte[1024];
			int len = 0;
			while((len = inputStream.read(buff)) >= 0) {
				bout.write(buff, 0 ,len);
			}
			inputStream.close();
			bout.close();
			String response = new String(bout.toByteArray());
			JSONObject jo = JSON.parseObject(response);
			String token = jo.getString("data");
			System.out.println("create token : " + user.getId());

			String row = user.getId() + "," + token;
			raf.seek(raf.length());
			raf.write(row.getBytes());
			raf.write("\n".getBytes());
			System.out.println("write to file : " + user.getId());
		}
		raf.close();
		
		System.out.println("over");
	}
	
//	public static void main(String[] args)throws Exception {
//		createUser(1000);
//	}
}