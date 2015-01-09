package easyjdbc.query.select;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import easyjdbc.columnset.ColumnList;
import easyjdbc.columnset.PassedParameters;
import easyjdbc.query.EasyQuery;

public class SelectQuery<T> extends EasyQuery {

	public SelectQuery(Class<T> cLass, Object... primaryKey) {
		list = new PassedParameters(cLass,primaryKey);
		sql = "select " + list.getJoinedName(ColumnList.ALL, ",", true) + " from " + list.getTableName() + WHERE
				+ list.getNameAndValue(ColumnList.KEY, AND, true);

		for (int i = 0; i < primaryKey.length; i++) {
			parameters.add(primaryKey[i]);
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
					instance = list.getObject(rs);
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
