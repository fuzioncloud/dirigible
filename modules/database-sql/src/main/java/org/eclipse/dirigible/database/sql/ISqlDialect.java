/*******************************************************************************
 * Copyright (c) 2017 SAP and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * SAP - initial API and implementation
 *******************************************************************************/

package org.eclipse.dirigible.database.sql;

import java.sql.Connection;
import java.sql.SQLException;

import org.eclipse.dirigible.database.sql.builders.CreateBranchingBuilder;
import org.eclipse.dirigible.database.sql.builders.DropBranchingBuilder;
import org.eclipse.dirigible.database.sql.builders.records.DeleteBuilder;
import org.eclipse.dirigible.database.sql.builders.records.InsertBuilder;
import org.eclipse.dirigible.database.sql.builders.records.SelectBuilder;
import org.eclipse.dirigible.database.sql.builders.records.UpdateBuilder;
import org.eclipse.dirigible.database.sql.builders.sequence.NextValueSequenceBuilder;

public interface ISqlDialect<SELECT extends SelectBuilder, INSERT extends InsertBuilder, UPDATE extends UpdateBuilder, DELETE extends DeleteBuilder, CREATE extends CreateBranchingBuilder, DROP extends DropBranchingBuilder, NEXT extends NextValueSequenceBuilder>
		extends ISqlFactory<SELECT, INSERT, UPDATE, DELETE, CREATE, DROP, NEXT>, ISqlKeywords {

	/**
	 * Default implementation returns the direct toString() conversion. It may
	 * get overridden for specific database dialects
	 */
	public String getDataTypeName(DataType dataType);

	/**
	 * PRIMARY KEY argument for a column for the create table script Default is
	 * "PRIMARY KEY"
	 */
	public String getPrimaryKeyArgument();

	/**
	 * NOT NULL argument for a column for the create table script Default is
	 * "NOT NULL"
	 */
	public String getNotNullArgument();

	/**
	 * UNIQUE argument for a column for the create table script Default is
	 * "UNIQUE"
	 */
	public String getUniqueArgument();

	/**
	 * Check existence of a table
	 * 
	 * @param connection
	 *            the current connection
	 * @param table
	 *            the table name
	 * @return true if the table exists and false otherwise
	 */
	public boolean exists(Connection connection, String table) throws SQLException;

	/**
	 * Checks if the database is capable of schema-level filtering statements
	 * (e.g. to reduce the provisioned schemas down to those that the current
	 * user is entitled to see).
	 *
	 * @see IDialectSpecifier#getSchemaFilterScript()
	 * @return true if the feature is supported , false otherwise
	 */
	public boolean isSchemaFilterSupported();

	/**
	 * If the database supports schema filtering SQL statements (see
	 * {@link #isSchemaFilterSupported()}), this method provides the
	 * corresponding SQL statement.
	 *
	 * @return a filtering SQL statement
	 */
	public String getSchemaFilterScript();

	/**
	 * Does this database support catalogs synonymous to schemas.
	 *
	 * @return whether it is a catalog for schema
	 */
	boolean isCatalogForSchema();

	/**
	 * Gives the dialect specific name of the CURRENT_DATE function
	 * 
	 * @return the name of the function
	 */
	String functionCurrentDate();

	/**
	 * Gives the dialect specific name of the CURRENT_TIME function
	 * 
	 * @return the name of the function
	 */
	String functionCurrentTime();

	/**
	 * Gives the dialect specific name of the CURRENT_TIMESTAMP function
	 * 
	 * @return the name of the function
	 */
	String functionCurrentTimestamp();

}
