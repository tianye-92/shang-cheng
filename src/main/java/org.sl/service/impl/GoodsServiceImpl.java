package org.sl.service.impl;

import org.sl.dao.GoodsMapper;
import org.sl.pojo.Goods;
import org.sl.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private GoodsMapper goodsMapper;

	@Override
	public List<Goods> getGoosList(Goods goods) throws Exception {
		// TODO Auto-generated method stub
		return goodsMapper.getGoosList(goods);
	}

	@Override
	public Goods getGoodsById(Goods goods) throws Exception {
		// TODO Auto-generated method stub
		return goodsMapper.getGoodsById(goods);
	}

	@Override
	public int updateGoods(Goods goods) throws Exception {
		// TODO Auto-generated method stub
		return goodsMapper.updateGoods(goods);
	}

	@Override
	public int addGoods(Goods goods) throws Exception {
		// TODO Auto-generated method stub
		return goodsMapper.addGoods(goods);
	}

	@Override
	public int deleteGoods(Goods goods) throws Exception {
		// TODO Auto-generated method stub
		return goodsMapper.deleteGoods(goods);
	}

	@Override
	public int count(Goods goods) throws Exception {
		// TODO Auto-generated method stub
		return goodsMapper.count(goods);
	}

	@Override
	public int isgoodspack(Goods goods) throws Exception {
		// TODO Auto-generated method stub
		return goodsMapper.isgoodspack(goods);
	}

	@Override
	public List<Goods> getGoosListNoFenYe(Goods goods) throws Exception {
		// TODO Auto-generated method stub
		return goodsMapper.getGoosListNoFenYe(goods);
	}

}
