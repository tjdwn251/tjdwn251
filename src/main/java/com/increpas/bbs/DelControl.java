package com.increpas.bbs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import mybatis.dao.BbsDAO;

@Controller
public class DelControl {

	@Autowired
	private BbsDAO b_dao;
	
	@RequestMapping("/delete.inc")
	public ModelAndView delBbs(String b_idx, String cPage) {
		ModelAndView mv = new ModelAndView();
		
		b_dao.delBbs(b_idx);
		
		mv.setViewName("redirect:list.inc?cPage="+cPage);
		return mv;
	}
}
