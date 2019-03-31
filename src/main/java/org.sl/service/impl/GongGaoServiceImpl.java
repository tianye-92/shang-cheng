package org.sl.service.impl;

import org.sl.dao.GongGaoMapper;
import org.sl.pojo.GongGao;
import org.sl.service.GongGaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GongGaoServiceImpl implements GongGaoService {

	@Autowired
	private GongGaoMapper gongGaoMapper;

	@Override
	public List<GongGao> getGongGaoList(GongGao gongGao) throws Exception {
		// TODO Auto-generated method stub
		return gongGaoMapper.getGongGaoList(gongGao);
	}

	@Override
	public List<GongGao> getShouYeGongGaoList(GongGao gongGao) throws Exception {
		// TODO Auto-generated method stub
		return gongGaoMapper.getShouYeGongGaoList(gongGao);
	}

	@Override
	public int addGongGao(GongGao gongGao) throws Exception {
		// TODO Auto-generated method stub
		return gongGaoMapper.addGongGao(gongGao);
	}

	@Override
	public int updateGongGao(GongGao gongGao) throws Exception {
		// TODO Auto-generated method stub
		return gongGaoMapper.updateGongGao(gongGao);
	}

	@Override
	public int deleteGongGao(GongGao gongGao) throws Exception {
		// TODO Auto-generated method stub
		return gongGaoMapper.deleteGongGao(gongGao);
	}

	@Override
	public GongGao getGongGaoById(GongGao gongGao) throws Exception {
		// TODO Auto-generated method stub
		return gongGaoMapper.getGongGaoById(gongGao);
	}

	@Override
	public int getCount(GongGao gongGao) throws Exception {
		// TODO Auto-generated method stub
		return gongGaoMapper.getCount(gongGao);
	}

	@Override
	public int getShouYeCount(GongGao gongGao) throws Exception {
		// TODO Auto-generated method stub
		return gongGaoMapper.getShouYeCount(gongGao);
	}

}
