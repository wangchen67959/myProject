package com.test.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.dao.TestDao;
import com.test.entity.User;
import com.test.service.TestService;

	@Service
	public class TestServiceImpl implements TestService {
//	    @Autowired
	    private TestDao stuDto;

		public TestDao getStuDto() {
			return stuDto;
		}
		 @Autowired
		public void setStuDto(TestDao stuDto) {
			this.stuDto = stuDto;
		}

		public String testQuery() throws Exception {
	        List<User> users = stuDto.testQuery();
	        String res = "";
	        if (users != null && users.size() > 0) {
	            for (User user : users) {
	                res += user.toString() + "|";
	            }
	        } else {
	            res = "Not found.";
	        }
	        return res;
	    }
		
		public List<User> testQueryList() throws Exception {
	        List<User> users = stuDto.testQuery();
	        return users;
	    }
		public void insertV(Map<String, Object> m) throws Exception {
			stuDto.inserV(m);
		}
}
