package com.increpas.bbs;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import mybatis.dao.BbsDAO;
import mybatis.vo.BbsVO;

@Controller
public class ViewControl {
	
	@Autowired
	private BbsDAO b_dao;
	
	@Autowired
	private HttpServletRequest request;
	
	@RequestMapping("/view.inc")
	public ModelAndView getBbs(String b_idx, String cPage) {
		ModelAndView mv = new ModelAndView();
		
		BbsVO vo = b_dao.getBbs(b_idx);
		
		mv.addObject("vo", vo);
		//mv.addObject("cPage", cPage);
		// cPage도 사실 가야 하는데... 저장할 필요는 없다.
		// 이유는 view.jsp로 forword되므로 여기까지 전달된 
		// 파라미터들이 모두 같이 가게된다.
		
		mv.addObject("ip", request.getRemoteAddr());
		mv.setViewName("view");
		
		return mv;
	}

}
