/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2029-07-20
 ******************************************************************************/


package org.pentaho.di.core.compress.gzip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.pentaho.di.core.compress.CompressionPluginType;
import org.pentaho.di.core.compress.CompressionProviderFactory;
import org.pentaho.di.core.plugins.PluginRegistry;
import org.pentaho.di.junit.rules.RestorePDIEngineEnvironment;

public class GZIPCompressionProviderTest {
  @ClassRule public static RestorePDIEngineEnvironment env = new RestorePDIEngineEnvironment();

  public static final String PROVIDER_NAME = "GZip";

  public CompressionProviderFactory factory = null;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    PluginRegistry.addPluginType( CompressionPluginType.getInstance() );
    PluginRegistry.init( false );
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception {
    factory = CompressionProviderFactory.getInstance();
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testCtor() {
    GZIPCompressionProvider ncp = new GZIPCompressionProvider();
    assertNotNull( ncp );
  }

  @Test
  public void testGetName() {
    GZIPCompressionProvider provider = (GZIPCompressionProvider) factory.getCompressionProviderByName( PROVIDER_NAME );
    assertNotNull( provider );
    assertEquals( PROVIDER_NAME, provider.getName() );
  }

  @Test
  public void testGetProviderAttributes() {
    GZIPCompressionProvider provider = (GZIPCompressionProvider) factory.getCompressionProviderByName( PROVIDER_NAME );
    assertEquals( "GZIP compression", provider.getDescription() );
    assertTrue( provider.supportsInput() );
    assertTrue( provider.supportsOutput() );
    assertEquals( "gz", provider.getDefaultExtension() );
  }

  @Test
  public void testCreateInputStream() throws IOException {
    GZIPCompressionProvider provider = (GZIPCompressionProvider) factory.getCompressionProviderByName( PROVIDER_NAME );

    // Create an in-memory GZIP output stream for use by the input stream (to avoid exceptions)
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    GZIPOutputStream gos = new GZIPOutputStream( baos );
    byte[] testBytes = "Test".getBytes();
    gos.write( testBytes );
    ByteArrayInputStream in = new ByteArrayInputStream( baos.toByteArray() );

    // Test stream creation paths
    GZIPInputStream gis = new GZIPInputStream( in );
    in = new ByteArrayInputStream( baos.toByteArray() );
    GZIPCompressionInputStream ncis = provider.createInputStream( in );
    assertNotNull( ncis );
    GZIPCompressionInputStream ncis2 = provider.createInputStream( gis );
    assertNotNull( ncis2 );
  }

  @Test
  public void testCreateOutputStream() throws IOException {
    GZIPCompressionProvider provider = (GZIPCompressionProvider) factory.getCompressionProviderByName( PROVIDER_NAME );
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    GZIPOutputStream gos = new GZIPOutputStream( out );
    GZIPCompressionOutputStream outStream = new GZIPCompressionOutputStream( out, provider );
    assertNotNull( outStream );
    out = new ByteArrayOutputStream();
    GZIPCompressionOutputStream ncis = provider.createOutputStream( out );
    assertNotNull( ncis );
    GZIPCompressionOutputStream ncis2 = provider.createOutputStream( gos );
    assertNotNull( ncis2 );
  }
}
