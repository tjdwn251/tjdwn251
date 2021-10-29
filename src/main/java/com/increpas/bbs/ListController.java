package com.increpas.bbs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import mybatis.dao.BbsDAO;
import mybatis.vo.BbsVO;
import spring.util.Paging;

@Controller
public class ListController {
	
	@Autowired
	private BbsDAO b_dao;
	
	int nowPage; //현재 페이지 값
	int rowTotal; //총 게시물 수
	int blockList = 6; // 한 페이지에 표현될 게시물 수
	int blockPage = 5; // 한 블럭당 표현할 페이지 수
	
	@RequestMapping("/list.inc")
	public ModelAndView list(String cPage, String bname) {
		ModelAndView mv = new ModelAndView();
		
		if(cPage == null)
			nowPage = 1;
		else
			nowPage = Integer.parseInt(cPage);
		if(bname == null)
			bname = "BBS"; //일반게시판
		
		rowTotal = b_dao.getTotalCount(bname); //전체 게시물 수
		
		//페이징 처리를 위한 객체 생성
		Paging page = new Paging(nowPage, rowTotal, blockList, blockPage);
		
		int begin = page.getBegin();
		int end = page.getEnd();
		
		BbsVO[] ar = b_dao.getList(String.valueOf(begin), String.valueOf(end), bname);
		
		//JSP에서 표현해야 하므로 ar을 mv에 저장
		mv.addObject("ar", ar);
		mv.addObject("nowPage", nowPage);
		mv.addObject("rowTotal", rowTotal);
		mv.addObject("blockList", blockList);
		mv.addObject("pageCode", page.getSb().toString());
		
		mv.setViewName("list"); // views/list.jsp
		
		return mv;
	}

}
