package org.eclipse.dirigible.core.test;

import java.io.IOException;

import org.eclipse.dirigible.commons.api.module.DirigibleModulesInstallerModule;
import org.eclipse.dirigible.commons.api.module.StaticInjector;
import org.eclipse.dirigible.commons.config.Configuration;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.persist.UnitOfWork;

/**
 * Test supporting class, enabling dependency injection
 */
@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractGuiceTest {

	protected UnitOfWork unitOfWorkMock;

	/**
	 * Dependency injection before execution of every test method
	 *
	 * @throws IOException
	 */
	@Before
	public void beforeEveryMethod() throws IOException {
		StaticInjector.setInjector(getInjector());
	}

	protected Injector getInjector() throws IOException {
		// TODO: doublecheck the logic for cleaning up the DB
		// FileUtils.forceDelete(new File("./target/derby_test_database"));
		Configuration.setSystemProperty("DIRIGIBLE_DATABASE_DERBY_ROOT_FOLDER_DEFAULT", "./target/derby_test_database");
		return Guice.createInjector(new DirigibleModulesInstallerModule(), new Module() {

			@Override
			public void configure(Binder binder) {
				bind(binder);
			}

		});
	}

	protected void bind(Binder binder) {
		setUpMocks();

		binder.bind(UnitOfWork.class).toInstance(unitOfWorkMock);
	}

	protected void setUpMocks() {
		this.unitOfWorkMock = Mockito.mock(UnitOfWork.class);
	}

}
