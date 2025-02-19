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


package org.pentaho.googledrive.vfs.util;

import com.google.api.client.extensions.java6.auth.oauth2.VerificationCodeReceiver;
import com.google.api.client.util.Throwables;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class CustomLocalServerReceiver implements VerificationCodeReceiver {

  private Server server;
  String code;
  String error;
  private int port;
  private final String host;
  private String url;

  public CustomLocalServerReceiver() {
    this( "localhost", -1 );
  }

  CustomLocalServerReceiver( String host, int port ) {
    this.host = host;
    this.port = port;
  }

  public void setUrl( String url ) {
    this.url = url;
  }

  public String getRedirectUri() throws IOException {
    if ( this.port == -1 ) {
      this.port = getUnusedPort();
    }

    this.server = new Server( this.port );
    Connector[] arr$ = this.server.getConnectors();
    int len$ = arr$.length;

    for ( int i$ = 0; i$ < len$; ++i$ ) {
      Connector c = arr$[i$];
      c.setHost( this.host );
    }

    this.server.setHandler( new CustomLocalServerReceiver.CallbackHandler() ); //addHandler( new CustomLocalServerReceiver.CallbackHandler() );

    try {
      this.server.start();
    } catch ( Exception var5 ) {
      Throwables.propagateIfPossible( var5 );
      throw new IOException( var5 );
    }

    return "http://" + this.host + ":" + this.port + "/Callback/success.html";
  }

  public String waitForCode() throws IOException {
    return this.code;
  }

  public void stop() throws IOException {
    if ( this.server != null ) {
      try {
        this.server.stop();
      } catch ( Exception var2 ) {
        Throwables.propagateIfPossible( var2 );
        throw new IOException( var2 );
      }
      this.server = null;
    }
  }

  public String getHost() {
    return this.host;
  }

  public int getPort() {
    return this.port;
  }

  private static int getUnusedPort() throws IOException {
    Socket s = new Socket();
    s.bind( (SocketAddress) null );

    int var1;
    try {
      var1 = s.getLocalPort();
    } finally {
      s.close();
    }
    return var1;
  }

  class CallbackHandler extends WebAppContext {

    CallbackHandler() {
      URL warUrl = this.getClass().getClassLoader().getResource( "success_page" );
      String warUrlString = warUrl.toExternalForm();
      setResourceBase( warUrlString );
      setContextPath( "/Callback" );
    }

    public void handle( String target, HttpServletRequest request, HttpServletResponse response, int dispatch )
        throws IOException, ServletException {
      if ( target.contains( "/Callback" ) ) {

        CustomLocalServerReceiver.this.error = request.getParameter( "error" );
        if ( CustomLocalServerReceiver.this.code == null ) {
          CustomLocalServerReceiver.this.code = request.getParameter( "code" );
        }
        if ( CustomLocalServerReceiver.this.url != null && CustomLocalServerReceiver.this.error != null
            && CustomLocalServerReceiver.this.error.equals( "access_denied" ) ) {
          response.sendRedirect( CustomLocalServerReceiver.this.url );
        } else {
          super.handle( target, request, response, dispatch );
        }
        ( (Request) request ).setHandled( true );
      }
    }
  }

  public static final class Builder {
    private String host = "localhost";
    private int port = -1;

    public Builder() {
    }

    public CustomLocalServerReceiver build() {
      return new CustomLocalServerReceiver( this.host, this.port );
    }

    public String getHost() {
      return this.host;
    }

    public CustomLocalServerReceiver.Builder setHost( String host ) {
      this.host = host;
      return this;
    }

    public int getPort() {
      return this.port;
    }

    public CustomLocalServerReceiver.Builder setPort( int port ) {
      this.port = port;
      return this;
    }
  }
}
