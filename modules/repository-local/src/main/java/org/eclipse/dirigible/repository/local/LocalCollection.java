/*******************************************************************************
 * Copyright (c) 2015 SAP and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * SAP - initial API and implementation
 *******************************************************************************/

package org.eclipse.dirigible.repository.local;

import static java.text.MessageFormat.format;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.dirigible.commons.api.helpers.ContentTypeHelper;
import org.eclipse.dirigible.repository.api.ICollection;
import org.eclipse.dirigible.repository.api.IEntity;
import org.eclipse.dirigible.repository.api.IRepository;
import org.eclipse.dirigible.repository.api.IResource;
import org.eclipse.dirigible.repository.api.RepositoryNotFoundException;
import org.eclipse.dirigible.repository.api.RepositoryPath;
import org.eclipse.dirigible.repository.api.RepositoryReadException;
import org.eclipse.dirigible.repository.api.RepositoryWriteException;
import org.eclipse.dirigible.repository.fs.FileSystemRepository;
import org.eclipse.dirigible.repository.fs.FileSystemUtils;

/**
 * The file system based implementation of {@link ICollection}
 */
public class LocalCollection extends LocalEntity implements ICollection {

	public LocalCollection(FileSystemRepository repository, RepositoryPath path) {
		super(repository, path);
	}

	@Override
	public void create() throws RepositoryWriteException {
		final ICollection parent = getParent();
		if (parent == null) {
			throw new LocalRepositoryException("Cannot create root collection.");
		}
		parent.createCollection(getName());
	}

	@Override
	public void delete() throws RepositoryWriteException {
		final LocalFolder folder = getFolderSafe();
		try {
			folder.deleteTree();
		} catch (LocalRepositoryException ex) {
			throw new RepositoryWriteException(format("Could not delete collection {0} ", this.getName()), ex);
		}
	}

	@Override
	public void renameTo(String name) throws RepositoryWriteException {
		final LocalFolder folder = getFolderSafe();
		try {
			folder.renameFolder(RepositoryPath.normalizePath(getParent().getPath(), name));
		} catch (LocalRepositoryException ex) {
			throw new RepositoryWriteException(format("Could not rename collection {0}", this.getName()), ex);
		}
	}

	@Override
	public void moveTo(String path) throws RepositoryWriteException {
		final LocalFolder folder = getFolderSafe();
		try {
			folder.renameFolder(path);
		} catch (LocalRepositoryException ex) {
			throw new RepositoryWriteException(format("Could not move collection {0}", this.getName()), ex);
		}
	}

	@Override
	public void copyTo(String path) throws RepositoryWriteException {
		final LocalFolder folder = getFolderSafe();
		try {
			folder.copyFolder(path);
		} catch (LocalRepositoryException ex) {
			throw new RepositoryWriteException(format("Could not move collection {0}", this.getName()), ex);
		}
	}

	@Override
	public boolean exists() throws RepositoryWriteException {
		String repositoryPath = getRepositoryPath().toString();
		if (IRepository.SEPARATOR.equals(repositoryPath)) {
			return true;
		}
		String localPath = LocalWorkspaceMapper.getMappedName(getRepository(), repositoryPath);
		return FileSystemUtils.directoryExists(localPath);
	}

	@Override
	public boolean isEmpty() throws RepositoryReadException {
		return getResources().isEmpty() && getCollections().isEmpty();
	}

	@Override
	public List<ICollection> getCollections() throws RepositoryReadException {
		// return new ArrayList<ICollection>(collections.values());
		final List<String> collectionNames = getCollectionsNames();
		final List<ICollection> result = new ArrayList<ICollection>(collectionNames.size());
		for (String collectionName : collectionNames) {
			result.add(getCollection(collectionName));
		}
		return result;
	}

	@Override
	public List<String> getCollectionsNames() throws RepositoryReadException {
		final List<String> result = new ArrayList<String>();
		final LocalFolder folder = getFolderSafe();
		try {
			for (LocalObject child : folder.getChildren()) {
				if (child instanceof LocalFolder) {
					result.add(child.getName());
				}
			}
		} catch (LocalRepositoryException ex) {
			throw new RepositoryReadException(format("Could not get child collection names {0} ", this.getName()), ex);
		}
		return result;
	}

	@Override
	public ICollection createCollection(String name) throws RepositoryWriteException {
		createAncestorsAndSelfIfMissing();
		final LocalFolder folder = getFolderSafe();
		try {
			folder.createFolder(name);
		} catch (LocalRepositoryException ex) {
			throw new RepositoryWriteException(format("Could not create child collection {0}", name), ex);
		}
		return getCollection(name);
	}

	@Override
	public ICollection getCollection(String name) {
		final RepositoryPath path = getRepositoryPath().append(name);
		return new LocalCollection(getRepository(), path);
	}

	@Override
	public void removeCollection(String name) throws RepositoryWriteException {
		final ICollection collection = getCollection(name);
		collection.delete();
	}

	@Override
	public void removeCollection(ICollection childCollection) throws RepositoryWriteException {
		removeCollection(childCollection.getName());
	}

	@Override
	public List<IResource> getResources() throws RepositoryReadException {
		final List<String> resourceNames = getResourcesNames();
		final List<IResource> result = new ArrayList<IResource>(resourceNames.size());
		for (String resourceName : resourceNames) {
			result.add(getResource(resourceName));
		}
		return result;
	}

	@Override
	public List<String> getResourcesNames() throws RepositoryReadException {
		final List<String> result = new ArrayList<String>();
		final LocalFolder folder = getFolderSafe();
		try {
			for (LocalObject child : folder.getChildren()) {
				if (child instanceof LocalFile) {
					result.add(child.getName());
				}
			}
		} catch (LocalRepositoryException ex) {
			throw new RepositoryReadException(format("Could not get child resource names {0}", this.getName()), ex);
		}
		return result;
	}

	@Override
	public IResource getResource(String name) throws RepositoryReadException {
		final RepositoryPath path = getRepositoryPath().append(name);
		return new LocalResource(getRepository(), path);
	}

	@Override
	public IResource createResource(String name, byte[] content, boolean isBinary, String contentType) throws RepositoryWriteException {
		createAncestorsAndSelfIfMissing();
		final LocalFolder folder = getFolderSafe();
		try {
			folder.createFile(name, content, isBinary, contentType);
		} catch (LocalRepositoryException ex) {
			throw new RepositoryWriteException(format("Could not create child document {0}", name), ex);
		}
		return getResource(name);
	}

	@Override
	public IResource createResource(String name, byte[] content) throws RepositoryWriteException {
		String contentType = ContentTypeHelper.getContentType(ContentTypeHelper.getExtension(name));
		boolean isBinary = ContentTypeHelper.isBinary(contentType);
		return createResource(name, content, isBinary, contentType);
	}

	@Override
	public void removeResource(String name) throws RepositoryWriteException {
		final IResource resource = getResource(name);
		resource.delete();
	}

	@Override
	public void removeResource(IResource resource) throws RepositoryWriteException {
		removeResource(resource.getName());
	}

	@Override
	public List<IEntity> getChildren() throws RepositoryReadException {
		final List<IEntity> result = new ArrayList<IEntity>();
		result.addAll(getCollections());
		result.addAll(getResources());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof LocalCollection)) {
			return false;
		}
		final LocalCollection other = (LocalCollection) obj;
		return getPath().equals(other.getPath());
	}

	@Override
	public int hashCode() {
		return getPath().hashCode();
	}

	protected LocalFolder getFolder() throws RepositoryReadException {
		final LocalObject object = getLocalObject();
		if (object == null) {
			return null;
		}
		if (!(object instanceof LocalFolder)) {
			return null;
		}
		return (LocalFolder) object;
	}

	protected LocalFolder getFolderSafe() throws RepositoryNotFoundException {
		final LocalFolder folder = getFolder();
		if (folder == null) {
			throw new RepositoryNotFoundException(format("There is no collection at path ''{0}''.", getPath()));
		}
		return folder;
	}

}
