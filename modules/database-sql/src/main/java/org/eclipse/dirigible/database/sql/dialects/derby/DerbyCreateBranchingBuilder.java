/*******************************************************************************
 * Copyright (c) 2017 SAP and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * SAP - initial API and implementation
 *******************************************************************************/

package org.eclipse.dirigible.database.sql.dialects.derby;

import org.eclipse.dirigible.database.sql.ISqlDialect;
import org.eclipse.dirigible.database.sql.builders.CreateBranchingBuilder;

public class DerbyCreateBranchingBuilder extends CreateBranchingBuilder {

	public DerbyCreateBranchingBuilder(ISqlDialect dialect) {
		super(dialect);
	}

	@Override
	public DerbyCreateViewBuilder view(String view) {
		return new DerbyCreateViewBuilder(this.getDialect(), view);
	}

}
