/*******************************************************************************
 * Copyright (c) 2015 SAP and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * SAP - initial API and implementation
 *******************************************************************************/

package org.eclipse.dirigible.repository.api;

import java.nio.charset.Charset;

/**
 * This interface represents a Repository. It allows for querying, modifying and
 * navigating through collections and resources.
 */
public interface IRepository extends IRepositoryReader, IRepositoryWriter, IRepositoryImporter, IRepositoryExporter,
			IRepositorySearch, IRepositoryVersioning {

	public static final String SEPARATOR = IRepositoryStructure.SEPARATOR;
	
	public static final Charset UTF8 = Charset.forName("UTF-8");
	
	public static final String DIRIGIBLE_REPOSITORY_PROVIDER = "DIRIGIBLE_REPOSITORY_PROVIDER"; //$NON-NLS-1$
	public static final String DIRIGIBLE_REPOSITORY_PROVIDER_LOCAL = "local"; //$NON-NLS-1$

	

}
