package org.eclipse.dirigible.runtime.repository.processor;

import javax.inject.Inject;

import org.eclipse.dirigible.repository.api.ICollection;
import org.eclipse.dirigible.repository.api.IRepository;
import org.eclipse.dirigible.repository.api.IResource;
import org.eclipse.dirigible.runtime.repository.json.Repository;
import org.eclipse.dirigible.runtime.repository.json.RepositoryJsonHelper;

public class RepositoryProcessor {

	@Inject
	private IRepository repository;

	public IResource getResource(String path) {
		return repository.getResource(path);
	}

	public IResource createResource(String path, byte[] content, String contentType) {
		return repository.createResource(path, content, false, contentType);
	}

	public IResource updateResource(String path, byte[] content) {
		IResource resource = repository.getResource(path);
		resource.setContent(content);
		return resource;
	}

	public void deleteResource(String path) {
		repository.removeResource(path);
	}

	public ICollection getCollection(String path) {
		return repository.getCollection(path);
	}

	public Repository renderRepository(ICollection collection) {
		return RepositoryJsonHelper.traverseRepository(collection, "", "");
	}

}
