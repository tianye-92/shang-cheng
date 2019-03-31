package org.sl.service.impl;

import org.sl.dao.ZiXunMapper;
import org.sl.pojo.ZiXun;
import org.sl.service.ZiXunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZiXunServiceImpl implements ZiXunService {

	@Autowired
	private ZiXunMapper ziXunMapper;

	@Override
	public List<ZiXun> getZiXunList(ZiXun ziXun) throws Exception {
		// TODO Auto-generated method stub
		return ziXunMapper.getZiXunList(ziXun);
	}

	@Override
	public List<ZiXun> getZiXunListFenYe(ZiXun ziXun) throws Exception {
		// TODO Auto-generated method stub
		return ziXunMapper.getZiXunListFenYe(ziXun);
	}

	@Override
	public ZiXun getZiXunById(ZiXun ziXun) throws Exception {
		// TODO Auto-generated method stub
		return ziXunMapper.getZiXunById(ziXun);
	}

	@Override
	public int addZiXun(ZiXun ziXun) throws Exception {
		// TODO Auto-generated method stub
		return ziXunMapper.addZiXun(ziXun);
	}

	@Override
	public int updateZiXun(ZiXun ziXun) throws Exception {
		// TODO Auto-generated method stub
		return ziXunMapper.updateZiXun(ziXun);
	}

	@Override
	public int updateZiXunFileInfo(ZiXun ziXun) throws Exception {
		// TODO Auto-generated method stub
		return ziXunMapper.updateZiXunFileInfo(ziXun);
	}

	@Override
	public int deleteZiXun(ZiXun ziXun) throws Exception {
		// TODO Auto-generated method stub
		return ziXunMapper.deleteZiXun(ziXun);
	}

	@Override
	public int getCount(ZiXun ziXun) throws Exception {
		// TODO Auto-generated method stub
		return ziXunMapper.getCount(ziXun);
	}

}
