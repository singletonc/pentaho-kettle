package org.pentaho.di.engine.configuration.api;

import java.util.List;

public interface RunConfigurationProviderFactoryManager {
  List<RunConfigurationProvider> generateProviders( CheckedMetaStoreSupplier checkedMetaStoreSupplier );
}
