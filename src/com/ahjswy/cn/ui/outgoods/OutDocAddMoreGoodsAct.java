package com.ahjswy.cn.ui.outgoods;

import java.util.ArrayList;
import java.util.List;

import com.ahjswy.cn.R;
import com.ahjswy.cn.app.RequestHelper;
import com.ahjswy.cn.dao.GoodsDAO;
import com.ahjswy.cn.dao.GoodsUnitDAO;
import com.ahjswy.cn.model.DefDocItemXS;
import com.ahjswy.cn.model.DefDocXS;
import com.ahjswy.cn.model.GoodsThin;
import com.ahjswy.cn.model.GoodsUnit;
import com.ahjswy.cn.request.ReqStrGetGoodsPrice;
import com.ahjswy.cn.response.RespGoodsWarehouse;
import com.ahjswy.cn.scaner.Scaner;
import com.ahjswy.cn.scaner.Scaner.ScanerBarcodeListener;
import com.ahjswy.cn.service.ServiceGoods;
import com.ahjswy.cn.ui.BaseActivity;
import com.ahjswy.cn.utils.DocUtils;
import com.ahjswy.cn.utils.JSONUtil;
import com.ahjswy.cn.utils.PDH;
import com.ahjswy.cn.utils.Utils;
import com.ahjswy.cn.views.Dialog_listCheckBox;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import mexxen.mx5010.barcode.BarcodeConfig;
import mexxen.mx5010.barcode.BarcodeEvent;
import mexxen.mx5010.barcode.BarcodeListener;
import mexxen.mx5010.barcode.BarcodeManager;

/**
 * 销售单数量添加Activity
 * 
 * @author Administrator
 *
 */
public class OutDocAddMoreGoodsAct extends BaseActivity {
	private List<DefDocItemXS> items;
	private OutDocAddMoreAdapter adapter;
	private DefDocXS doc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_out_doc_add_moregoods);
		intView();
		addListener();
	}

	private void intView() {
		lv_commodity_add = (ListView) findViewById(R.id.lv_commodity_add);
		items = JSONUtil.str2list(getIntent().getStringExtra("items"), DefDocItemXS.class);
		doc = (DefDocXS) getIntent().getSerializableExtra("doc");
		adapter = new OutDocAddMoreAdapter(this);
		dialog = new Dialog_listCheckBox(this);
		adapter.setDoc(doc);
		setInitItem();
	}

	ArrayList<DefDocItemXS> Newitems;

	public void addListener() {
		if (Newitems == null) {
			Newitems = new ArrayList<DefDocItemXS>();
		}
		scaner = Scaner.factory(this);
		scaner.setBarcodeListener(barcodeListener);
		// if (bm == null) {
		// bm = new BarcodeManager(this);
		// }
		// bm.addListener(new BarcodeListener() {
		// @Override
		// public void barcodeEvent(BarcodeEvent event) {
		// if (event.getOrder().equals("SCANNER_READ")) {
		// if (dialog != null) {
		// dialog.dismiss();
		// }
		// readBarcode(bm.getBarcode().toString().trim());
		// }
		//
		// }
		// });
		// // 扫码枪 功能调用 先new 对相 在调用
		// barcodeConfig = new BarcodeConfig(this);
		// // 设置条码输出模式 不显示模式(复制到粘贴板)
		// barcodeConfig.setOutputMode(2);

	}

	ScanerBarcodeListener barcodeListener = new ScanerBarcodeListener() {

		@Override
		public void setBarcode(String barcode) {
			if (dialog != null) {
				dialog.dismiss();
			}
			readBarcode(barcode);
		}
	};

	@Override
	protected void onPause() {
		super.onPause();
		// deleBm();
		scaner.removeListener();
	}

	protected void readBarcode(String barcode) {
		ArrayList<GoodsThin> goodsThinList = new GoodsDAO().getGoodsThinList(barcode);
		if (goodsThinList.size() == 1) {
			long maxTempItemId = getMaxTempItemId();
			DefDocItemXS fillItem = fillItem(goodsThinList.get(0), 0.0D, 0.0D, maxTempItemId + 1L);
			Newitems.add(fillItem);
			addItems();
		} else if (goodsThinList.size() > 1) {

			dialog.setGoods(goodsThinList);
			dialog.setTempGoods(goodsThinList);
			dialog.ShowMe();
			dialog.ensure(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
					List<GoodsThin> select = dialog.getSelect();
					long maxTempItemId = getMaxTempItemId();
					for (int i = 0; i < select.size(); i++) {
						maxTempItemId += 1L;
						DefDocItemXS fillItem = fillItem(select.get(i), 0.0D, 0.0D, maxTempItemId);
						Newitems.add(fillItem);
					}
					addItems();
				}
			});
		} else {
			PDH.showFail("没有查找到商品！可以尝试更新数据");
		}

	}

	// 扫码枪添加商品 转换
	protected void addItems() {
		for (int i = 0; i < Newitems.size(); i++) {
			DefDocItemXS defdocitemxs = Newitems.get(i);
			String localString = new ServiceGoods().gds_GetGoodsWarehouses(defdocitemxs.getGoodsid(),
					defdocitemxs.isIsusebatch());
			List<RespGoodsWarehouse> goodsWarehouses;
			if (RequestHelper.isSuccess(localString)) {
				goodsWarehouses = JSONUtil.str2list(localString, RespGoodsWarehouse.class);
				for (int j = 0; j < goodsWarehouses.size(); j++) {
					if (defdocitemxs.getGoodsid().equals(goodsWarehouses.get(j).getGoodsid())
							&& defdocitemxs.getWarehouseid().equals(goodsWarehouses.get(j).getWarehouseid())) {
						RespGoodsWarehouse res = goodsWarehouses.get(j);
						defdocitemxs.setStocknum(res);
						String bigstocknum = res.getBigstocknum().length() == 0 ? "0" + defdocitemxs.getUnitname()
								: res.getBigstocknum();
						// 库存 的 设置
						defdocitemxs.goodStock = bigstocknum;
						break;
					}

				}
				// 查询库存
				ReqStrGetGoodsPrice goodsPrice = DocUtils.GetMultiGoodsPrice(doc.getCustomerid(), defdocitemxs);
				defdocitemxs.setPrice(goodsPrice.getPrice());
				items.addAll(Newitems);
				adapter.setData(items);
				Newitems.clear();
			} else {
				showError("没有获取到库存数据!请重试!");
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu paramMenu) {
		paramMenu.add(0, 0, 0, "单击显示菜单").setTitle("确定").setShowAsAction(1);
		return super.onCreateOptionsMenu(paramMenu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
		switch (paramMenuItem.getItemId()) {
		case android.R.id.home:
			// deleBm();
			setResult(RESULT_FIRST_USER, new Intent());
			finish();
			break;
		case 0:
			tv_title_start();
			break;
		}
		return true;
	}

	// public void deleBm() {
	// if (bm != null) {
	// bm.removeListener(new BarcodeListener() {
	//
	// @Override
	// public void barcodeEvent(BarcodeEvent arg0) {
	//
	// }
	// });
	// bm.dismiss();
	// bm = null;
	// }
	// }

	// 初始化 设置 库存 单位转换
	protected void setInitItem() {
		for (int i = 0; i < items.size(); i++) {
			DefDocItemXS defdocitemxs = items.get(i);
			String localString = new ServiceGoods().gds_GetGoodsWarehouses(defdocitemxs.getGoodsid(),
					defdocitemxs.isIsusebatch());
			List<RespGoodsWarehouse> goodsWarehouses;
			if (RequestHelper.isSuccess(localString)) {
				goodsWarehouses = JSONUtil.str2list(localString, RespGoodsWarehouse.class);
				for (int j = 0; j < goodsWarehouses.size(); j++) {
					if (defdocitemxs.getGoodsid().equals(goodsWarehouses.get(j).getGoodsid())
							&& defdocitemxs.getWarehouseid().equals(goodsWarehouses.get(j).getWarehouseid())) {
						RespGoodsWarehouse res = goodsWarehouses.get(j);
						defdocitemxs.setStocknum(res);
						String bigstocknum = res.getBigstocknum().length() == 0 ? "0" + defdocitemxs.getUnitname()
								: res.getBigstocknum();
						// 库存 的 设置
						defdocitemxs.goodStock = bigstocknum;
						break;
					}

				}
				// 查询库存
				ReqStrGetGoodsPrice goodsPrice = DocUtils.GetMultiGoodsPrice(doc.getCustomerid(), defdocitemxs);
				defdocitemxs.setPrice(goodsPrice.getPrice());
				adapter.setData(items);
				lv_commodity_add.setAdapter(adapter);
				lv_commodity_add.setItemsCanFocus(true);
			} else {
				showError("没有获取到库存数据!请重试!");
			}
		}
	}

	private List<DefDocItemXS> listDe;
	private ListView lv_commodity_add;
	// private BarcodeManager bm;
	// private BarcodeConfig barcodeConfig;
	private Dialog_listCheckBox dialog;
	private Scaner scaner;

	// 保存输入的值 必须有一个 大于0 的
	private void tv_title_start() {
		List<DefDocItemXS> data = adapter.getData();
		listDe = new ArrayList<DefDocItemXS>();
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).getNum() > 0.0D) {
				listDe.add(data.get(i));
			}
		}
		if (listDe.size() == 0) {
			PDH.showError("必需至少有一条商品数量大于0");
			return;
		}
		// deleBm();
		Intent intent = new Intent();
		intent.putExtra("items", JSONUtil.object2Json(listDe));
		setResult(RESULT_OK, intent);
		finish();
	}

	public long getMaxTempItemId() {
		long l1 = 0L;
		if (items.size() <= 0) {
			return l1;
		}
		for (int i = 0; i < items.size(); i++) {
			long l2 = items.get(i).getTempitemid();
			if (l2 > l1) {
				l1 = l2;
			}
		}
		return l1;
	}

	DefDocItemXS fillItem(GoodsThin paramGoodsThin, double paramDouble1, double paramDouble2, long paramLong) {
		GoodsUnitDAO localGoodsUnitDAO = new GoodsUnitDAO();
		DefDocItemXS localDefDocItemXS = new DefDocItemXS();
		localDefDocItemXS.setItemid(0L);
		localDefDocItemXS.setTempitemid(paramLong);
		localDefDocItemXS.setDocid(this.doc.getDocid());
		localDefDocItemXS.setGoodsid(paramGoodsThin.getId());
		localDefDocItemXS.setGoodsname(paramGoodsThin.getName());
		localDefDocItemXS.setBarcode(paramGoodsThin.getBarcode());
		localDefDocItemXS.setSpecification(paramGoodsThin.getSpecification());
		localDefDocItemXS.setModel(paramGoodsThin.getModel());
		localDefDocItemXS.setWarehouseid(this.doc.getWarehouseid());
		localDefDocItemXS.setWarehousename(this.doc.getWarehousename());
		GoodsUnit localGoodsUnit = null;
		if (Utils.DEFAULT_OutDocUNIT == 0) {
			localGoodsUnit = localGoodsUnitDAO.queryBaseUnit(paramGoodsThin.getId());
		} else {
			localGoodsUnit = localGoodsUnitDAO.queryBigUnit(paramGoodsThin.getId());
		}
		localDefDocItemXS.setUnitid(localGoodsUnit.getUnitid());
		localDefDocItemXS.setUnitname(localGoodsUnit.getUnitname());
		localDefDocItemXS.setNum(Utils.normalize(paramDouble1, 2));
		localDefDocItemXS.setBignum(localGoodsUnitDAO.getBigNum(localDefDocItemXS.getGoodsid(),
				localDefDocItemXS.getUnitid(), localDefDocItemXS.getNum()));
		// 价格
		localDefDocItemXS.setPrice(Utils.normalizePrice(paramDouble2));
		// 小计
		localDefDocItemXS
				.setSubtotal(Utils.normalizeSubtotal(localDefDocItemXS.getNum() * localDefDocItemXS.getPrice()));
		// 折扣率
		localDefDocItemXS.setDiscountratio(doc.getDiscountratio());
		// 折扣价格
		localDefDocItemXS
				.setDiscountprice(Utils.normalizePrice(localDefDocItemXS.getPrice() * this.doc.getDiscountratio()));
		// 折扣小计
		localDefDocItemXS.setDiscountsubtotal(
				Utils.normalizeSubtotal(localDefDocItemXS.getNum() * localDefDocItemXS.getDiscountprice()));
		if (localDefDocItemXS.getPrice() == 0.0D) {
			localDefDocItemXS.setIsgift(true);
			localDefDocItemXS.setCostprice(0.0D);
			localDefDocItemXS.setRemark("");
			localDefDocItemXS.setRversion(0L);
			localDefDocItemXS.setIsdiscount(false);
			localDefDocItemXS.setIsexhibition(false);
			localDefDocItemXS.setIspromotion(false);
			localDefDocItemXS.setParentitemid(0L);
			localDefDocItemXS.setPromotiontype(-1);
			localDefDocItemXS.setPromotiontypename(null);
			localDefDocItemXS.setOutorderdocid(0L);
			localDefDocItemXS.setOutorderdocshowid(null);
			localDefDocItemXS.setOutorderitemid(0L);
			// 是否显示批次
			localDefDocItemXS.setIsusebatch(paramGoodsThin.isIsusebatch());
			localGoodsUnit = localGoodsUnitDAO.queryBigUnit(paramGoodsThin.getId());
		}
		return localDefDocItemXS;
	}

	/**
	 * * 监听Back键按下事件,方法2: * 注意: * 返回值表示:是否能完全处理该事件 * 在此处返回false,所以会继续传播该事件. *
	 * 
	 */

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			// deleBm();
			setResult(RESULT_FIRST_USER, new Intent());
			finish();
		}
		return true;
	}

	@Override
	public void setActionBarText() {
		getActionBar().setTitle("商品添加");
	}
}