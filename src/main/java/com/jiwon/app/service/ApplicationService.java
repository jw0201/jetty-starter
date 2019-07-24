package com.jiwon.app.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiwon.app.repository.ApplicationRepository;

@Service
public class ApplicationService {
	@Autowired
	private SqlSession sqlSession;
	
	private static final Logger logger = LoggerFactory.getLogger(ApplicationService.class);
	
	public List<Map<String, Object>> select(Map<String, Object> map) {
		ApplicationRepository reposit = sqlSession.getMapper(ApplicationRepository.class);
		return reposit.select(map);
	}
}