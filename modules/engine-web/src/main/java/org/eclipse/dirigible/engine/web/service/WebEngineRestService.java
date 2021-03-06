package org.eclipse.dirigible.engine.web.service;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.eclipse.dirigible.commons.api.service.IRestService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

/**
 * Front facing REST service serving the raw web content from the registry/public space
 */
@Singleton
@Path("/web")
@Api(value = "Core - Web Engine", authorizations = { @Authorization(value = "basicAuth", scopes = {}) })
@ApiResponses({ @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden") })
public class WebEngineRestService extends AbstractWebEngineRestService {

	@Override
	@GET
	@Path("/{path:.*}")
	@ApiOperation("Get Resource Content")
	@ApiResponses({ @ApiResponse(code = 200, message = "Get the content fo the resource", response = byte[].class),
			@ApiResponse(code = 404, message = "No such resource") })
	public Response getResource(@ApiParam(value = "Path of the Resource", required = true) @PathParam("path") String path) {
		return super.getResource(path);
	}

	@Override
	public Class<? extends IRestService> getType() {
		return WebEngineRestService.class;
	}
}
