package com.test.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.test.entity.User;

@Service
public interface TestService {
	public String testQuery() throws Exception;
	public void insertV(Map<String, Object> m)throws Exception;
	public List<User> testQueryList() throws Exception;
}
