package com.test.dao;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ibatis.sqlmap.client.SqlMapClient;

public class BaseDao {
	@Autowired 
  private SqlMapClient sqlMapClient; 
	
   public SqlMapClient getSqlMapClient() {
		return sqlMapClient;
	}

	public void setSqlMapClient(SqlMapClient sqlMapClient) {
		this.sqlMapClient = sqlMapClient;
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> getList() throws SQLException {
		return  sqlMapClient.queryForList("TestDao.testQuery");
	}

}
