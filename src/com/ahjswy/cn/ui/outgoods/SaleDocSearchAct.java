package com.ahjswy.cn.ui.outgoods;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.ahjswy.cn.R;
import com.ahjswy.cn.model.CustomerThin;
import com.ahjswy.cn.model.Department;
import com.ahjswy.cn.model.Warehouse;
import com.ahjswy.cn.request.ReqStrSearchDoc;
import com.ahjswy.cn.ui.BaseActivity;
import com.ahjswy.cn.ui.CustomerSearchAct;
import com.ahjswy.cn.ui.DepartmentSearchAct;
import com.ahjswy.cn.ui.DocTypeListAct;
import com.ahjswy.cn.ui.WarehouseSearchAct;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

public class SaleDocSearchAct extends BaseActivity implements OnClickListener {
	private Button btnCustomer;
	private Button btnDepartment;
	private Button btnDoctype;
	private Button btnEndtime;
	private Button btnStarttime;
	private Button btnWarehouse;
	private Calendar calendar;
	private CheckBox checkOnlyShowNoSettleUp;
	private ReqStrSearchDoc condition;
	private EditText etRemarkSummary;
	private EditText etShowID;

	@Override
	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.act_saledocsearch);
		condition = ((ReqStrSearchDoc) getIntent().getSerializableExtra("condition"));
		initView(condition);
		calendar = Calendar.getInstance();
	}

	private void initView(ReqStrSearchDoc reqstrsearchdoc) {
		btnDoctype = ((Button) findViewById(R.id.btnDoctype));
		btnDepartment = ((Button) findViewById(R.id.btnDepartment));
		btnWarehouse = ((Button) findViewById(R.id.btnWarehouse));
		btnCustomer = ((Button) findViewById(R.id.btnCustomer));
		btnStarttime = ((Button) findViewById(R.id.btnStarttime));
		btnEndtime = ((Button) findViewById(R.id.btnEndtime));
		etRemarkSummary = ((EditText) findViewById(R.id.etRemarkSummary));
		etShowID = ((EditText) findViewById(R.id.etShowID));
		checkOnlyShowNoSettleUp = ((CheckBox) findViewById(R.id.checkBox));
		btnDoctype.setOnClickListener(this);
		btnDepartment.setOnClickListener(this);
		btnWarehouse.setOnClickListener(this);
		btnCustomer.setOnClickListener(this);
		btnStarttime.setOnClickListener(dateClickListener);
		btnEndtime.setOnClickListener(dateClickListener);
		btnDoctype.setText(reqstrsearchdoc.getDoctypeName());
		btnDoctype.setTag(reqstrsearchdoc.getDoctype());
		btnDepartment.setText(reqstrsearchdoc.getDepartmentName());
		btnDepartment.setTag(reqstrsearchdoc.getDepartmentID());
		btnWarehouse.setText(reqstrsearchdoc.getWarehouseName());
		btnWarehouse.setTag(reqstrsearchdoc.getWarehouseID());
		btnCustomer.setText(reqstrsearchdoc.getCustomerName());
		btnCustomer.setTag(reqstrsearchdoc.getCustomerID());
		try {
			Date localDate1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(reqstrsearchdoc.getDateBeginTime());
			Date localDate2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(reqstrsearchdoc.getDateEndTime());
			btnStarttime.setText(
					new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(Long.valueOf(localDate1.getTime())));
			btnEndtime.setText(
					new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(Long.valueOf(localDate2.getTime())));
		} catch (Exception e) {
		}
		etRemarkSummary.setText(reqstrsearchdoc.getRemarkSummary());
		etShowID.setText(reqstrsearchdoc.getShowID());
		checkOnlyShowNoSettleUp.setChecked(reqstrsearchdoc.isOnlyShowNoSettleUp());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnDepartment:
			startActivityForResult(new Intent().setClass(this, DepartmentSearchAct.class), 5);
			break;
		case R.id.btnWarehouse:
			startActivityForResult(new Intent().setClass(this, WarehouseSearchAct.class), 6);
			break;
		case R.id.btnDoctype:
			startActivityForResult(new Intent().setClass(this, DocTypeListAct.class), 0);
			break;
		case R.id.btnCustomer:
			startActivityForResult(new Intent().setClass(this, CustomerSearchAct.class), 4);
			break;

		default:
			break;
		}
	}

	private ReqStrSearchDoc fillCondition() {
		if (btnDoctype.getTag() == null) {
			condition.setDoctype(null);
			condition.setDoctypeName("全部");
		} else {
			condition.setDoctype(btnDoctype.getTag().toString());
			condition.setDoctypeName(btnDoctype.getText().toString());
		}

		if (btnDepartment.getTag() == null) {
			condition.setDepartmentID(null);
			condition.setDepartmentName("全部");
		} else {
			condition.setDepartmentID(btnDepartment.getTag().toString());
			condition.setDepartmentName(btnDepartment.getText().toString());
		}

		if (btnWarehouse.getTag() == null) {
			condition.setWarehouseID(null);
			condition.setWarehouseName("全部");
		} else {
			condition.setWarehouseID(btnWarehouse.getTag().toString());
			condition.setWarehouseName(btnWarehouse.getText().toString());
		}

		if (btnCustomer.getTag() == null) {
			condition.setCustomerID(null);
			condition.setCustomerName("全部");
		} else {
			condition.setCustomerID(btnCustomer.getTag().toString());
			condition.setCustomerName(btnCustomer.getText().toString());
		}
		condition.setDateBeginTime(btnStarttime.getText() + " 00:00:00");
		condition.setDateEndTime(btnEndtime.getText() + " 00:00:00");
		condition.setRemarkSummary(etRemarkSummary.getText().toString());
		condition.setShowID(etShowID.getText().toString());
		condition.setOnlyShowNoSettleUp(checkOnlyShowNoSettleUp.isChecked());
		return condition;

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 0:
				btnDoctype.setText(data.getStringExtra("doctypename"));
				btnDoctype.setTag(data.getStringExtra("doctypeid"));
				break;
			case 4:
				CustomerThin localCustomerThin = (CustomerThin) data.getSerializableExtra("customer");
				btnCustomer.setText(localCustomerThin.getName());
				btnCustomer.setTag(localCustomerThin.getId());
				break;
			case 5:
				Department localDepartment = (Department) data.getSerializableExtra("department");
				btnDepartment.setText(localDepartment.getDname());
				btnDepartment.setTag(localDepartment.getDid());
				break;
			case 6:
				Warehouse localWarehouse = (Warehouse) data.getSerializableExtra("warehouse");
				btnWarehouse.setText(localWarehouse.getName());
				btnWarehouse.setTag(localWarehouse.getId());
				break;
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(0, 0, 0, "确定").setShowAsAction(2);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuitem) {
		switch (menuitem.getItemId()) {
		case android.R.id.home:
			setResult(RESULT_FIRST_USER);
			finish();
			break;
		case 0:
			fillCondition();
			Intent localIntent = new Intent();
			localIntent.putExtra("condition", condition);
			setResult(RESULT_OK, localIntent);
			finish();
			break;
		}
		return true;
	}

	private View.OnClickListener dateClickListener = new View.OnClickListener() {
		private Button btn;
		private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				SaleDocSearchAct.this.calendar.set(1, year);
				SaleDocSearchAct.this.calendar.set(2, monthOfYear);
				SaleDocSearchAct.this.calendar.set(5, dayOfMonth);
				SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
				btn.setText(localSimpleDateFormat.format(SaleDocSearchAct.this.calendar.getTime()));
			}
		};

		public void onClick(View paramAnonymousView) {
			this.btn = ((Button) paramAnonymousView);
			try {
				calendar.setTime(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).parse((String) this.btn.getText()));
				new DatePickerDialog(SaleDocSearchAct.this, this.listener, SaleDocSearchAct.this.calendar.get(1),
						SaleDocSearchAct.this.calendar.get(2), SaleDocSearchAct.this.calendar.get(5)).show();
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}
	};

	@Override
	public void setActionBarText() {
		getActionBar().setTitle("筛选");
	}

}
