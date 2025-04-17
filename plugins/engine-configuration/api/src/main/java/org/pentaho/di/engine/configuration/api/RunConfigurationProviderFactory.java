package org.pentaho.di.engine.configuration.api;

public interface RunConfigurationProviderFactory {
  RunConfigurationProvider getProvider( CheckedMetaStoreSupplier metaStoreSupplier );
}
