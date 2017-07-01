package com.ahjswy.cn.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.ahjswy.cn.app.SystemState;
import com.ahjswy.cn.bean.DefDocItemDD;
import com.ahjswy.cn.bean.Def_Doc;
import com.ahjswy.cn.bean.Def_DocDraft;
import com.ahjswy.cn.bean.PurchaseEntity;
import com.ahjswy.cn.bean.QueryEntity;
import com.ahjswy.cn.bean.SaleEntity;
import com.ahjswy.cn.model.DefDoc;
import com.ahjswy.cn.model.DefDocCG;
import com.ahjswy.cn.model.DefDocExchange;
import com.ahjswy.cn.model.DefDocItem;
import com.ahjswy.cn.model.DefDocItemCG;
import com.ahjswy.cn.model.DefDocItemPD;
import com.ahjswy.cn.model.DefDocItemTH;
import com.ahjswy.cn.model.DefDocItemXS;
import com.ahjswy.cn.model.DefDocPD;
import com.ahjswy.cn.model.DefDocPayType;
import com.ahjswy.cn.model.DefDocTransfer;
import com.ahjswy.cn.model.DefDocXS;
import com.ahjswy.cn.model.DocContainerEntity;
import com.ahjswy.cn.model.User;
import com.ahjswy.cn.request.ReqStrGetDocDetail;
import com.ahjswy.cn.request.ReqStrGetGoodsPrice;
import com.ahjswy.cn.request.ReqStrInitCgDoc;
import com.ahjswy.cn.request.ReqStrInitDoc;
import com.ahjswy.cn.request.ReqStrSearchDoc;
import com.ahjswy.cn.response.DraftEntity;
import com.ahjswy.cn.ui.out_in_goods.PrintEntity;
import com.ahjswy.cn.utils.HttpRequestUtils;
import com.ahjswy.cn.utils.JSONUtil;
import com.ahjswy.cn.utils.TextUtils;
import com.ahjswy.cn.utils.Utils;
import com.ahjswy.cn.utils.Utils_help;

public class ServiceStore {
	private String baseAddress = "store";
	private String baseNewAddress = "LoginService/";
	LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

	public String str_CheckDBDoc(DefDocTransfer paramDefDocTransfer, List<DefDocItem> paramList, List<Long> paramList1,
			boolean paramBoolean) {
		String url = Utils.getServiceAddress(this.baseAddress, "checkdbdoc");
		DocContainerEntity localDocContainerEntity = new DocContainerEntity();
		localDocContainerEntity.setDoctype("43");
		localDocContainerEntity.setDoc(JSONUtil.object2Json(paramDefDocTransfer));
		localDocContainerEntity.setItem(JSONUtil.object2Json(paramList));
		localDocContainerEntity.setDeleteitem(JSONUtil.object2Json(paramList1));
		map.put("parameter", JSONUtil.object2Json(localDocContainerEntity));
		map.put("proposerid", SystemState.getUser().getId());
		map.put("isprint", paramBoolean + "");
		return new Utils_help().getServiceInfor(url, map);
	}

	public String str_CheckPDDoc(DefDocPD paramDefDocPD, List<DefDocItemPD> paramList, List<Long> paramList1,
			boolean paramBoolean) {
		String url = Utils.getServiceAddress(this.baseAddress, "checkpddoc");
		DocContainerEntity localDocContainerEntity = new DocContainerEntity();
		localDocContainerEntity.setDoctype("49");
		localDocContainerEntity.setDoc(JSONUtil.object2Json(paramDefDocPD));
		localDocContainerEntity.setItem(JSONUtil.object2Json(paramList));
		localDocContainerEntity.setDeleteitem(JSONUtil.object2Json(paramList1));
		map.put("parameter", JSONUtil.object2Json(localDocContainerEntity));
		map.put("proposerid", SystemState.getUser().getId());
		map.put("isprint", paramBoolean + "");
		return new Utils_help().getServiceInfor(url, map);
	}

	public String str_CheckXHDoc(DefDocExchange paramDefDocExchange, List<DefDocItem> paramList1,
			List<DefDocItem> paramList2, List<DefDocPayType> paramList, List<Long> paramList3, List<Long> paramList4,
			boolean paramBoolean) {
		String url = Utils.getServiceAddress(this.baseAddress, "checkxhdoc");
		DocContainerEntity localDocContainerEntity = new DocContainerEntity();
		localDocContainerEntity.setDoctype("15");
		localDocContainerEntity.setDoc(JSONUtil.object2Json(paramDefDocExchange));
		localDocContainerEntity.setItem(JSONUtil.object2Json(paramList1));
		localDocContainerEntity.setInitem(JSONUtil.object2Json(paramList2));
		localDocContainerEntity.setPaytype(JSONUtil.object2Json(paramList));
		localDocContainerEntity.setDeleteitem(JSONUtil.object2Json(paramList3));
		localDocContainerEntity.setDeleteinitem(JSONUtil.object2Json(paramList4));
		map.put("parameter", JSONUtil.object2Json(localDocContainerEntity));
		map.put("proposerid", SystemState.getUser().getId());
		map.put("isprint", paramBoolean + "");
		return new Utils_help().getServiceInfor(url, map);
	}

	/* 销售过账 */
	public String str_CheckXSDoc(DefDocXS defDocXS, List<DefDocItemXS> listItem, List<DefDocPayType> paytype,
			List<Long> deleteitem, boolean isprint) {
		String url = Utils.getServiceAddress(this.baseAddress, "checkxsdoc");
		DocContainerEntity docc = new DocContainerEntity();
		defDocXS.setDoctypeid("13");
		docc.setDoctype("13");
		docc.setDoc(JSONUtil.object2Json(defDocXS));
		docc.setItem(JSONUtil.object2Json(listItem));
		docc.setPaytype(JSONUtil.object2Json(paytype));
		docc.setDeleteitem(JSONUtil.object2Json(deleteitem));
		map.put("proposerid", SystemState.getUser().getId());
		map.put("parameter", JSONUtil.object2Json(docc));
		map.put("isprint", isprint + "");
		return new Utils_help().getServiceInfor(url, map);
	}

	/**
	 * 采购单过账 doccg.setIsposted(true); 单据保存 doccg.setIsposted(false); 单据更新 单据id更新
	 * 
	 * @param doccg
	 * @param listPayType
	 * @param goodsitemjson
	 * @return
	 */
	public String savePurchase(Def_Doc doccg, List<DefDocPayType> listPayType, List<DefDocItemCG> goodsitemjson) {
		String url = baseNewAddress + "SavePurchase";
		doccg.setDoctypeid("03");
		doccg.setMakerid(SystemState.getUser().getId());// 单据创建人
		doccg.setMaketime(Utils.getDataTime());// 单据创建的时间
		doccg.setIsavailable(true);
		doccg.setIscompleted(true);
		doccg.setPrinttemplate("2014采购单模板");
		PurchaseEntity entity = new PurchaseEntity();
		entity.setUserid(SystemState.getUser().getId());
		entity.setDocjson(JSONUtil.object2Json(doccg));
		entity.setGoodsitemjson(JSONUtil.object2Json(goodsitemjson));
		entity.setTypelistjson(JSONUtil.object2Json(listPayType));
		return new HttpRequestUtils().Post(url, JSONUtil.object2Json(entity));
	}

	/**
	 * 销售订单 审核
	 * 
	 * @param doccg
	 *            单据信息
	 * @param listItem
	 *            商品列表
	 * @return
	 */
	public String AddSaleReview(Def_Doc doccg, List<DefDocItemDD> listItem) {
		String url = baseNewAddress + "AddSaleReview";
		doccg.setBuilderid(SystemState.getUser().getId());// 开单人
		doccg.setBuildername(SystemState.getUser().getName());// 开单人名称
		doccg.setPrinttemplate("2016销售订单模板");
		doccg.setDoctypeid("12");
		doccg.setMakerid(SystemState.getUser().getId());
		doccg.setMakername(SystemState.getUser().getName());
		doccg.setMaketime(Utils.getDataTime());
		doccg.setIsavailable(true);
		doccg.setIssettleup(true);
		SaleEntity entity = new SaleEntity();
		entity.setUserid(SystemState.getUser().getId());
		entity.setDocjson(JSONUtil.object2Json(doccg));
		entity.setDocddjson(JSONUtil.object2Json(listItem));
		return new HttpRequestUtils().Post(url, JSONUtil.object2Json(entity));
	}

	// 打印单据
	public String PrintDoc(Def_DocDraft draft) {
		String url = baseNewAddress + "PrintDoc";
		draft.setBuilderid(SystemState.getUser().getId());// 开单人
		draft.setBuildername(SystemState.getUser().getName());// 开单人名称
		draft.setMakerid(SystemState.getUser().getId());// 创建人id
		draft.setMakername(SystemState.getUser().getName());// 创建人名称
		draft.setObjectid(SystemState.getUser().getId());// 对象人
		draft.setObjectname(SystemState.getUser().getName());// 对象人名称
		draft.setBuildtime(Utils.getDataTime());// 创建的时间
		PrintEntity entity = new PrintEntity();
		entity.setDocdraftjson(JSONUtil.object2Json(draft));
		entity.setUserid(SystemState.getUser().getId());// 用户id
		return new HttpRequestUtils().Post(url, JSONUtil.object2Json(entity));
	}

	/*
	 * New 获取供应商商品的单价,每次只能查询一条记录 customerid goodsid unitid
	 */
	public String GetGoodsPrice(Def_Doc doccg, String defdocitem) {
		String url = baseNewAddress + "GetGoodsPrice";
		if (doccg == null || !TextUtils.isEmptyS(doccg.getCustomerid()) || TextUtils.isEmpty(defdocitem)) {
			return "";
		}
		PurchaseEntity pur = new PurchaseEntity();
		pur.setUserid(SystemState.getUser().getId());
		pur.setDocjson(JSONUtil.object2Json(doccg));
		pur.setGoodsitemjson(defdocitem);
		return new HttpRequestUtils().Post(url, JSONUtil.object2Json(pur));
	}

	/**
	 * New 获取客户商品客史价格，每次只能查询一条记录 customerid goodsid unitid
	 * 
	 * @param customerid
	 *            客户id
	 * @param goodsid
	 *            商品id
	 * @param unitid
	 *            单位id
	 * @return
	 */

	public String GetCustomerPrice(String customerid, String goodsid, String unitid) {
		String url = baseNewAddress + "GetCustomerPrice";
		if (TextUtils.isEmpty(customerid) || TextUtils.isEmpty(unitid)) {
			return "";
		}
		String customer = "{\"customerid\":\"" + customerid + "\"}";
		ReqStrGetGoodsPrice itemGoods = new ReqStrGetGoodsPrice();
		itemGoods.setGoodsid(goodsid);
		itemGoods.setUnitid(unitid);
		List<ReqStrGetGoodsPrice> list = new ArrayList<ReqStrGetGoodsPrice>();
		list.add(itemGoods);
		PurchaseEntity pur = new PurchaseEntity();
		pur.setUserid(SystemState.getUser().getId());
		pur.setDocjson(customer);
		pur.setGoodsitemjson(JSONUtil.object2Json(list));
		return new HttpRequestUtils().Post(url, JSONUtil.object2Json(pur));
	}

	public String GetCustomerPrices(String customerid, String goodsid, String unitid) {
		String url = baseNewAddress + "GetCustomerPrice";
		if (TextUtils.isEmpty(customerid) || TextUtils.isEmpty(unitid)) {
			return "";
		}
		String customer = "{\"customerid\":\"" + customerid + "\"}";
		ReqStrGetGoodsPrice itemGoods = new ReqStrGetGoodsPrice();
		itemGoods.setGoodsid(goodsid);
		itemGoods.setUnitid(unitid);
		List<ReqStrGetGoodsPrice> list = new ArrayList<ReqStrGetGoodsPrice>();
		list.add(itemGoods);
		PurchaseEntity pur = new PurchaseEntity();
		pur.setUserid(SystemState.getUser().getId());
		pur.setDocjson(customer);
		pur.setGoodsitemjson(JSONUtil.object2Json(list));
		return new HttpRequestUtils().Posts(url, JSONUtil.object2Json(pur));
	}

	/**
	 * 删除单据
	 * 
	 * @param makerid
	 *            处理人的id
	 * @param doctypeid
	 *            单据类型
	 * @param showid
	 *            单据号
	 * @param id
	 * @return
	 */
	public String DelDraft(String makerid, Long docid, String doctypeid) {
		String url = baseNewAddress + "DelDraft";
		DraftEntity drafts = new DraftEntity();
		drafts.setMakerid(makerid);
		drafts.setDoctypeid(doctypeid);
		drafts.setDocid(docid + "");
		return new HttpRequestUtils().Post(url, JSONUtil.object2Json(drafts));
	}

	/**
	 * 获取商品类别
	 * 
	 * @param index
	 *            查询当前的页数
	 * @param length
	 *            数据的大小
	 * 
	 * @return
	 */
	public String GoodsClass(int index, int length) {
		String url = baseNewAddress + "GoodsClass";
		QueryEntity queryentity = new QueryEntity();
		queryentity.setIndex(index);
		queryentity.setLength(length);
		return new HttpRequestUtils().Post(url, JSONUtil.object2Json(queryentity));
	}

	/**
	 * 获取商品单位
	 * 
	 * @param goodsId
	 *            商品的id 返回当前的 商品对应的单位 NULL 返回所有 商品的单位
	 * @return
	 */
	public String GetGoodsUnit(String goodsId) {
		String url = baseNewAddress + "GetGoodsUnit";
		String req = "{\"goodsid\":" + goodsId + "}";
		return new HttpRequestUtils().Post(url, req);
	}

	/**
	 * 获取体系价格
	 * 
	 * @param isavailable
	 * @return
	 */
	public String GetPriceSystem() {
		String url = baseNewAddress + "GetPriceSystem";
		return new HttpRequestUtils().Post(url, "true");
	}

	public String str_CheckXTDoc(DefDoc paramDefDoc, List<DefDocItem> paramList, List<DefDocPayType> paramList1,
			List<Long> paramList2, boolean paramBoolean) {
		String url = Utils.getServiceAddress(this.baseAddress, "checkxtdoc");
		DocContainerEntity localDocContainerEntity = new DocContainerEntity();
		localDocContainerEntity.setDoctype("14");
		localDocContainerEntity.setDoc(JSONUtil.object2Json(paramDefDoc));
		localDocContainerEntity.setItem(JSONUtil.object2Json(paramList));
		localDocContainerEntity.setPaytype(JSONUtil.object2Json(paramList1));
		localDocContainerEntity.setDeleteitem(JSONUtil.object2Json(paramList2));
		map.put("proposerid", SystemState.getUser().getId());
		map.put("parameter", JSONUtil.object2Json(localDocContainerEntity));
		map.put("isprint", paramBoolean + "");
		return new Utils_help().getServiceInfor(url, map);
	}

	public String str_DeleteDoc(String paramString, long paramLong) {
		String url = Utils.getServiceAddress(this.baseAddress, "deletedoc");
		map.put("docid", paramLong + "");
		map.put("doctype", paramString);
		map.put("proposerid", SystemState.getUser().getId());
		return new Utils_help().getServiceInfor(url, map);
	}

	public String str_DeleteCGDoc(String doctype, long docid) {
		String url = Utils.getServiceAddress(this.baseAddress, "deletecgdoc");
		map.put("docid", docid + "");
		map.put("doctype", doctype);
		map.put("proposerid", SystemState.getUser().getId());
		return new Utils_help().getServiceInfor(url, map);
	}

	public String str_GetCustomerAddress(String paramString) {
		String url = Utils.getServiceAddress(this.baseAddress, "getcustomeraddress");
		map.put("customerid", paramString);
		return new Utils_help().getServiceInfor(url, map);
	}

	public String str_GetCustomerHistory(String paramString) {
		String url = Utils.getServiceAddress(this.baseAddress, "getcustomerhistory");
		map.put("customerid", paramString);
		map.put("accountset", SystemState.getAccountSet().getName());
		return new Utils_help().getServiceInfor(url, map);
	}

	public String str_GetDBDocDetail(long paramLong) {
		String url = Utils.getServiceAddress(this.baseAddress, "getdbdocdetail");
		ReqStrGetDocDetail localReqStrGetDocDetail = new ReqStrGetDocDetail();
		localReqStrGetDocDetail.setDoctype("43");
		localReqStrGetDocDetail.setDocid(paramLong);
		map.put("parameter", JSONUtil.object2Json(localReqStrGetDocDetail));
		return new Utils_help().getServiceInfor(url, map);
	}

	public String str_GetDocItemXS(long paramLong) {
		String url = Utils.getServiceAddress(this.baseAddress, "getdocitemxs");
		map.put("docid", paramLong + "");
		return new Utils_help().getServiceInfor(url, map);
	}

	public String str_GetPDDocDetail(long paramLong) {
		String url = Utils.getServiceAddress(this.baseAddress, "getpddocdetail");
		ReqStrGetDocDetail localReqStrGetDocDetail = new ReqStrGetDocDetail();
		localReqStrGetDocDetail.setDoctype("49");
		localReqStrGetDocDetail.setDocid(paramLong);
		map.put("parameter", JSONUtil.object2Json(localReqStrGetDocDetail));
		return new Utils_help().getServiceInfor(url, map);
	}

	public String str_GetPromotionRule(String paramString1, String paramString2) {
		String url = Utils.getServiceAddress(this.baseAddress, "getpromotionrule");
		map.put("promotionid", paramString1);
		map.put("warehouseid", paramString2);
		return new Utils_help().getServiceInfor(url, map);
	}

	public String str_GetXHDocDetail(long paramLong) {
		String url = Utils.getServiceAddress(this.baseAddress, "getxhdocdetail");
		ReqStrGetDocDetail localReqStrGetDocDetail = new ReqStrGetDocDetail();
		localReqStrGetDocDetail.setDoctype("15");
		localReqStrGetDocDetail.setDocid(paramLong);
		map.put("parameter", JSONUtil.object2Json(localReqStrGetDocDetail));
		return new Utils_help().getServiceInfor(url, map);
	}

	public String str_GetXSDocDetail(long paramLong) {
		String url = Utils.getServiceAddress(this.baseAddress, "getxsdocdetail");
		ReqStrGetDocDetail localReqStrGetDocDetail = new ReqStrGetDocDetail();
		localReqStrGetDocDetail.setDoctype("13");
		localReqStrGetDocDetail.setDocid(paramLong);
		map.put("parameter", JSONUtil.object2Json(localReqStrGetDocDetail));
		return new Utils_help().getServiceInfor(url, map);
	}

	public String str_GetXTDocDetail(long paramLong) {
		String url = Utils.getServiceAddress(this.baseAddress, "getxtdocdetail");
		ReqStrGetDocDetail localReqStrGetDocDetail = new ReqStrGetDocDetail();
		localReqStrGetDocDetail.setDoctype("14");
		localReqStrGetDocDetail.setDocid(paramLong);
		map.put("parameter", JSONUtil.object2Json(localReqStrGetDocDetail));
		return new Utils_help().getServiceInfor(url, map);
	}

	public String str_InitDBDoc(String paramString1, String paramString2, String paramString3) {
		String url = Utils.getServiceAddress(this.baseAddress, "initdbdoc");
		ReqStrInitDoc localReqStrInitDoc = new ReqStrInitDoc();
		localReqStrInitDoc.setDepartmentID(paramString1);
		localReqStrInitDoc.setWarehouseID(paramString2);
		localReqStrInitDoc.setWarehouseID2(paramString3);
		User localUser = SystemState.getUser();
		localReqStrInitDoc.setMakerID(localUser.getId());
		localReqStrInitDoc.setMakerName(localUser.getName());
		map.put("parameter", JSONUtil.object2Json(localReqStrInitDoc));
		return new Utils_help().getServiceInfor(url, map);
	}

	public String str_InitPDDoc(String paramString1, String paramString2) {
		String url = Utils.getServiceAddress(this.baseAddress, "initpddoc");
		ReqStrInitDoc localReqStrInitDoc = new ReqStrInitDoc();
		localReqStrInitDoc.setDepartmentID(paramString1);
		localReqStrInitDoc.setWarehouseID(paramString2);
		User localUser = SystemState.getUser();
		localReqStrInitDoc.setMakerID(localUser.getId());
		localReqStrInitDoc.setMakerName(localUser.getName());
		map.put("parameter", JSONUtil.object2Json(localReqStrInitDoc));
		return new Utils_help().getServiceInfor(url, map);
	}

	public String str_InitXHDoc(String paramString1, String paramString2, String paramString3) {
		String url = Utils.getServiceAddress(this.baseAddress, "initxhdoc");
		ReqStrInitDoc localReqStrInitDoc = new ReqStrInitDoc();
		localReqStrInitDoc.setDepartmentID(paramString1);
		localReqStrInitDoc.setWarehouseID(paramString2);
		localReqStrInitDoc.setWarehouseID2(paramString3);
		User localUser = SystemState.getUser();
		localReqStrInitDoc.setMakerID(localUser.getId());
		localReqStrInitDoc.setMakerName(localUser.getName());
		map.put("parameter", JSONUtil.object2Json(localReqStrInitDoc));
		return new Utils_help().getServiceInfor(url, map);
	}

	// 销售开单
	public String str_InitXSDoc(String departmentID, String warehouseID) {
		String url = Utils.getServiceAddress(this.baseAddress, "initxsdoc");
		ReqStrInitDoc localReqStrInitDoc = new ReqStrInitDoc();
		localReqStrInitDoc.setDepartmentID(departmentID);
		localReqStrInitDoc.setWarehouseID(warehouseID);
		User localUser = SystemState.getUser();
		localReqStrInitDoc.setMakerID(localUser.getId());
		localReqStrInitDoc.setMakerName(localUser.getName());
		map.put("parameter", JSONUtil.object2Json(localReqStrInitDoc));
		return new Utils_help().getServiceInfor(url, map);
	}

	public String str_InitXTDoc(String paramString1, String paramString2) {
		String url = Utils.getServiceAddress(this.baseAddress, "initxtdoc");
		ReqStrInitDoc localReqStrInitDoc = new ReqStrInitDoc();
		localReqStrInitDoc.setDepartmentID(paramString1);
		localReqStrInitDoc.setWarehouseID(paramString2);
		User localUser = SystemState.getUser();
		localReqStrInitDoc.setMakerID(localUser.getId());
		localReqStrInitDoc.setMakerName(localUser.getName());
		map.put("parameter", JSONUtil.object2Json(localReqStrInitDoc));
		return new Utils_help().getServiceInfor(url, map);
	}

	// 采购入库开单
	public String str_InitCGDoc(String departmentID, String warehouseID) {
		String url = Utils.getServiceAddress(baseAddress, "initcgdoc");
		ReqStrInitCgDoc localReqStrInitDoc = new ReqStrInitCgDoc();
		localReqStrInitDoc.setDepartmentID(departmentID);
		localReqStrInitDoc.setWarehouseID(warehouseID);
		User localUser = SystemState.getUser();
		localReqStrInitDoc.setMakerID(localUser.getId());
		localReqStrInitDoc.setMakerName(localUser.getName());
		map.put("parameter", JSONUtil.object2Json(localReqStrInitDoc));
		return new Utils_help().getServiceInfor(url, map);
	}

	public String str_PrintDoc(String paramString, long paramLong) {
		String url = Utils.getServiceAddress(this.baseAddress, "printdoc");
		map.put("doctype", paramString);
		map.put("docid", paramLong + "");
		map.put("proposerid", SystemState.getUser().getId());
		return new Utils_help().getServiceInfor(url, map);
	}

	public String str_SaveDBDoc(DefDocTransfer paramDefDocTransfer, List<DefDocItem> paramList, List<Long> paramList1) {
		String url = Utils.getServiceAddress(this.baseAddress, "savedbdoc");
		DocContainerEntity localDocContainerEntity = new DocContainerEntity();
		localDocContainerEntity.setDoctype("43");
		localDocContainerEntity.setDoc(JSONUtil.object2Json(paramDefDocTransfer));
		localDocContainerEntity.setItem(JSONUtil.object2Json(paramList));
		localDocContainerEntity.setDeleteitem(JSONUtil.object2Json(paramList1));
		map.put("parameter", JSONUtil.object2Json(localDocContainerEntity));
		map.put("proposerid", SystemState.getUser().getId());
		return new Utils_help().getServiceInfor(url, map);
	}

	public String str_SavePDDoc(DefDocPD paramDefDocPD, List<DefDocItemPD> paramList, List<Long> paramList1) {
		String url = Utils.getServiceAddress(this.baseAddress, "savepddoc");
		DocContainerEntity localDocContainerEntity = new DocContainerEntity();
		localDocContainerEntity.setDoctype("49");
		localDocContainerEntity.setDoc(JSONUtil.object2Json(paramDefDocPD));
		localDocContainerEntity.setItem(JSONUtil.object2Json(paramList));
		localDocContainerEntity.setDeleteitem(JSONUtil.object2Json(paramList1));
		map.put("parameter", JSONUtil.object2Json(localDocContainerEntity));
		map.put("proposerid", SystemState.getUser().getId());
		return new Utils_help().getServiceInfor(url, map);
	}

	public String str_SaveXHDoc(DefDocExchange paramDefDocExchange, List<DefDocItem> paramList1,
			List<DefDocItem> paramList2, List<DefDocPayType> paramList, List<Long> paramList3, List<Long> paramList4) {
		String url = Utils.getServiceAddress(this.baseAddress, "savexhdoc");
		DocContainerEntity localDocContainerEntity = new DocContainerEntity();
		localDocContainerEntity.setDoctype("15");
		localDocContainerEntity.setDoc(JSONUtil.object2Json(paramDefDocExchange));
		localDocContainerEntity.setItem(JSONUtil.object2Json(paramList1));
		localDocContainerEntity.setInitem(JSONUtil.object2Json(paramList2));
		localDocContainerEntity.setPaytype(JSONUtil.object2Json(paramList));
		localDocContainerEntity.setDeleteitem(JSONUtil.object2Json(paramList3));
		localDocContainerEntity.setDeleteinitem(JSONUtil.object2Json(paramList4));
		map.put("parameter", JSONUtil.object2Json(localDocContainerEntity));
		map.put("proposerid", SystemState.getUser().getId());
		return new Utils_help().getServiceInfor(url, map);
	}

	/* 销售单据保存 */
	public String str_SaveXSDoc(DefDocXS defdocxs, List<DefDocItemXS> listItem, List<DefDocPayType> paramList1,
			List<Long> listItemDelete) {
		String url = Utils.getServiceAddress(this.baseAddress, "savexsdoc");
		defdocxs.setIsposted(false);
		defdocxs.setIssettleup(false);
		DocContainerEntity localDocContainerEntity = new DocContainerEntity();
		localDocContainerEntity.setDoctype("13");
		localDocContainerEntity.setDoc(JSONUtil.object2Json(defdocxs));
		localDocContainerEntity.setItem(JSONUtil.object2Json(listItem));
		localDocContainerEntity.setPaytype(JSONUtil.object2Json(paramList1));
		localDocContainerEntity.setDeleteitem(JSONUtil.object2Json(listItemDelete));
		// 申请人id（那个人保存）
		map.put("proposerid", SystemState.getUser().getId());
		map.put("parameter", JSONUtil.object2Json(localDocContainerEntity));
		return new Utils_help().getServiceInfor(url, map);
	}

	public String str_SaveCGDoc(DefDocCG defdoccg, List<DefDocItemCG> defdocitemcgList,
			List<DefDocPayType> defdocpaytype, List<Long> deleteitem) {
		String url = Utils.getServiceAddress(baseAddress, "savecgdoc");
		DocContainerEntity localDocContainerEntity = new DocContainerEntity();
		localDocContainerEntity.setDoctype("101");
		localDocContainerEntity.setDoc(JSONUtil.object2Json(defdoccg));
		localDocContainerEntity.setItem(JSONUtil.object2Json(defdocitemcgList));
		localDocContainerEntity.setPaytype(JSONUtil.object2Json(defdocpaytype));
		localDocContainerEntity.setDeleteitem(JSONUtil.object2Json(deleteitem));
		map.put("proposerid", SystemState.getUser().getId());
		map.put("parameter", JSONUtil.object2Json(localDocContainerEntity));
		return new Utils_help().getServiceInfor(url, map);
	}

	// 采购退货保存
	public String str_SaveTHDoc(DefDocCG defdoccg, List<DefDocItemTH> defdocitemcgList,
			List<DefDocPayType> defdocpaytype, List<Long> deleteitem) {
		String url = Utils.getServiceAddress(baseAddress, "savecgdoc");
		DocContainerEntity localDocContainerEntity = new DocContainerEntity();
		localDocContainerEntity.setDoctype("101");
		localDocContainerEntity.setDoc(JSONUtil.object2Json(defdoccg));
		localDocContainerEntity.setItem(JSONUtil.object2Json(defdocitemcgList));
		localDocContainerEntity.setPaytype(JSONUtil.object2Json(defdocpaytype));
		localDocContainerEntity.setDeleteitem(JSONUtil.object2Json(deleteitem));
		map.put("proposerid", SystemState.getUser().getId());
		map.put("parameter", JSONUtil.object2Json(localDocContainerEntity));
		return new Utils_help().getServiceInfor(url, map);
	}

	public String str_SaveXTDoc(DefDoc paramDefDoc, List<DefDocItem> paramList, List<DefDocPayType> paramList1,
			List<Long> paramList2) {
		String url = Utils.getServiceAddress(this.baseAddress, "savextdoc");
		DocContainerEntity localDocContainerEntity = new DocContainerEntity();
		localDocContainerEntity.setDoctype("14");
		localDocContainerEntity.setDoc(JSONUtil.object2Json(paramDefDoc));
		localDocContainerEntity.setItem(JSONUtil.object2Json(paramList));
		localDocContainerEntity.setPaytype(JSONUtil.object2Json(paramList1));
		localDocContainerEntity.setDeleteitem(JSONUtil.object2Json(paramList2));
		localDocContainerEntity.setDeleteinitem("");
		localDocContainerEntity.setInfo("");
		localDocContainerEntity.setInitem("");
		map.put("parameter", JSONUtil.object2Json(localDocContainerEntity));
		map.put("proposerid", SystemState.getUser().getId());
		return new Utils_help().getServiceInfor(url, map);
	}

	public String str_SearchDBDoc(ReqStrSearchDoc paramReqStrSearchDoc) {
		String url = Utils.getServiceAddress(this.baseAddress, "searchdbdoc");
		paramReqStrSearchDoc.setDoctype("43");
		map.put("parameter", JSONUtil.object2Json(paramReqStrSearchDoc));
		map.put("proposerid", SystemState.getUser().getId());
		return new Utils_help().getServiceInfor(url, map);
	}

	public String str_SearchPDDoc(ReqStrSearchDoc paramReqStrSearchDoc) {
		String url = Utils.getServiceAddress(this.baseAddress, "searchpddoc");
		paramReqStrSearchDoc.setDoctype("49");
		map.put("parameter", JSONUtil.object2Json(paramReqStrSearchDoc));
		map.put("proposerid", SystemState.getUser().getId());
		return new Utils_help().getServiceInfor(url, map);
	}

	public String str_SearchXSDoc(ReqStrSearchDoc paramReqStrSearchDoc) {
		String url = Utils.getServiceAddress(this.baseAddress, "searchxsdoc");
		map.put("parameter", JSONUtil.object2Json(paramReqStrSearchDoc));
		map.put("proposerid", SystemState.getUser().getId());
		return new Utils_help().getServiceInfor(url, map);
	}

}