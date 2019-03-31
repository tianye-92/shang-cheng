package org.sl.service.impl;

import org.sl.dao.GoodsPackMapper;
import org.sl.pojo.Goods;
import org.sl.pojo.GoodsPack;
import org.sl.service.GoodsPackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsPackServiceImpl implements GoodsPackService {

	@Autowired
	private GoodsPackMapper goodsPackMapper;

	@Override
	public List<GoodsPack> getGoodsPackList(GoodsPack goodsPack) throws Exception {
		// TODO Auto-generated method stub
		return goodsPackMapper.getGoodsPackList(goodsPack);
	}

	@Override
	public GoodsPack getGoodsPackById(GoodsPack goodsPack) throws Exception {
		// TODO Auto-generated method stub
		return goodsPackMapper.getGoodsPackById(goodsPack);
	}

	@Override
	public int addGoodsPack(GoodsPack goodsPack) throws Exception {
		// TODO Auto-generated method stub
		return goodsPackMapper.addGoodsPack(goodsPack);
	}

	@Override
	public int updateGoodsPack(GoodsPack goodsPack) throws Exception {
		// TODO Auto-generated method stub
		return goodsPackMapper.updateGoodsPack(goodsPack);
	}

	@Override
	public int deleteGoodsPack(GoodsPack goodsPack) throws Exception {
		// TODO Auto-generated method stub
		return goodsPackMapper.deleteGoodsPack(goodsPack);
	}

	@Override
	public int count(GoodsPack goodsPack) throws Exception {
		// TODO Auto-generated method stub
		return goodsPackMapper.count(goodsPack);
	}

	@Override
	public List<Goods> getGoodsById(GoodsPack goodsPack) throws Exception {
		// TODO Auto-generated method stub
		return goodsPackMapper.getGoodsById(goodsPack);
	}

	@Override
	public int getMaxId() throws Exception {
		// TODO Auto-generated method stub
		return goodsPackMapper.getMaxId();
	}

	@Override
	public int addGoodsPackLian(Goods goods) throws Exception {
		// TODO Auto-generated method stub
		return goodsPackMapper.addGoodsPackLian(goods);
	}

	@Override
	public boolean hl_addGoodsPack(GoodsPack goodsPack, List<Goods> goodsList)
			throws Exception {
		// TODO Auto-generated method stub
		if(null != goodsPack){
			goodsPack.setId(goodsPackMapper.getMaxId()+1);
			for (Goods goods : goodsList) {
				//添加之前，先set套餐id，然后添加关联表
				goods.setGoodsPackId(goodsPackMapper.getMaxId()+1);
				goodsPackMapper.addGoodsPackLian(goods);
			}
			goodsPackMapper.addGoodsPack(goodsPack);
		}
		return true;
	}

	@Override
	public boolean hl_updateGoodsPack(GoodsPack goodsPack, List<Goods> goodsList)
			throws Exception {
		// TODO Auto-generated method stub
		if(null != goodsPack){
			//先删除
			goodsPackMapper.deleteGoodsLian(goodsPack);
			//然后修改套餐表
			goodsPackMapper.updateGoodsPack(goodsPack);
			//然后添加关联表
			for (Goods goods : goodsList) {
				goods.setGoodsPackId(goodsPack.getId());
				goodsPackMapper.addGoodsPackLian(goods);
			}
		}
		return true;
	}

	@Override
	public boolean hl_deleteGoodsPack(GoodsPack goodsPack) throws Exception {
		// TODO Auto-generated method stub
		if(null != goodsPack){
			//先删除关联表
			goodsPackMapper.deleteGoodsLian(goodsPack);
			//再删除套餐表
			goodsPackMapper.deleteGoodsPack(goodsPack);
		}
		return false;
	}
}
