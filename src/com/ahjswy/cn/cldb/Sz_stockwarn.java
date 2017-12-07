package com.ahjswy.cn.cldb;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ahjswy.cn.response.RespStockwarn;
import com.ahjswy.cn.utils.DocUtils;
import com.ahjswy.cn.utils.Utils;

public class Sz_stockwarn extends CloudDBBase {

	public double queryStockNum(String warehouseid, String goodsid) {
		StringBuilder stockwarnBuilder = new StringBuilder();
		stockwarnBuilder.append("select a.warehouseid,a.goodsid,stocknum from sz_stockwarn a where a.warehouseid='");
		stockwarnBuilder.append(warehouseid).append("' and ");
		stockwarnBuilder.append("a.goodsid='" + goodsid + "'");
		ResultSet query = null;
		try {
			query = executeQuery(stockwarnBuilder.toString());
			if (query == null) {
				return 0;
			}

			while (query.next()) {
				return Double.parseDouble(query.getString("stocknum"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			DocUtils.insertLog(e, null);
		} finally {
			if (query != null) {
				try {
					query.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}
		return 0;
	}

	/**
	 * 查询商品 总库存
	 * 
	 * @param goodsid
	 * @return
	 */
	public double querySumStock(String goodsid) {
		String sql = "select SUM(stocknum) as stocknum from sz_stockwarn a where a.isavailable='1' and a.goodsid='"
				+ goodsid + "'";
		ResultSet query = null;
		try {
			query = executeQuery(sql);
			if (query == null) {
				return 0;
			}
			while (query.next()) {
				return query.getDouble("stocknum");
			}
		} catch (Exception e) {
			e.printStackTrace();
			DocUtils.insertLog(e, null);
		} finally {
			try {
				if (query != null) {
					query.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return 0;
	}

	public List<RespStockwarn> queryStockwarnAll(String goodsid) {
		String sql = "select a.warehouseid,a.goodsid,stocknum from sz_stockwarn a where a.isavailable='1' and a.goodsid='"
				+ goodsid + "'";
		ResultSet query = null;
		try {
			query = executeQuery(sql);
			if (query == null) {
				return null;
			}
			List<RespStockwarn> listStockwarn = new ArrayList<RespStockwarn>();
			while (query.next()) {
				RespStockwarn stockwarn = new RespStockwarn();
				stockwarn.warehouseid = query.getString("warehouseid");
				stockwarn.goodsid = query.getString("goodsid");
				stockwarn.stocknum = query.getDouble("stocknum");
				listStockwarn.add(stockwarn);
			}
			return listStockwarn;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (query != null) {
					query.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 商品 查询成本价格
	 * 
	 * @param warehouseid
	 * @param goodsid
	 * @param unitid
	 * @return
	 */
	public double queryGoodsCostprice(String warehouseid, String goodsid, String unitid) {
		ResultSet rs = null;
		try {
			Connection conn = getConnection();
			String sql = "{call sp_getgoodscostprice(?,?,?)}";
			CallableStatement statement = conn.prepareCall(sql);
			statement.setString(1, warehouseid);
			statement.setString(2, goodsid);
			statement.setString(3, unitid);
			statement.execute();
			rs = statement.getResultSet();
			while (rs.next()) {
				return Utils.getDouble(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return 0;
	}

}
