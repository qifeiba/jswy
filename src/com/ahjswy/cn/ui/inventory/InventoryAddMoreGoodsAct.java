package com.ahjswy.cn.ui.inventory;

import java.util.List;

import com.ahjswy.cn.R;
import com.ahjswy.cn.model.DefDocItemPD;
import com.ahjswy.cn.ui.BaseActivity;
import com.ahjswy.cn.utils.JSONUtil;
import com.ahjswy.cn.utils.PDH;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class InventoryAddMoreGoodsAct extends BaseActivity {
	private List<DefDocItemPD> items;
	private ListView listView;
	private InventoryAddMoreAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_inventory_add_more_goods);
		String stringExtra = getIntent().getStringExtra("items");
		items = JSONUtil.str2list(stringExtra, DefDocItemPD.class);
		listView = ((ListView) findViewById(R.id.listView));
		adapter = new InventoryAddMoreAdapter(this);
		this.adapter.setData(this.items);
		this.listView.setAdapter(this.adapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 0, "确定").setShowAsAction(2);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menu) {
		if (menu.getItemId() == 1) {
			PDH.show(this, new PDH.ProgressCallBack() {
				public void action() {
					Intent intent = new Intent();
					intent.putExtra("items", JSONUtil.object2Json(items));
					setResult(Activity.RESULT_OK, intent);
					finish();
				}
			});
		}
		return super.onOptionsItemSelected(menu);
	}

	public void setActionBarText() {
		setTitle("商品添加");
	}
}
