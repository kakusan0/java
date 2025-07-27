package com.jp.login.Enum;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import java.sql.*;

public class ContentItemListTypeHandler implements TypeHandler<ContentItemList> {
    @Override
    public void setParameter(PreparedStatement ps, int i, ContentItemList parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getValue());
    }

    @Override
    public ContentItemList getResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return ContentItemList.fromValue(value);
    }

    @Override
    public ContentItemList getResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return ContentItemList.fromValue(value);
    }

    @Override
    public ContentItemList getResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return ContentItemList.fromValue(value);
    }
}
