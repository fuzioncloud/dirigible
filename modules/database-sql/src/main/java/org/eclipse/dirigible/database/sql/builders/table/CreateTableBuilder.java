/*******************************************************************************
 * Copyright (c) 2017 SAP and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * SAP - initial API and implementation
 *******************************************************************************/

package org.eclipse.dirigible.database.sql.builders.table;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.dirigible.database.sql.DataType;
import org.eclipse.dirigible.database.sql.ISqlDialect;
import org.eclipse.dirigible.database.sql.builders.AbstractCreateSqlBuilder;

public class CreateTableBuilder extends AbstractCreateSqlBuilder {

	private String table = null;
	private List<String[]> columns = new ArrayList<String[]>();

	public CreateTableBuilder(ISqlDialect dialect, String table) {
		super(dialect);
		this.table = table;
	}

	protected String getTable() {
		return table;
	}

	protected List<String[]> getColumns() {
		return columns;
	}

	public CreateTableBuilder column(String name, DataType type, boolean isPrimaryKey, boolean isNullable, boolean isUnique, String... args) {
		String[] definition = new String[] { name, getDialect().getDataTypeName(type) };
		String[] column = null;
		if (!isNullable) {
			column = Stream.of(definition, args, new String[] { getDialect().getNotNullArgument() }).flatMap(Stream::of).toArray(String[]::new);
		} else {
			column = Stream.of(definition, args).flatMap(Stream::of).toArray(String[]::new);
		}
		if (isPrimaryKey) {
			column = Stream.of(column, new String[] { getDialect().getPrimaryKeyArgument() }).flatMap(Stream::of).toArray(String[]::new);
		}
		if (isUnique && !isPrimaryKey) {
			column = Stream.of(column, new String[] { getDialect().getUniqueArgument() }).flatMap(Stream::of).toArray(String[]::new);
		}

		this.columns.add(column);
		return this;
	}

	public CreateTableBuilder columnVarchar(String name, int length, boolean isPrimaryKey, boolean isNullable, boolean isUnique, String... args) {
		String[] definition = new String[] { OPEN + length + CLOSE };
		String[] coulmn = Stream.of(definition, args).flatMap(Stream::of).toArray(String[]::new);
		return this.column(name, DataType.VARCHAR, isPrimaryKey, isNullable, isUnique, coulmn);
	}

	public CreateTableBuilder columnChar(String name, int length, boolean isPrimaryKey, boolean isNullable, boolean isUnique, String... args) {
		String[] definition = new String[] { OPEN + length + CLOSE };
		String[] coulmn = Stream.of(definition, args).flatMap(Stream::of).toArray(String[]::new);
		return this.column(name, DataType.CHAR, isPrimaryKey, isNullable, isUnique, coulmn);
	}

	public CreateTableBuilder columnDate(String name, boolean isPrimaryKey, boolean isNullable, boolean isUnique, String... args) {
		return this.column(name, DataType.DATE, isPrimaryKey, isNullable, isUnique, args);
	}

	public CreateTableBuilder columnTime(String name, boolean isPrimaryKey, boolean isNullable, boolean isUnique, String... args) {
		return this.column(name, DataType.TIME, isPrimaryKey, isNullable, isUnique, args);
	}

	public CreateTableBuilder columnTimestamp(String name, boolean isPrimaryKey, boolean isNullable, boolean isUnique, String... args) {
		return this.column(name, DataType.TIMESTAMP, isPrimaryKey, isNullable, isUnique, args);
	}

	public CreateTableBuilder columnInteger(String name, boolean isPrimaryKey, boolean isNullable, boolean isUnique, String... args) {
		return this.column(name, DataType.INTEGER, isPrimaryKey, isNullable, isUnique, args);
	}

	public CreateTableBuilder columnTinyint(String name, boolean isPrimaryKey, boolean isNullable, boolean isUnique, String... args) {
		return this.column(name, DataType.TINYINT, isPrimaryKey, isNullable, isUnique, args);
	}

	public CreateTableBuilder columnBigint(String name, boolean isPrimaryKey, boolean isNullable, boolean isUnique, String... args) {
		return this.column(name, DataType.BIGINT, isPrimaryKey, isNullable, isUnique, args);
	}

	public CreateTableBuilder columnReal(String name, boolean isPrimaryKey, boolean isNullable, boolean isUnique, String... args) {
		return this.column(name, DataType.REAL, isPrimaryKey, isNullable, isUnique, args);
	}

	public CreateTableBuilder columnDouble(String name, boolean isPrimaryKey, boolean isNullable, boolean isUnique, String... args) {
		return this.column(name, DataType.DOUBLE, isPrimaryKey, isNullable, isUnique, args);
	}

	public CreateTableBuilder columnBoolean(String name, boolean isPrimaryKey, boolean isNullable, boolean isUnique, String... args) {
		return this.column(name, DataType.BOOLEAN, isPrimaryKey, isNullable, isUnique, args);
	}

	public CreateTableBuilder columnBlob(String name, boolean isNullable, String... args) {
		return this.column(name, DataType.BLOB, false, isNullable, false, args);
	}

	public CreateTableBuilder columnDecimal(String name, boolean isPrimaryKey, boolean isNullable, boolean isUnique, int precision, int scale,
			String... args) {
		String[] definition = new String[] { OPEN + precision + "," + scale + CLOSE };
		String[] coulmn = Stream.of(definition, args).flatMap(Stream::of).toArray(String[]::new);
		return this.column(name, DataType.CHAR, isPrimaryKey, isNullable, isUnique, coulmn);
	}

	@Override
	public String generate() {

		StringBuilder sql = new StringBuilder();

		// CREATE
		generateCreate(sql);

		// TABLE
		generateTable(sql);

		// COLUMNS
		generateColumns(sql);

		return sql.toString();
	}

	protected void generateTable(StringBuilder sql) {
		sql.append(SPACE).append(KEYWORD_TABLE).append(SPACE).append(this.table);
	}

	protected void generateColumns(StringBuilder sql) {
		if (!this.columns.isEmpty()) {
			sql.append(SPACE).append(OPEN).append(traverseColumns()).append(CLOSE);
		}
	}

	protected String traverseColumns() {
		StringBuilder snippet = new StringBuilder();
		snippet.append(SPACE);
		for (String[] column : this.columns) {
			for (String arg : column) {
				snippet.append(arg).append(SPACE);
			}
			snippet.append(COMMA).append(SPACE);
		}
		return snippet.toString().substring(0, snippet.length() - 2);
	}

}
