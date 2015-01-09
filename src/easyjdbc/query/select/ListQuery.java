package easyjdbc.query.select;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import easyjdbc.annotation.Table;
import easyjdbc.column.list.ColumnList;
import easyjdbc.column.list.SelectList;
import easyjdbc.query.EasyQuery;

public class ListQuery<T> extends EasyQuery {

	private List<String> whereClauses = new ArrayList<String>();
	private String order = "";
	private String limit = "";
	private int pageSize;

	public ListQuery(Class<T> cLass, String whereClause, Object... keys) {
		list = new SelectList(cLass);
		Table table = cLass.getAnnotation(Table.class);
		this.pageSize = table.pageSize();
		whereClauses.add(whereClause);
		for (int i = 0; i < keys.length; i++)
			parameters.add(keys[i]);
	}

	public ListQuery(Class<T> cLass) {
		list = new SelectList(cLass);
		Table table = cLass.getAnnotation(Table.class);
		if (!table.defaultCondition().equals(""))
			whereClauses.add(table.defaultCondition());
	}

	@SuppressWarnings("unchecked")
	public List<T> execute(Connection conn) {
		setSql();
		System.out.println(sql);
		List<T> result = new ArrayList<T>();
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sql);
			if (parameters != null)
				for (int j = 0; j < parameters.size(); j++) {
					pstmt.setObject(j + 1, parameters.get(j));
				}
			ResultSet rs = pstmt.executeQuery();
			Object instance = null;
			try {
				while (rs.next()) {
					instance = list.objFromResultSet(rs);
					result.add((T) instance);
				}
			} catch (Exception e) {
				System.out.println(sql);
				e.printStackTrace();
			} finally {
				if (pstmt != null)
					try {
						pstmt.close();
					} catch (SQLException sqle) {
					}
				if (rs != null)
					try {
						rs.close();
					} catch (SQLException sqle) {
					}
			}
			return result;
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	private void setSql() {
		sql = "select " + list.getJoinedName(ColumnList.ALL, ",", true) + " from " + list.getTableName();
		if (whereClauses.size() != 0)
			setWhere();
		sql += order;
		sql += limit;
	}

	private void setWhere() {
		sql += WHERE;
		for (int i = 0; i < whereClauses.size(); i++) {
			sql += whereClauses.get(i) + " ";
		}
	}

	public void setOrder(String columnName, boolean asc) {
		order = " order by " + columnName + " ";
		if (!asc)
			order += "desc ";
	}

	public void setPage(int pageIndex) {
		limit = " limit " + (pageSize * (pageIndex - 1)) + COMMA + pageSize + " ";
	}

	public void setPage(int pageIndex, int pageSize) {
		limit = " limit " + (pageSize * (pageIndex - 1)) + COMMA + pageSize + " ";
	}

}
