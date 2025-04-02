package org.pentaho.di.engine.configuration.impl;

import org.pentaho.di.engine.configuration.api.RunConfigurationProvider;
import org.pentaho.di.engine.configuration.impl.pentaho.DefaultRunConfigurationProviderFactory;

import java.util.ArrayList;
import java.util.List;

public class RunConfigurationProviderFactoryManager {
  private static RunConfigurationProviderFactoryManager instance;

  private List<RunConfigurationProviderFactory> factories;

  public static RunConfigurationProviderFactoryManager getInstance() {
    if ( null == instance ) {
      instance = new RunConfigurationProviderFactoryManager();
    }
    return instance;
  }

  public RunConfigurationProviderFactoryManager(){
    factories = new ArrayList<>();
    factories.add( new DefaultRunConfigurationProviderFactory() );
  }

  public void registerFactory( RunConfigurationProviderFactory factory ) {
    //TODO do we want to prevent duplicates?
    factories.add( factory );
  }

  public List<RunConfigurationProvider> getProviders( CheckedMetaStoreSupplier checkedMetaStoreSupplier ) {
    List<RunConfigurationProvider> providers = new ArrayList<>();
    factories.forEach( factory -> providers.add( factory.getProvider( checkedMetaStoreSupplier ) ) );
    return providers;
  }
}
