package org.sl.service.impl;

import org.sl.dao.ZiDianMapper;
import org.sl.pojo.ZiDian;
import org.sl.service.ZiDianService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ZiDianServiceImpl implements ZiDianService {

	@Resource
	private ZiDianMapper ziDianMapper;

	@Override
	public List<ZiDian> getZiDianList(ZiDian ziDian) throws Exception {
		// TODO Auto-generated method stub
		return ziDianMapper.getZiDianList(ziDian);
	}

	@Override
	public List<ZiDian> getUserTypeNoZhuCe(ZiDian ziDian) throws Exception {
		// TODO Auto-generated method stub
		return ziDianMapper.getUserTypeNoZhuCe(ziDian);
	}

	@Override
	public List<ZiDian> getOnlyZiDianList() throws Exception {
		// TODO Auto-generated method stub
		return ziDianMapper.getOnlyZiDianList();
	}

	@Override
	public int exis(ZiDian ziDian) throws Exception {
		// TODO Auto-generated method stub
		return ziDianMapper.exis(ziDian);
	}

	@Override
	public int updateZiDian(ZiDian ziDian) throws Exception {
		// TODO Auto-generated method stub
		return ziDianMapper.updateZiDian(ziDian);
	}

	@Override
	public int updateZiDianType(ZiDian ziDian) throws Exception {
		// TODO Auto-generated method stub
		return ziDianMapper.updateZiDianType(ziDian);
	}

	@Override
	public int addZiDian(ZiDian ziDian) throws Exception {
		// TODO Auto-generated method stub
		return ziDianMapper.addZiDian(ziDian);
	}

	@Override
	public int getMaxValueId(ZiDian ziDian) throws Exception {
		// TODO Auto-generated method stub
		return ziDianMapper.getMaxValueId(ziDian);
	}

	@Override
	public int deleteZiDian(ZiDian ziDian) throws Exception {
		// TODO Auto-generated method stub
		return ziDianMapper.deleteZiDian(ziDian);
	}

	@Override
	public int exisByValueName(ZiDian ziDian) throws Exception {
		// TODO Auto-generated method stub
		return ziDianMapper.exisByValueName(ziDian);
	}

}
