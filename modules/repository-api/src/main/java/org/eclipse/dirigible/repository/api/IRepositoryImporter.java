package org.eclipse.dirigible.repository.api;

import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * The interface with the import methods for the repository 
 *
 */
public interface IRepositoryImporter {
	
	/**
	 * Imports content from zip file to the repository, based on the relative
	 * root
	 *
	 * @param zipInputStream the input stream
	 * @param relativeRoot the relative root
	 * @throws RepositoryImportException in case the zip cannot be imported
	 */
	public void importZip(ZipInputStream zipInputStream, String relativeRoot) throws RepositoryImportException;

	/**
	 * Imports content from zip file to the repository, based on the relative
	 * root. Overrides the previous content depending on the override parameter.
	 *
	 * @param zipInputStream the input stream
	 * @param relativeRoot the relative root
	 * @param override whether to override existing
	 * @throws RepositoryImportException  in case the zip cannot be imported
	 */
	public void importZip(ZipInputStream zipInputStream, String relativeRoot, boolean override) throws RepositoryImportException;

	/**
	 * Imports content from zip file to the repository, based on the relative
	 * root. Overrides the previous content depending on the override parameter.
	 * Excludes the name of the root folder, during the import, based on the excludeRootFolderName parameter.
	 *
	 * @param zipInputStream the input stream
	 * @param relativeRoot the relative root
	 * @param override whether to override existing
	 * @param excludeRootFolderName whether to exclude the root folder name
	 * @throws RepositoryImportException in case the zip cannot be imported
	 */
	public void importZip(ZipInputStream zipInputStream, String relativeRoot, boolean override, boolean excludeRootFolderName) throws RepositoryImportException;

	/**
	 * Imports content from zip file to the repository, based on the relative
	 * root
	 *
	 * @param data
	 *            the zip file as byte array
	 * @param relativeRoot the relative root
	 * @throws RepositoryImportException in case the zip cannot be imported
	 */
	public void importZip(byte[] data, String relativeRoot) throws RepositoryImportException;

	/**
	 * Imports content from zip file to the repository, based on the relative
	 * root. Overrides the previous content depending on the override parameter.
	 *
	 * @param data
	 *            the zip file as byte array
	 * @param relativeRoot the relative root
	 * @param override whether to override existing
	 * @throws RepositoryImportException in case the zip cannot be imported
	 */
	public void importZip(byte[] data, String relativeRoot, boolean override) throws RepositoryImportException;

	/**
	 * Imports content from zip file to the repository, based on the relative
	 * root. Overrides the previous content depending on the override parameter.
	 * Excludes the name of the root folder, during the import, based on the excludeRootFolderName parameter.
	 *
	 * @param data
	 *            the zip file as byte array
	 * @param relativeRoot the relative root
	 * @param override whether to override existing
	 * @param filter a filter
	 * @param excludeRootFolderName
	 * @throws RepositoryImportException in case the zip cannot be imported
	 */
	public void importZip(byte[] data, String relativeRoot, boolean override, boolean excludeRootFolderName, Map<String, String> filter)
			throws RepositoryImportException;


}
