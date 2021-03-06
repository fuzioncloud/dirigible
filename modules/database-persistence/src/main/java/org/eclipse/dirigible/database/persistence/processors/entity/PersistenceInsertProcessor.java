/*******************************************************************************
 * Copyright (c) 2017 SAP and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * SAP - initial API and implementation
 *******************************************************************************/

package org.eclipse.dirigible.database.persistence.processors.entity;

import static java.text.MessageFormat.format;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.persistence.GenerationType;

import org.eclipse.dirigible.database.persistence.PersistenceException;
import org.eclipse.dirigible.database.persistence.model.PersistenceTableColumnModel;
import org.eclipse.dirigible.database.persistence.model.PersistenceTableModel;
import org.eclipse.dirigible.database.persistence.processors.AbstractPersistenceProcessor;
import org.eclipse.dirigible.database.persistence.processors.identity.PersistenceNextValueIdentityProcessor;
import org.eclipse.dirigible.database.persistence.processors.sequence.PersistenceNextValueSequenceProcessor;
import org.eclipse.dirigible.database.sql.SqlFactory;
import org.eclipse.dirigible.database.sql.builders.records.InsertBuilder;

public class PersistenceInsertProcessor extends AbstractPersistenceProcessor {

	@Override
	protected String generateScript(Connection connection, PersistenceTableModel tableModel) {
		InsertBuilder insertBuilder = SqlFactory.getNative(SqlFactory.deriveDialect(connection)).insert().into(tableModel.getTableName());
		for (PersistenceTableColumnModel columnModel : tableModel.getColumns()) {
			insertBuilder.column(columnModel.getName());
		}
		String sql = insertBuilder.toString();
		return sql;
	}

	public Object insert(Connection connection, PersistenceTableModel tableModel, Object pojo) throws PersistenceException {
		Object result = 0;
		String sql = null;
		PreparedStatement preparedStatement = null;
		try {
			setGeneratedValues(connection, tableModel, pojo);
			sql = generateScript(connection, tableModel);
			preparedStatement = openPreparedStatement(connection, sql);
			setValuesFromPojo(tableModel, pojo, preparedStatement);
			preparedStatement.executeUpdate();
			result = getPrimaryKeyValue(tableModel, pojo);
		} catch (Exception e) {
			throw new PersistenceException(sql, e);
		} finally {
			closePreparedStatement(preparedStatement);
		}
		return result;
	}

	private void setGeneratedValues(Connection connection, PersistenceTableModel tableModel, Object pojo)
			throws NoSuchFieldException, IllegalAccessException, SQLException {
		for (PersistenceTableColumnModel columnModel : tableModel.getColumns()) {
			if (columnModel.isPrimaryKey() && (columnModel.getGenerated() != null)) {
				long id = -1;
				if (GenerationType.SEQUENCE.name().equals(columnModel.getGenerated())) {
					PersistenceNextValueSequenceProcessor persistenceNextValueSequenceProcessor = new PersistenceNextValueSequenceProcessor();
					id = persistenceNextValueSequenceProcessor.nextval(connection, tableModel);
				} else if (GenerationType.TABLE.name().equals(columnModel.getGenerated())) {
					PersistenceNextValueIdentityProcessor persistenceNextValueIdentityProcessor = new PersistenceNextValueIdentityProcessor();
					id = persistenceNextValueIdentityProcessor.nextval(connection, tableModel);
				} else {
					throw new IllegalArgumentException(format("Generation Type: [{0}] not supported.", columnModel.getGenerated()));
				}
				setValueToPojo(pojo, id, columnModel);
			}
		}
	}

	private Object getPrimaryKeyValue(PersistenceTableModel tableModel, Object pojo)
			throws NoSuchFieldException, IllegalAccessException, SQLException {
		for (PersistenceTableColumnModel columnModel : tableModel.getColumns()) {
			if (columnModel.isPrimaryKey()) {
				return getValueFromPojo(pojo, columnModel);
			}
		}
		return null;
	}

}
