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


package org.pentaho.di.imp.rules;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.di.core.util.Utils;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.TransLogTable;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.imp.rule.ImportRuleInterface;
import org.pentaho.di.imp.rule.ImportValidationFeedback;
import org.pentaho.di.imp.rule.ImportValidationResultType;
import org.pentaho.di.trans.TransMeta;
import org.w3c.dom.Node;

/**
 * This rule verifies that a transformation contains a certain transformation log table configuration.
 *
 * @author matt
 *
 */
public class TransformationHasTransLogConfiguredImportRule extends BaseImportRule implements ImportRuleInterface {

  private String schemaName;
  private String tableName;
  private String connectionName;

  public TransformationHasTransLogConfiguredImportRule() {
    super();
  }

  @Override
  public List<ImportValidationFeedback> verifyRule( Object subject ) {

    List<ImportValidationFeedback> feedback = new ArrayList<ImportValidationFeedback>();

    if ( !isEnabled() ) {
      return feedback;
    }
    if ( !( subject instanceof TransMeta ) ) {
      return feedback;
    }

    TransMeta transMeta = (TransMeta) subject;
    TransLogTable transLogTable = transMeta.getTransLogTable();

    if ( !transLogTable.isDefined() ) {
      feedback.add( new ImportValidationFeedback(
        this, ImportValidationResultType.ERROR, "The logging table is not defined" ) );
    } else {
      if ( !Utils.isEmpty( schemaName ) ) {
        if ( schemaName.equals( transLogTable.getSchemaName() ) ) {
          feedback.add( new ImportValidationFeedback(
            this, ImportValidationResultType.APPROVAL, "The schema name is set to: " + schemaName ) );
        } else {
          feedback.add( new ImportValidationFeedback(
            this, ImportValidationResultType.ERROR, "The schema name is not set to: " + schemaName ) );
        }
      }

      if ( !Utils.isEmpty( tableName ) ) {
        if ( tableName.equals( transLogTable.getTableName() ) ) {
          feedback.add( new ImportValidationFeedback(
            this, ImportValidationResultType.APPROVAL, "The table name is set to: " + tableName ) );
        } else {
          feedback.add( new ImportValidationFeedback(
            this, ImportValidationResultType.ERROR, "The table name is not set to: " + tableName ) );
        }
      }

      if ( !Utils.isEmpty( connectionName ) ) {
        if ( connectionName.equals( transLogTable.getDatabaseMeta().getName() ) ) {
          feedback.add( new ImportValidationFeedback(
            this, ImportValidationResultType.APPROVAL, "The database connection used for logging is: "
              + connectionName ) );
        } else {
          feedback.add( new ImportValidationFeedback(
            this, ImportValidationResultType.ERROR, "The database connection used for logging is not: "
              + connectionName ) );
        }
      }

      if ( feedback.isEmpty() ) {
        feedback.add( new ImportValidationFeedback(
          this, ImportValidationResultType.APPROVAL, "The logging table is correctly defined" ) );
      }
    }

    return feedback;
  }

  @Override
  public String getXML() {

    StringBuilder xml = new StringBuilder();
    xml.append( XMLHandler.openTag( XML_TAG ) );

    xml.append( super.getXML() ); // id, enabled

    xml.append( XMLHandler.addTagValue( "schema_name", schemaName ) );
    xml.append( XMLHandler.addTagValue( "table_name", tableName ) );
    xml.append( XMLHandler.addTagValue( "connection_name", connectionName ) );

    xml.append( XMLHandler.closeTag( XML_TAG ) );
    return xml.toString();
  }

  @Override
  public void loadXML( Node ruleNode ) throws KettleException {
    super.loadXML( ruleNode );

    schemaName = XMLHandler.getTagValue( ruleNode, "schema_name" );
    tableName = XMLHandler.getTagValue( ruleNode, "table_name" );
    connectionName = XMLHandler.getTagValue( ruleNode, "connection_name" );
  }

  /**
   * @return the schemaName
   */
  public String getSchemaName() {
    return schemaName;
  }

  /**
   * @param schemaName
   *          the schemaName to set
   */
  public void setSchemaName( String schemaName ) {
    this.schemaName = schemaName;
  }

  /**
   * @return the tableName
   */
  public String getTableName() {
    return tableName;
  }

  /**
   * @param tableName
   *          the tableName to set
   */
  public void setTableName( String tableName ) {
    this.tableName = tableName;
  }

  /**
   * @return the connectionName
   */
  public String getConnectionName() {
    return connectionName;
  }

  /**
   * @param connectionName
   *          the connectionName to set
   */
  public void setConnectionName( String connectionName ) {
    this.connectionName = connectionName;
  }
}
