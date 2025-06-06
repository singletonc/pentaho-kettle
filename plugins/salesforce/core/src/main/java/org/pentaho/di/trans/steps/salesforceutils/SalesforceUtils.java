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

package org.pentaho.di.trans.steps.salesforceutils;

import java.util.regex.Pattern;

import org.pentaho.di.core.logging.LogChannelInterface;
import org.pentaho.di.i18n.BaseMessages;

/**
 * Utility class to process salesforce specific features.
 *
 * @author Tatsiana_Kasiankova
 *
 */
public class SalesforceUtils {

  private SalesforceUtils() {
  }

  private static Class<?> PKG = SalesforceUtils.class; // for i18n purposes, needed by Translator2!!

  private static final String EXTID_SEPARATOR = "/";

  private static final String CUSTOM_OBJECT_RELATIONSHIP_FIELD_SUFFIX = "_r";

  private static final String CUSTOM_OBJECT_SUFFIX = "_c";

  private static final Pattern FIELD_NAME_WITH_EXTID_PATTERN = Pattern.compile( "^\\w+\\:\\w+\\/\\w+$" );

  /**
   * Extract and return the correct name for the field that should be processed as NULL
   *
   * @param log
   *          the logging object
   * @param field
   *          the field that should be processed as NULL
   * @param isUseExtId
   *          the flag that indicates if the field is external id or not
   * @return return the correct name for the field that should be processed as NULL
   */
  public static String getFieldToNullName( LogChannelInterface log, String field, boolean isUseExtId ) {
    String fieldToNullName = field;
    if ( isUseExtId ) {
      // verify if the field has correct syntax
      if ( !FIELD_NAME_WITH_EXTID_PATTERN.matcher( field ).matches() ) {
        if ( log.isDebug() ) {
          log.logDebug( BaseMessages.getString( PKG, "SalesforceUtils.Warn.IncorrectExternalKeySyntax", field,
              fieldToNullName ) );
        }
        return fieldToNullName;
      }

      String lookupField = field.substring( field.indexOf( EXTID_SEPARATOR ) + 1 );
      // working with custom objects and relationship
      // cut off _r and then add _c in the end of the name
      if ( lookupField.endsWith( CUSTOM_OBJECT_RELATIONSHIP_FIELD_SUFFIX ) ) {
        fieldToNullName =
            lookupField.substring( 0, lookupField.length() - CUSTOM_OBJECT_RELATIONSHIP_FIELD_SUFFIX.length() )
                + CUSTOM_OBJECT_SUFFIX;
        if ( log.isDebug() ) {
          log.logDebug( BaseMessages.getString( PKG, "SalesforceUtils.Debug.NullFieldName", fieldToNullName ) );
        }
        return fieldToNullName;
      }

      fieldToNullName = lookupField + "Id";
    }

    if ( log.isDebug() ) {
      log.logDebug( BaseMessages.getString( PKG, "SalesforceUtils.Debug.NullFieldName", fieldToNullName ) );
    }

    return fieldToNullName;
  }

}
