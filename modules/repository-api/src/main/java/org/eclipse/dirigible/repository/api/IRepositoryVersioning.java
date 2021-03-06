package org.eclipse.dirigible.repository.api;

import java.util.List;

/**
 * The interface containing versioning related methods of the repository  
 *
 */
public interface IRepositoryVersioning {

	/**
	 * Retrieve all the kept versions of a given resource
	 *
	 * @param path the location of the {@link IResource}
	 * @return a list of {@link IResourceVersion} instances
	 * @throws RepositoryVersioningException
	 */
	public List<IResourceVersion> getResourceVersions(String path) throws RepositoryVersioningException;

	/**
	 * Retrieve a particular version of a given resource
	 *
	 * @param path the location of the {@link IResource}
	 * @param version the exact version
	 * @return a {@link IResourceVersion} instance
	 * @throws RepositoryVersioningException
	 */
	public IResourceVersion getResourceVersion(String path, int version) throws RepositoryVersioningException;

}
