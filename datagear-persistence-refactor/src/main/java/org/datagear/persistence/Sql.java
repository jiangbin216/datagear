/*
 * Copyright 2018 datagear.tech. All Rights Reserved.
 */

package org.datagear.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * SQL构建器。
 * 
 * @author datagear@163.com
 *
 */
public class Sql implements Serializable
{
	private static final long serialVersionUID = 1L;

	/** sql语句 */
	private StringBuilder sql = new StringBuilder();

	/** 参数 */
	private List<SqlParamValue> paramValues = new ArrayList<SqlParamValue>();

	private String delimiter = null;

	private boolean isDelimiterElementAdded = false;

	public Sql()
	{
		super();
	}

	public Sql(String sql)
	{
		super();

		if (sql != null && !sql.isEmpty())
			this.sql.append(sql);
	}

	/**
	 * 获取SQL语句。
	 * 
	 * @return
	 */
	public String getSqlString()
	{
		return this.sql.toString();
	}

	/**
	 * 获取SQL语句。
	 * 
	 * @return
	 */
	public StringBuilder getSql()
	{
		return sql;
	}

	/**
	 * 获取{@linkplain SqlParamValue}列表。
	 * 
	 * @return
	 */
	public List<SqlParamValue> getParamValues()
	{
		return this.paramValues;
	}

	/**
	 * 获取SQL语句的长度。
	 * 
	 * @return
	 */
	public int sqlLength()
	{
		return this.sql.length();
	}

	/**
	 * 是否为空。
	 * 
	 * @return
	 */
	public boolean isEmpty()
	{
		return (this.sql.length() == 0);
	}

	/**
	 * 追加SQL语句。
	 * 
	 * @param sql
	 */
	public Sql sql(String sql)
	{
		resetDelimiterStatus();

		this.sql.append(sql);

		return this;
	}

	/**
	 * 追加SQL语句和参数。
	 * 
	 * @param sql
	 * @param paramValues
	 * @return
	 */
	public Sql sql(String sql, SqlParamValue... paramValues)
	{
		resetDelimiterStatus();

		this.sql.append(sql);

		for (SqlParamValue p : paramValues)
			this.paramValues.add(p);

		return this;
	}

	/**
	 * 追加SQL语句、参数、参数类型。
	 * 
	 * @param sql
	 * @param param
	 * @param type
	 * @return
	 */
	public Sql sql(String sql, Object param, int type)
	{
		resetDelimiterStatus();

		this.sql.append(sql);
		this.paramValues.add(new SqlParamValue(param, type));

		return this;
	}

	/**
	 * 追加另一个Sql对象。
	 * 
	 * @param sql
	 */
	public Sql sql(Sql sql)
	{
		resetDelimiterStatus();

		this.sql.append(sql.getSqlString());
		this.paramValues.addAll(sql.getParamValues());

		return this;
	}

	/**
	 * 追加参数。
	 * 
	 * @param paramValues
	 * @return
	 */
	public Sql param(SqlParamValue... paramValues)
	{
		for (SqlParamValue p : paramValues)
			this.paramValues.add(p);

		return this;
	}

	/**
	 * 追加参数。
	 * 
	 * @param paramValue
	 * @return
	 */
	public Sql param(List<? extends SqlParamValue> paramValues)
	{
		this.paramValues.addAll(paramValues);

		return this;
	}

	/**
	 * 追加参数。
	 * 
	 * @param value
	 * @param type
	 * @return
	 */
	public Sql param(Object value, int type)
	{
		this.paramValues.add(new SqlParamValue(value, type));

		return this;
	}

	/**
	 * 清除参数。
	 */
	public void clearParam()
	{
		this.paramValues.clear();
	}

	/**
	 * 定义SQL分隔符。
	 * <p>
	 * {@linkplain #sqld(String)}、{@linkplain #sqld(String, int)}、
	 * {@linkplain #sqld(String[])}、{@linkplain #sqldSuffix(String[], String)}、
	 * {@linkplain #sqldPrefix(String[], String)}
	 * 方法使用此分隔符追加SQL，连续调用这些方法不会重复添加多余的分隔符，直到再次调用此方法、或者调用任何一个 {@code sql(...)} 方法。
	 * </p>
	 * 
	 * @param delimiter
	 * @return
	 */
	public Sql delimit(String delimiter)
	{
		resetDelimiterStatus();
		this.delimiter = delimiter;

		return this;
	}

	/**
	 * 追加SQL分隔元素。
	 * 
	 * @param elements
	 * @return
	 */
	public Sql sqld(String element)
	{
		if (this.isDelimiterElementAdded)
			this.sql.append(this.delimiter);
		else
			this.isDelimiterElementAdded = true;

		this.sql.append(element);

		return this;
	}

	/**
	 * 追加SQL分隔元素。
	 * 
	 * @param elements
	 * @return
	 */
	public Sql sqld(String[] elements)
	{
		if (elements == null || elements.length == 0)
			return this;

		if (this.isDelimiterElementAdded)
			this.sql.append(this.delimiter);
		else
			this.isDelimiterElementAdded = true;

		for (int i = 0; i < elements.length; i++)
		{
			if (i > 0)
				this.sql.append(this.delimiter);

			this.sql.append(elements[i]);
		}

		return this;
	}

	/**
	 * 重复追加SQL分隔元素。
	 * 
	 * @param element
	 * @param count
	 *            追加次数
	 * @return
	 */
	public Sql sqld(String element, int count)
	{
		if (count <= 0)
			return this;

		if (this.isDelimiterElementAdded)
			this.sql.append(this.delimiter);
		else
			this.isDelimiterElementAdded = true;

		for (int i = 0; i < count; i++)
		{
			if (i > 0)
				this.sql.append(this.delimiter);

			this.sql.append(element);
		}

		return this;
	}

	/**
	 * 追加SQL分隔元素。
	 * 
	 * @param element
	 * @return
	 */
	public Sql sqld(Sql element)
	{
		if (this.isDelimiterElementAdded)
			this.sql.append(this.delimiter);
		else
			this.isDelimiterElementAdded = true;

		this.sql.append(element.sql);
		this.paramValues.addAll(element.getParamValues());

		return this;
	}

	/**
	 * 追加SQL分隔元素。
	 * 
	 * @param elements
	 * @return
	 */
	public Sql sqld(Sql[] elements)
	{
		if (elements == null || elements.length == 0)
			return this;

		if (this.isDelimiterElementAdded)
			this.sql.append(this.delimiter);
		else
			this.isDelimiterElementAdded = true;

		for (int i = 0; i < elements.length; i++)
		{
			if (i > 0)
				this.sql.append(this.delimiter);

			this.sql.append(elements[i].sql);
			this.paramValues.addAll(elements[i].getParamValues());
		}

		return this;
	}

	/**
	 * 追加SQL分隔元素，并为元素追加{@code suffix}后缀。
	 * 
	 * @param element
	 * @param suffix
	 * @return
	 */
	public Sql sqldSuffix(String element, String suffix)
	{
		if (this.isDelimiterElementAdded)
			this.sql.append(this.delimiter);
		else
			this.isDelimiterElementAdded = true;

		this.sql.append(element).append(suffix);

		return this;
	}

	/**
	 * 追加SQL分隔元素，并为每一个元素追加{@code suffix}后缀。
	 * 
	 * @param elements
	 * @param suffix
	 * @return
	 */
	public Sql sqldSuffix(String[] elements, String suffix)
	{
		if (elements == null || elements.length == 0)
			return this;

		if (this.isDelimiterElementAdded)
			this.sql.append(this.delimiter);
		else
			this.isDelimiterElementAdded = true;

		for (int i = 0; i < elements.length; i++)
		{
			if (i > 0)
				this.sql.append(this.delimiter);

			this.sql.append(elements[i]).append(suffix);
		}

		return this;
	}

	/**
	 * 追加SQL分隔元素，并为元素追加{@code prefix}前缀。
	 * 
	 * @param element
	 * @param prefix
	 * @return
	 */
	public Sql sqldPrefix(String element, String prefix)
	{
		if (this.isDelimiterElementAdded)
			this.sql.append(this.delimiter);
		else
			this.isDelimiterElementAdded = true;

		this.sql.append(prefix).append(element);

		return this;
	}

	/**
	 * 追加SQL分隔元素，并为每一个元素追加{@code prefix}前缀。
	 * 
	 * @param elements
	 * @param prefix
	 * @return
	 */
	public Sql sqldPrefix(String[] elements, String prefix)
	{
		if (elements == null || elements.length == 0)
			return this;

		if (this.isDelimiterElementAdded)
			this.sql.append(this.delimiter);
		else
			this.isDelimiterElementAdded = true;

		for (int i = 0; i < elements.length; i++)
		{
			if (i > 0)
				this.sql.append(this.delimiter);

			this.sql.append(prefix).append(elements[i]);
		}

		return this;
	}

	protected void setSql(StringBuilder sql)
	{
		this.sql = sql;
	}

	protected void setParamValues(List<SqlParamValue> paramValues)
	{
		this.paramValues = paramValues;
	}

	/**
	 * 重置分隔状态。
	 */
	protected void resetDelimiterStatus()
	{
		if (this.isDelimiterElementAdded == true)
			this.isDelimiterElementAdded = false;
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + " [sql=" + sql + ", paramValues=" + paramValues + "]";
	}

	/**
	 * 创建一个新的SQL构建器。
	 * 
	 * @return
	 */
	public static Sql valueOf()
	{
		return new Sql();
	}

	/**
	 * 创建一个新的SQL构建器。
	 * 
	 * @param sql
	 * @return
	 */
	public static Sql valueOf(String sql)
	{
		return new Sql(sql);
	}

	/**
	 * 指定{@linkplain Sql}是否为{@code null}、{@linkplain #isEmpty()}。
	 * 
	 * @param sql
	 * @return
	 */
	public static boolean isEmpty(Sql sql)
	{
		return (sql == null || sql.isEmpty());
	}
}