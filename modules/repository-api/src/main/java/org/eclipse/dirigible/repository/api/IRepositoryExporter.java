package org.eclipse.dirigible.repository.api;

import java.util.List;

/**
 * The interface with the export related methods of the repository 
 *
 */
public interface IRepositoryExporter {
	
	/**
	 * Export all the content under the given path(s) with the target repository
	 * instance Include the last segment of the relative roots during the
	 * archiving
	 *
	 * @param relativeRoots the list of relative roots
	 * @return the zip content
	 * @throws RepositoryExportException in case the export cannot be performed
	 */
	public byte[] exportZip(List<String> relativeRoots) throws RepositoryExportException;

	/**
	 * Export all the content under the given path with the target repository
	 * instance Include or NOT the last segment of the relative root during the
	 * archiving
	 *
	 * @param relativeRoot
	 *            single root
	 * @param inclusive
	 *            whether to include the last segment of the root or to pack its
	 *            content directly in the archive
	 * @return the zip content
	 * @throws RepositoryExportException in case the export cannot be performed
	 */
	public byte[] exportZip(String relativeRoot, boolean inclusive) throws RepositoryExportException;


}
