package org.pentaho.di.engine.configuration.impl.spark;

import org.pentaho.di.engine.configuration.api.RunConfigurationProvider;
import org.pentaho.di.engine.configuration.impl.CheckedMetaStoreSupplier;
import org.pentaho.di.engine.configuration.impl.RunConfigurationProviderFactory;

public class SparkRunConfigurationProviderFactory implements RunConfigurationProviderFactory {

  @Override public RunConfigurationProvider getProvider( CheckedMetaStoreSupplier metaStoreSupplier ) {
    return new SparkRunConfigurationProvider( metaStoreSupplier );
  }
}
