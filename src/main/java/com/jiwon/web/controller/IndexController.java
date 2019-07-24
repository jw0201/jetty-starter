package com.jiwon.web.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiwon.app.service.ApplicationService;

@Controller
public class IndexController {

	private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

	@Autowired
	ApplicationService appSvc;

	@Value("${example.message}")
	private String message;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	public String showIndex() {
		return message;
	}

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> showUsers(@RequestParam Map<String, Object> map) {
		return appSvc.select(map);
	}
}