package com.increpas.bbs;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import mybatis.dao.BbsDAO;
import mybatis.vo.BbsVO;
import spring.util.FileRenameUtil;
import spring.vo.ImgVO;

@Controller
public class WriteControl {

	//에디터에서 이미지가 들어갈 때 해당 이미지를 받아서
	// 저장할 위치
	private String editor_img = "/resources/editor_img";
	
	private String bbs_upload = "/resources/bbs_upload";
	
	@Autowired
	private ServletContext application;
	
	@Autowired
	private BbsDAO b_dao;
	
	
	@Autowired
	private HttpServletRequest request;
	
	
	@RequestMapping("/write.inc")
	public String write() {
		return "write";
	}
	
	@RequestMapping(value="/saveImg.inc", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> saveImg(ImgVO vo){
		//반환객체 생성
		Map<String, String> map = new HashMap<String, String>();
		
		//넘어온 이미지 파일이 있는지 확인
		MultipartFile f = vo.getS_file();
		String fname = null; //반환 할 때 필요함!
		
		if(f.getSize() > 0) {
			//파일이 들어온 경우
			String realpath = application.getRealPath(editor_img);
			
			fname = f.getOriginalFilename();
			
			fname = FileRenameUtil.checkSameFileName(fname, realpath);
			
			try {
				f.transferTo(new File(realpath, fname));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//파일 업로드가 되었으므로 이제 정확한 경로를 반환해야한다.
		String c_path = request.getContextPath();
		
		map.put("url", c_path+editor_img);
		map.put("fname", fname);
		
		return map;
	}
	@RequestMapping(value="/write.inc", method=RequestMethod.POST)
	public ModelAndView write(BbsVO vo) {
		ModelAndView mv = new ModelAndView();
		
		MultipartFile mf = vo.getFile();
		
		if(mf.getSize() > 0) {
			//파일이 들어있을 때
			String realpath = application.getRealPath(bbs_upload);
			
			String fname = mf.getOriginalFilename();
			
			fname = FileRenameUtil.checkSameFileName(fname, realpath);
			
			try {
				mf.transferTo(new File(realpath, fname));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			vo.setFile_name(fname);
			vo.setOri_name(fname);
		}
		vo.setBname("BBS");
		vo.setIp(request.getRemoteAddr());
		
		b_dao.add(vo);
		
		mv.setViewName("redirect:/list.inc");
		
		return mv;
	}
	
}
