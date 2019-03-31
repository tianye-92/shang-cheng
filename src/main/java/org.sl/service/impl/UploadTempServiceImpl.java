package org.sl.service.impl;

import org.sl.dao.UploadTempMapper;
import org.sl.pojo.UploadTemp;
import org.sl.service.UploadTempService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UploadTempServiceImpl implements UploadTempService {

	@Resource
	private UploadTempMapper uploadTempMapper;

	@Override
	public List<UploadTemp> getList(UploadTemp uploadTemp) throws Exception {
		// TODO Auto-generated method stub
		return uploadTempMapper.getList(uploadTemp);
	}

	@Override
	public int add(UploadTemp uploadTemp) throws Exception {
		// TODO Auto-generated method stub
		return uploadTempMapper.add(uploadTemp);
	}

	@Override
	public int delete(UploadTemp uploadTemp) throws Exception {
		// TODO Auto-generated method stub
		return uploadTempMapper.delete(uploadTemp);
	}

}
