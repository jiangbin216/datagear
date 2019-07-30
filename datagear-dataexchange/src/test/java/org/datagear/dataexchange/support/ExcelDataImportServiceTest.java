/*
 * Copyright (c) 2018 datagear.tech. All Rights Reserved.
 */

package org.datagear.dataexchange.support;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.sql.Connection;
import java.util.concurrent.atomic.AtomicInteger;

import org.datagear.dataexchange.ColumnNotFoundException;
import org.datagear.dataexchange.DataExchangeException;
import org.datagear.dataexchange.DataFormat;
import org.datagear.dataexchange.DataIndex;
import org.datagear.dataexchange.DataexchangeTestSupport;
import org.datagear.dataexchange.ExceptionResolve;
import org.datagear.dataexchange.SimpleConnectionFactory;
import org.datagear.dataexchange.ValueDataImportOption;
import org.datagear.util.JdbcUtil;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * {@linkplain ExcelDataImportService}单元测试类。
 * 
 * @author datagear@163.com
 *
 */
public class ExcelDataImportServiceTest extends DataexchangeTestSupport
{
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private ExcelDataImportService excelDataImportService;

	public ExcelDataImportServiceTest()
	{
		super();
		this.excelDataImportService = new ExcelDataImportService(databaseInfoResolver);
	}

	@Test
	public void exchangeTest_xls() throws Throwable
	{
		DataFormat dataFormat = new DataFormat();

		Connection cn = getConnection();

		try
		{
			cn = getConnection();

			File excelFile = getClasspathFileForTest(
					"org/datagear/dataexchange/support/ExcelDataImportServiceTest.xls");

			ValueDataImportOption valueDataImportOption = new ValueDataImportOption(ExceptionResolve.ABORT, false,
					true);

			ExcelDataImport impt = new ExcelDataImport(new SimpleConnectionFactory(cn, false), dataFormat,
					valueDataImportOption, excelFile);

			clearTable(cn, TABLE_NAME_DATA_IMPORT);
			clearTable(cn, TABLE_NAME_DATA_EXPORT);

			this.thrown.expect(ColumnNotFoundException.class);

			this.excelDataImportService.exchange(impt);

		}
		finally
		{
			JdbcUtil.closeConnection(cn);
		}
	}

	@Test
	public void exchangeTest_xls_ignoreInexistentColumn() throws Throwable
	{
		DataFormat dataFormat = new DataFormat();

		Connection cn = getConnection();

		try
		{
			cn = getConnection();

			File excelFile = getClasspathFileForTest(
					"org/datagear/dataexchange/support/ExcelDataImportServiceTest.xls");

			final AtomicInteger successCount = new AtomicInteger(0);
			final AtomicInteger ignoreCount = new AtomicInteger(0);

			ValueDataImportOption valueDataImportOption = new ValueDataImportOption(ExceptionResolve.ABORT, true, true);

			ExcelDataImport impt = new ExcelDataImport(new SimpleConnectionFactory(cn, false), dataFormat,
					valueDataImportOption, excelFile);

			impt.setListener(new MockValueDataImportListener()
			{
				@Override
				public void onSuccess(DataIndex dataIndex)
				{
					super.onSuccess(dataIndex);
					successCount.incrementAndGet();
				}

				@Override
				public void onIgnore(DataIndex dataIndex, DataExchangeException e)
				{
					super.onIgnore(dataIndex, e);
					ignoreCount.incrementAndGet();
				}
			});

			clearTable(cn, TABLE_NAME_DATA_IMPORT);
			clearTable(cn, TABLE_NAME_DATA_EXPORT);

			this.excelDataImportService.exchange(impt);

			int count0 = getCount(cn, TABLE_NAME_DATA_IMPORT);
			int count1 = getCount(cn, TABLE_NAME_DATA_EXPORT);

			assertEquals(5, count0);
			assertEquals(6, count1);
		}
		finally
		{
			JdbcUtil.closeConnection(cn);
		}
	}

	@Test
	public void exchangeTest_xlsx_ignoreInexistentColumn() throws Throwable
	{
		DataFormat dataFormat = new DataFormat();

		Connection cn = getConnection();

		try
		{
			cn = getConnection();

			File excelFile = getClasspathFileForTest(
					"org/datagear/dataexchange/support/ExcelDataImportServiceTest.xlsx");

			final AtomicInteger successCount = new AtomicInteger(0);
			final AtomicInteger ignoreCount = new AtomicInteger(0);

			ValueDataImportOption valueDataImportOption = new ValueDataImportOption(ExceptionResolve.ABORT, true, true);

			ExcelDataImport impt = new ExcelDataImport(new SimpleConnectionFactory(cn, false), dataFormat,
					valueDataImportOption, excelFile);

			impt.setListener(new MockValueDataImportListener()
			{
				@Override
				public void onSuccess(DataIndex dataIndex)
				{
					super.onSuccess(dataIndex);
					successCount.incrementAndGet();
				}

				@Override
				public void onIgnore(DataIndex dataIndex, DataExchangeException e)
				{
					super.onIgnore(dataIndex, e);
					ignoreCount.incrementAndGet();
				}
			});

			clearTable(cn, TABLE_NAME_DATA_IMPORT);
			clearTable(cn, TABLE_NAME_DATA_EXPORT);

			this.excelDataImportService.exchange(impt);

			int count0 = getCount(cn, TABLE_NAME_DATA_IMPORT);
			int count1 = getCount(cn, TABLE_NAME_DATA_EXPORT);

			assertEquals(5, count0);
			assertEquals(6, count1);
		}
		finally
		{
			JdbcUtil.closeConnection(cn);
		}
	}

	protected File getClasspathFileForTest(String classpath)
	{
		if (!classpath.startsWith("/"))
			classpath = "/" + classpath;

		File file = new File("src/test/resources" + classpath);

		return file;
	}
}
