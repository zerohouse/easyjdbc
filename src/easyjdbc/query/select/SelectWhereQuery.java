package easyjdbc.query.select;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import easyjdbc.annotation.Table;
import easyjdbc.query.EasyQuery;
import easyjdbc.query.support.DBColumn;

public class SelectWhereQuery<T> extends EasyQuery {

	String tableName;
	Class<T> type;

	public SelectWhereQuery(Class<T> cLass, String WhereClause, Object... keys) {
		this.type = cLass;
		setByType(type, DBColumn.PHASE_SELECT);
		Table table = type.getAnnotation(Table.class);
		this.tableName = table.value();
		sql = "select * from " + tableName + WHERE + WhereClause;

		for (int i = 0; i < keys.length; i++) {
			parameters.add(keys[i]);
		}
	}

	@SuppressWarnings("unchecked")
	public T execute(Connection conn) {
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
				if (rs.next()) {
					instance = type.getConstructor().newInstance();
					for (int i = 0; i < columns.size(); i++) {
						DBColumn column = columns.get(i);
						column.setObjectField(instance, rs.getObject(column.getColumnName()));
					}
					for (int i = 0; i < keys.size(); i++) {
						DBColumn column = keys.get(i);
						column.setObjectField(instance, rs.getObject(column.getColumnName()));
					}
				}
			} catch (Exception e) {
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
			return (T) instance;
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		return null;
	}

}
