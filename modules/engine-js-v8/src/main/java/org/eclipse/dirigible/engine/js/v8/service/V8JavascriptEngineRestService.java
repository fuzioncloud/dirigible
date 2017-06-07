package org.eclipse.dirigible.engine.js.v8.service;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.dirigible.commons.api.scripting.ScriptingDependencyException;
import org.eclipse.dirigible.commons.api.service.IRestService;
import org.eclipse.dirigible.engine.js.service.AbstractJavascriptEngineRestService;
import org.eclipse.dirigible.engine.js.v8.processor.V8JavascriptEngineProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Front facing REST service serving the V8 based Javascript backend services
 *
 */
@Singleton
public class V8JavascriptEngineRestService extends AbstractJavascriptEngineRestService {
	
	private static final Logger logger = LoggerFactory.getLogger(V8JavascriptEngineRestService.class.getCanonicalName());
	
	@Inject
	private V8JavascriptEngineProcessor processor;
	
	/**
	 * @param path
	 * @param request
	 * @return resource content
	 */
	@GET
	@Path("/v8/{path:.*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getResource(@PathParam("path") String path, @Context HttpServletRequest request, @Context HttpServletResponse response) {
		try {
			executeService(processor, path, request, response);
			return Response.ok().build();
		} catch(ScriptingDependencyException e) {
			logger.error(e.getMessage(), e);
			return Response.status(Response.Status.ACCEPTED).entity(e.getMessage()).build();
		} catch(Throwable e) {
			logger.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	@Override
	public Class<? extends IRestService> getType() {
		return V8JavascriptEngineRestService.class;
	}
}
