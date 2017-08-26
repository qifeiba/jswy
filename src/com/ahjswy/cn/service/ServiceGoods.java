package com.ahjswy.cn.service;

import java.util.LinkedHashMap;
import java.util.List;

import com.ahjswy.cn.bean.GoodEntity;
import com.ahjswy.cn.model.Goods;
import com.ahjswy.cn.model.GoodsUnit;
import com.ahjswy.cn.model.Pricesystem;
import com.ahjswy.cn.request.ReqStrGetGoodsPrice;
import com.ahjswy.cn.request.ReqStrGetGoodsPricePD;
import com.ahjswy.cn.utils.JSONUtil;
import com.ahjswy.cn.utils.PDH;
import com.ahjswy.cn.utils.Utils;
import com.ahjswy.cn.utils.Utils_help;

public class ServiceGoods {
	private String baseAddress = "goods";
	LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

	public String gds_GetAllGoodsBatchPD(String paramString1, String paramString2) {
		String url = Utils.getServiceAddress(this.baseAddress, "getallgoodsbatchpd");
		map.put("goodsid", paramString2);
		map.put("warehouseid", paramString1);
		return new Utils_help().getServiceInfor(url, map);
	}

	public String gds_GetGoodsBatch(String paramString1, String paramString2) {
		String url = Utils.getServiceAddress(this.baseAddress, "getgoodsbatch");
		map.put("goodsid", paramString2);
		map.put("warehouseid", paramString1);
		return new Utils_help().getServiceInfor(url, map);
	}

	public String gds_GetGoodsWarehouses(String paramString, boolean paramBoolean) {
		String url = Utils.getServiceAddress(this.baseAddress, "getgoodswarehouses");
		map.put("goodsid", paramString);
		map.put("isgetbatch", paramBoolean + "");
		return new Utils_help().getServiceInfor(url, map);
	}

	public String gds_GetMultiGoodsPrice(List<ReqStrGetGoodsPrice> paramList, boolean ischeckwarehouse,
			boolean isgetbatch) {
		String url = Utils.getServiceAddress(this.baseAddress, "getmultigoodsprice");
		map.put("parameter", JSONUtil.object2Json(paramList));
		map.put("ischeckwarehouse", ischeckwarehouse + "");
		map.put("isgetbatch", isgetbatch + "");
		return new Utils_help().getServiceInfor(url, map);
	}

	public String gds_GetMultiGoodsPriceDB(List<ReqStrGetGoodsPrice> paramList, String inwarehouseid,
			boolean ischeckwarehouse, boolean isgetbatch) {
		String url = Utils.getServiceAddress(this.baseAddress, "getmultigoodspricedb");

		map.put("parameter", JSONUtil.object2Json(paramList));
		map.put("inwarehouseid", inwarehouseid);
		map.put("ischeckwarehouse", ischeckwarehouse + "");
		map.put("isgetbatch", isgetbatch + "");
		return new Utils_help().getServiceInfor(url, map);
	}

	public String gds_GetMultiGoodsPricePD(List<ReqStrGetGoodsPricePD> paramList) {
		String url = Utils.getServiceAddress(this.baseAddress, "getmultigoodspricepd");
		map.put("parameter", JSONUtil.object2Json(paramList));
		return new Utils_help().getServiceInfor(url, map);
	}

	/**
	 * 添加商品
	 * 
	 * @return
	 */
	public String gds_AddGood(Goods goods, List<Pricesystem> Price, List<GoodsUnit> listGoodUnit) {
		String url = Utils.getServiceAddress(this.baseAddress, "AddGoods");
		if (goods == null) {
			throw new RuntimeException("ReqAddGoods is null");
		}
		GoodEntity goodEntity = new GoodEntity();
		goodEntity.setGoods(JSONUtil.object2Json(goods));
		goodEntity.setGoodsPrice(JSONUtil.object2Json(Price));
		goodEntity.setGoodsunit(JSONUtil.object2Json(listGoodUnit));
		map.put("parameter", JSONUtil.object2Json(goodEntity));
		return new Utils_help().getServiceInfor(url, map);
	}
}