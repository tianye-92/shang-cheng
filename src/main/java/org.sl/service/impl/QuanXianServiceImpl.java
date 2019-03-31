package org.sl.service.impl;

import org.sl.dao.FunctionMapper;
import org.sl.dao.QuanXianMapper;
import org.sl.pojo.Function;
import org.sl.pojo.QuanXian;
import org.sl.service.QuanXianService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class QuanXianServiceImpl implements QuanXianService {

	@Resource
	private QuanXianMapper quanXianMapper;

	@Resource
	private FunctionMapper functionMapper;

	@Override
	public int getQuanXianCount(QuanXian quanXian) throws Exception {
		// TODO Auto-generated method stub
		return quanXianMapper.getQuanXianCount(quanXian);
	}

	@Override
	public int addQuanXian(QuanXian quanXian) throws Exception {
		// TODO Auto-generated method stub
		return quanXianMapper.addQuanXian(quanXian);
	}

	@Override
	public int updateQuanXian(QuanXian quanXian) throws Exception {
		// TODO Auto-generated method stub
		return quanXianMapper.updateQuanXian(quanXian);
	}

	@Override
	public int deleteQuanXian(QuanXian quanXian) throws Exception {
		// TODO Auto-generated method stub
		return quanXianMapper.deleteQuanXian(quanXian);
	}

	@Override
	public boolean hl_addAuthority(String[] ids, String createdBy)
			throws Exception {
		// TODO Auto-generated method stub
		QuanXian quanXian = new QuanXian();
		quanXian.setRoleId(Integer.parseInt(ids[0]));
		quanXianMapper.deleteQuanXian(quanXian);
		String idsSqlString = "";
		for(int i=1;i<ids.length;i++){
			idsSqlString += Integer.parseInt(ids[i])+",";
		}
		if(idsSqlString != null && idsSqlString.contains(",")){
			idsSqlString = idsSqlString.substring(0,idsSqlString.lastIndexOf(","));

			List<Function> fList = functionMapper.getFunctionListByIn(idsSqlString);

			if(null != fList && fList.size() > 0){

				for(Function function : fList){
					quanXian.setFunctionId(function.getId());
					quanXian.setCreationTime(new Date());
					quanXian.setCreatedBy(createdBy);
					quanXianMapper.addQuanXian(quanXian);
				}
			}
		}
		return true;
	}

	@Override
	public boolean hl_delAddAuthority(QuanXian quanXian, String checkFuncList)
			throws Exception {
		// TODO Auto-generated method stub
		String[] funcList = null;
		quanXianMapper.deleteQuanXian(quanXian);
		if(null != checkFuncList && !checkFuncList.equals("")){
			funcList = checkFuncList.split(",");
			for(int i = 0; i < funcList.length; i++){
				quanXian.setFunctionId(Integer.valueOf(funcList[i]));
				quanXianMapper.addQuanXian(quanXian);
			}
		}
		return true;
	}
}
