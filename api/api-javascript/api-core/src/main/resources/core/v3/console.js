/*******************************************************************************
 * Copyright (c) 2017 SAP and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * SAP - initial API and implementation
 *******************************************************************************/

/* eslint-env node, dirigible */

var java = require('core/v3/java');

exports.log = function(message) {
	java.call('org.eclipse.dirigible.api.v3.core.ConsoleFacade', 'log', [message]);
};

exports.error = function(message, args) {
	java.call('org.eclipse.dirigible.api.v3.core.ConsoleFacade', 'error', [message, args]);
};

exports.info = function(message, args) {
	java.call('org.eclipse.dirigible.api.v3.core.ConsoleFacade', 'info', [message, args]);
};

exports.warn = function(message, args) {
	java.call('org.eclipse.dirigible.api.v3.core.ConsoleFacade', 'warn', [message, args]);
};

exports.debug = function(message, args) {
	java.call('org.eclipse.dirigible.api.v3.core.ConsoleFacade', 'debug', [message, args]);
};

exports.trace = function(message, args) {
	java.call('org.eclipse.dirigible.api.v3.core.ConsoleFacade', 'trace', [message, args]);
};