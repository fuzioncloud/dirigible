package org.eclipse.dirigible.engine.js.v8.service;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.dirigible.commons.api.scripting.ScriptingDependencyException;
import org.eclipse.dirigible.commons.api.service.IRestService;
import org.eclipse.dirigible.engine.js.v8.processor.V8JavascriptEngineProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

/**
 * Front facing REST service serving the V8 based Javascript backend services
 */
@Singleton
@Path("/v8")
@Api(value = "JavaScript Engine - V8", authorizations = { @Authorization(value = "basicAuth", scopes = {}) })
@ApiResponses({ @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden") })
public class V8JavascriptEngineRestService implements IRestService {

	private static final Logger logger = LoggerFactory.getLogger(V8JavascriptEngineRestService.class.getCanonicalName());

	@Inject
	private V8JavascriptEngineProcessor processor;

	/**
	 * @param path
	 * @return result of the execution of the service
	 */
	@GET
	@Path("/{path:.*}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation("Execute Server Side JavaScript V8 Resource")
	@ApiResponses({ @ApiResponse(code = 200, message = "Execution Result") })
	public Response executeService(@PathParam("path") String path) {
		try {
			processor.executeService(path);
			return Response.ok().build();
		} catch (ScriptingDependencyException e) {
			logger.error(e.getMessage(), e);
			return Response.status(Response.Status.ACCEPTED).entity(e.getMessage()).build();
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	/**
	 * @param path
	 * @return result of the execution of the service
	 */
	@POST
	@Path("/{path:.*}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation("Execute Server Side JavaScript Nashorn Resource")
	@ApiResponses({ @ApiResponse(code = 200, message = "Execution Result") })
	public Response executeServicePost(@PathParam("path") String path) {
		return executeService(path);
	}

	/**
	 * @param path
	 * @return result of the execution of the service
	 */
	@PUT
	@Path("/{path:.*}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation("Execute Server Side JavaScript Nashorn Resource")
	@ApiResponses({ @ApiResponse(code = 200, message = "Execution Result") })
	public Response executeServicePut(@PathParam("path") String path) {
		return executeService(path);
	}

	/**
	 * @param path
	 * @return result of the execution of the service
	 */
	@DELETE
	@Path("/{path:.*}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation("Execute Server Side JavaScript Nashorn Resource")
	@ApiResponses({ @ApiResponse(code = 200, message = "Execution Result") })
	public Response executeServiceDelete(@PathParam("path") String path) {
		return executeService(path);
	}

	/**
	 * @param path
	 * @return result of the execution of the service
	 */
	@HEAD
	@Path("/{path:.*}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation("Execute Server Side JavaScript Nashorn Resource")
	@ApiResponses({ @ApiResponse(code = 200, message = "Execution Result") })
	public Response executeServiceHead(@PathParam("path") String path) {
		return executeService(path);
	}

	@Override
	public Class<? extends IRestService> getType() {
		return V8JavascriptEngineRestService.class;
	}
}
