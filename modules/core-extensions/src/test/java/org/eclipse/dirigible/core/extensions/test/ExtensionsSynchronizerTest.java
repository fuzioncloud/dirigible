package org.eclipse.dirigible.core.extensions.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import javax.inject.Inject;

import org.eclipse.dirigible.core.extensions.api.ExtensionsException;
import org.eclipse.dirigible.core.extensions.api.IExtensionsCoreService;
import org.eclipse.dirigible.core.extensions.definition.DataStructureTableModel;
import org.eclipse.dirigible.core.extensions.service.ExtensionsCoreService;
import org.eclipse.dirigible.core.extensions.synchronizer.ExtensionsSynchronizer;
import org.eclipse.dirigible.core.test.AbstractGuiceTest;
import org.eclipse.dirigible.repository.api.IRepository;
import org.eclipse.dirigible.repository.api.IRepositoryStructure;
import org.junit.Before;
import org.junit.Test;

public class ExtensionsSynchronizerTest extends AbstractGuiceTest {

	@Inject
	private IExtensionsCoreService extensionsCoreService;

	@Inject
	private ExtensionsSynchronizer extensionsPublisher;

	@Inject
	private IRepository repository;

	@Before
	public void setUp() throws Exception {
		this.extensionsCoreService = getInjector().getInstance(ExtensionsCoreService.class);
		this.extensionsPublisher = getInjector().getInstance(ExtensionsSynchronizer.class);
		this.repository = getInjector().getInstance(IRepository.class);
	}

	@Test
	public void createExtensionPointTest() throws ExtensionsException, IOException {
		extensionsPublisher.registerPredeliveredExtensionPoint("/control/control.extensionpoint");
		extensionsPublisher.registerPredeliveredExtension("/control/control.extension");

		DataStructureTableModel extensionPointDefinitionCustom = new DataStructureTableModel();
		extensionPointDefinitionCustom.setLocation("/custom/custom.extensionpoint");
		extensionPointDefinitionCustom.setName("/custom/custom");
		extensionPointDefinitionCustom.setDescription("Test");
		extensionPointDefinitionCustom.setCreatedAt(new Timestamp(new Date().getTime()));
		extensionPointDefinitionCustom.setCreatedBy("test_user");

		String json = extensionsCoreService.serializeExtensionPoint(extensionPointDefinitionCustom);
		repository.createResource(IRepositoryStructure.PATH_REGISTRY_PUBLIC + "/custom/custom.extensionpoint", json.getBytes());

		extensionsPublisher.synchronize();

		DataStructureTableModel extensionPointDefinition = extensionsCoreService.getExtensionPoint("/control/control.extensionpoint");
		assertNotNull(extensionPointDefinition);
		extensionPointDefinition = extensionsCoreService.getExtensionPoint("/custom/custom.extensionpoint");
		assertNotNull(extensionPointDefinition);

	}

	@Test
	public void cleanupExtensionPointTest() throws ExtensionsException, IOException {
		createExtensionPointTest();

		repository.removeResource(IRepositoryStructure.PATH_REGISTRY_PUBLIC + "/custom/custom.extensionpoint");

		extensionsPublisher.synchronize();

		DataStructureTableModel extensionPointDefinition = extensionsCoreService.getExtensionPoint("/custom/custom.extensionpoint");
		assertNull(extensionPointDefinition);

	}

}
