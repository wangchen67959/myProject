package com.test.dao;

import java.util.List;
import java.util.Map;

import com.test.entity.User;
public interface TestDao {
	public List<User> testQuery() throws Exception;
	public void inserV(Map<String, Object> m);
}
