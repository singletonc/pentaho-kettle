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


package org.pentaho.di.trans.steps.rest;

import javax.ws.rs.POST;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * A simple rest service
 * 
 * @author vladimir.dolzhenko@gmail.com
 */
@Path( "/simple" )
public class SimpleRestService {
  @POST
  @Path( "/join" )
  @Produces( MediaType.TEXT_PLAIN )
  public String join( @MatrixParam( "limit" ) String limit, @QueryParam( "name" ) String name ) {
    return name + ":" + limit;
  }
}
