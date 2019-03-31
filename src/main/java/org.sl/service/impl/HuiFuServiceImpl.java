package org.sl.service.impl;

import org.sl.dao.HuiFuMapper;
import org.sl.pojo.HuiFu;
import org.sl.service.HuiFuService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class HuiFuServiceImpl implements HuiFuService {

	@Resource
	private HuiFuMapper huiFuMapper;

	@Override
	public List<HuiFu> getHuiFuList(HuiFu huiFu) throws Exception {
		// TODO Auto-generated method stub
		return huiFuMapper.getHuiFuList(huiFu);
	}

	@Override
	public int getCount(HuiFu huiFu) throws Exception {
		// TODO Auto-generated method stub
		return huiFuMapper.getCount(huiFu);
	}

	@Override
	public int deleteHuiFu(HuiFu huiFu) throws Exception {
		// TODO Auto-generated method stub
		return huiFuMapper.deleteHuiFu(huiFu);
	}

	@Override
	public int addHuiFu(HuiFu huiFu) throws Exception {
		// TODO Auto-generated method stub
		return huiFuMapper.addHuiFu(huiFu);
	}

}
