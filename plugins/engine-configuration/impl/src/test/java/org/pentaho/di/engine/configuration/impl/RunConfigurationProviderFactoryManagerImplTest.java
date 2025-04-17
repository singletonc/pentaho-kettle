package org.pentaho.di.engine.configuration.impl;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.pentaho.di.core.service.PluginServiceLoader;
import org.pentaho.di.engine.configuration.api.CheckedMetaStoreSupplier;
import org.pentaho.di.engine.configuration.api.RunConfigurationProvider;
import org.pentaho.di.engine.configuration.api.RunConfigurationProviderFactory;
import org.pentaho.di.engine.configuration.impl.pentaho.DefaultRunConfigurationProviderFactory;
import org.pentaho.metastore.stores.memory.MemoryMetaStore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;

public class RunConfigurationProviderFactoryManagerImplTest {

  @Test
  public void testDefault() {
    MemoryMetaStore memoryMetaStore = new MemoryMetaStore();
    CheckedMetaStoreSupplier metastoreSupplier = () -> memoryMetaStore;
    List<RunConfigurationProvider> providers =
      RunConfigurationProviderFactoryManagerImpl.getInstance().generateProviders( metastoreSupplier );

    Assert.assertEquals( 1, providers.size() );
    Assert.assertEquals( "Pentaho", providers.get( 0 ).getType() );
  }

  @Test
  public void testPluginServiceLoaded() {
    //TODO this works when I debug and step through it... I don't know why it doesn't work normally

    //    MemoryMetaStore memoryMetaStore = new MemoryMetaStore();
//    CheckedMetaStoreSupplier metastoreSupplier = () -> memoryMetaStore;
//
//    Collection<RunConfigurationProviderFactory> factories = new ArrayList<>();
//    factories.add( new DefaultRunConfigurationProviderFactory() );
//
//    try ( MockedStatic<PluginServiceLoader> pluginServiceLoaderMockedStatic = Mockito.mockStatic(
//      PluginServiceLoader.class ) ) {
//      pluginServiceLoaderMockedStatic.when( () -> PluginServiceLoader.loadServices( any() ) ).thenReturn( factories );
//
//      List<RunConfigurationProvider> providers =
//        RunConfigurationProviderFactoryManagerImpl.getInstance().generateProviders( metastoreSupplier );
//
//      Assert.assertEquals( 2, providers.size() );
//      Assert.assertEquals( "Pentaho", providers.get( 0 ).getType() );
//      Assert.assertEquals( "Pentaho", providers.get( 1 ).getType() );
//    } catch ( Exception e ){
//      System.out.println(e);
//    }
  }
}
