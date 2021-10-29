package mybatis.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mybatis.vo.BbsVO;

@Component
public class BbsDAO {

	@Autowired
	private SqlSessionTemplate ss;
	
	//원하는 페이지의 게시물목록 기능
	public BbsVO[] getList(String begin, String end, String bname) {
		BbsVO[] ar = null;
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("begin", begin);
		map.put("end", end);
		map.put("bname", bname);
		
		List<BbsVO> list = ss.selectList("bbs.list", map);
		if(list != null && list.size() > 0) {
			ar = new BbsVO[list.size()];
			list.toArray(ar);
		}
		
		return ar;
	}
	
	public int getTotalCount(String bname) {
		return ss.selectOne("bbs.totalCount", bname);
	}
	
	//기본키를 인자로 하여 원글 검색
	public BbsVO getBbs(String b_idx) {
		return ss.selectOne("bbs.getBbs", b_idx);
	}
	
	public int add(BbsVO vo) {
		return ss.insert("bbs.add",vo);
	}
	
	public int editBbs(BbsVO vo) {
		return ss.update("bbs.editBbs", vo);
	}
	
	public int delBbs(String b_idx) {
		return ss.update("bbs.del", b_idx);
	}
}
