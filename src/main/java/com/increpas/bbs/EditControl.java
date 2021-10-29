package com.increpas.bbs;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import mybatis.dao.BbsDAO;
import mybatis.vo.BbsVO;
import spring.util.FileRenameUtil;


@Controller
public class EditControl {

	
	@Autowired
	private BbsDAO b_dao;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private ServletContext application;
	
	
	private String editor_img = "/resources/editor_img";
	private String bbs_upload = "/resources/bbs_upload";
	
	
	//해당 경우에는 수정할 글을 b_idx를 인자로해서 글을 선택해 불러오고
	// 그 글에 파일이 있을경우와 없을경우를 나눠서 나타낸다.
	/*
	@RequestMapping("/edit.inc")
	public String edit(String b_idx, Model m) {
		
		BbsVO vo = b_dao.getBbs(b_idx);
		
		m.addAttribute("vo", vo); //Model은 request에 저장됨!
								//forward가 되므로 지역변수여도 사용가능
		
		return "edit";
	}
	*/
	@RequestMapping(value="/edit.inc", method = RequestMethod.POST)
	public ModelAndView  edit(BbsVO vo, String cPage)throws Exception{
		ModelAndView mv = new ModelAndView();
		
		// 요청시 파일이 첨부된 요청인지? 파일첨부가 없는 요청인지?
		//    파일이 첨부됬을 경우 multipart.....로 시작
		//    파일이 첨부가 안됐을 땐 application......으로 시작
		String ctx = request.getContentType();
		
		if(ctx.startsWith("multipart")) {
			
			MultipartFile mf = vo.getFile();
			
			if(mf != null && mf.getSize() > 0) {
				String realpath = application.getRealPath(bbs_upload);
				
				String fname = mf.getOriginalFilename();
				
				fname = FileRenameUtil.checkSameFileName(fname, realpath);
				
				mf.transferTo(new File(realpath, fname)); //첨부 파일 업로드
				
				vo.setFile_name(fname);
				vo.setOri_name(fname);
			}
			
			vo.setIp(request.getRemoteAddr());
			
			b_dao.editBbs(vo); //DB저장
			mv.setViewName("redirect:/view.inc?b_idx="+vo.getB_idx()+"&cPage="+cPage);
		}else if(ctx.startsWith("application")) {
			BbsVO bvo = b_dao.getBbs(vo.getB_idx());
			mv.addObject("vo", bvo);
			
			mv.setViewName("edit");
		}
		
		return mv;
	}
}
