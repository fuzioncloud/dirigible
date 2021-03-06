/*******************************************************************************
 * Copyright (c) 2017 SAP and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * SAP - initial API and implementation
 *******************************************************************************/

package org.eclipse.dirigible.database.persistence.processors.table;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.eclipse.dirigible.database.persistence.PersistenceException;
import org.eclipse.dirigible.database.persistence.model.PersistenceTableColumnModel;
import org.eclipse.dirigible.database.persistence.model.PersistenceTableModel;
import org.eclipse.dirigible.database.persistence.processors.AbstractPersistenceProcessor;
import org.eclipse.dirigible.database.sql.DataType;
import org.eclipse.dirigible.database.sql.SqlFactory;
import org.eclipse.dirigible.database.sql.builders.table.CreateTableBuilder;

public class PersistenceCreateTableProcessor extends AbstractPersistenceProcessor {

	@Override
	protected String generateScript(Connection connection, PersistenceTableModel tableModel) {
		CreateTableBuilder createTableBuilder = SqlFactory.getNative(SqlFactory.deriveDialect(connection)).create().table(tableModel.getTableName());
		for (PersistenceTableColumnModel columnModel : tableModel.getColumns()) {
			DataType dataType = DataType.valueOf(columnModel.getType());
			switch (dataType) {
				case VARCHAR:
					createTableBuilder.columnVarchar(columnModel.getName(), columnModel.getLength(), columnModel.isPrimaryKey(),
							columnModel.isNullable(), columnModel.isUnique());
					break;
				case CHAR:
					createTableBuilder.columnVarchar(columnModel.getName(), columnModel.getLength(), columnModel.isPrimaryKey(),
							columnModel.isNullable(), columnModel.isUnique());
					break;
				case DATE:
					createTableBuilder.columnDate(columnModel.getName(), columnModel.isPrimaryKey(), columnModel.isNullable(),
							columnModel.isUnique());
					break;
				case TIME:
					createTableBuilder.columnTime(columnModel.getName(), columnModel.isPrimaryKey(), columnModel.isNullable(),
							columnModel.isUnique());
					break;
				case TIMESTAMP:
					createTableBuilder.columnTimestamp(columnModel.getName(), columnModel.isPrimaryKey(), columnModel.isNullable(),
							columnModel.isUnique());
					break;
				case INTEGER:
					createTableBuilder.columnInteger(columnModel.getName(), columnModel.isPrimaryKey(), columnModel.isNullable(),
							columnModel.isUnique());
					break;
				case TINYINT:
					createTableBuilder.columnInteger(columnModel.getName(), columnModel.isPrimaryKey(), columnModel.isNullable(),
							columnModel.isUnique());
					break;
				case BIGINT:
					createTableBuilder.columnBigint(columnModel.getName(), columnModel.isPrimaryKey(), columnModel.isNullable(),
							columnModel.isUnique());
					break;
				case REAL:
					createTableBuilder.columnReal(columnModel.getName(), columnModel.isPrimaryKey(), columnModel.isNullable(),
							columnModel.isUnique());
					break;
				case DOUBLE:
					createTableBuilder.columnDouble(columnModel.getName(), columnModel.isPrimaryKey(), columnModel.isNullable(),
							columnModel.isUnique());
					break;
				case BOOLEAN:
					createTableBuilder.columnBoolean(columnModel.getName(), columnModel.isPrimaryKey(), columnModel.isNullable(),
							columnModel.isUnique());
					break;
				case BLOB:
					createTableBuilder.columnBlob(columnModel.getName(), columnModel.isNullable());
					break;
				case DECIMAL:
					createTableBuilder.columnDecimal(columnModel.getName(), columnModel.isPrimaryKey(), columnModel.isNullable(),
							columnModel.isUnique(), columnModel.getPrecision(), columnModel.getScale());
					break;
			}
		}
		String sql = createTableBuilder.toString();
		return sql;
	}

	public int create(Connection connection, PersistenceTableModel tableModel) throws PersistenceException {
		int result = 0;
		String sql = null;
		PreparedStatement preparedStatement = null;
		try {
			sql = generateScript(connection, tableModel);
			preparedStatement = openPreparedStatement(connection, sql);
			result = preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new PersistenceException(sql, e);
		} finally {
			closePreparedStatement(preparedStatement);
		}
		return result;
	}

}
