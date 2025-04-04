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


package org.pentaho.di.job.entries.setvariables;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.ClassRule;
import org.pentaho.di.job.entries.pgpencryptfiles.JobEntryPGPEncryptFiles;
import org.pentaho.di.job.entry.loadSave.JobEntryLoadSaveTestSupport;
import org.pentaho.di.junit.rules.RestorePDIEngineEnvironment;
import org.pentaho.di.trans.steps.loadsave.validator.ArrayLoadSaveValidator;
import org.pentaho.di.trans.steps.loadsave.validator.FieldLoadSaveValidator;
import org.pentaho.di.trans.steps.loadsave.validator.IntLoadSaveValidator;
import org.pentaho.di.trans.steps.loadsave.validator.PrimitiveIntArrayLoadSaveValidator;
import org.pentaho.di.trans.steps.loadsave.validator.StringLoadSaveValidator;

public class JobEntrySetVariablesLoadSaveTest extends JobEntryLoadSaveTestSupport<JobEntrySetVariables> {
  @ClassRule public static RestorePDIEngineEnvironment env = new RestorePDIEngineEnvironment();

  @Override
  protected Class<JobEntrySetVariables> getJobEntryClass() {
    return JobEntrySetVariables.class;
  }

  @Override
  protected List<String> listCommonAttributes() {
    return Arrays.asList( new String[] { "replaceVars", "filename", "fileVariableType",
      "variableName", "variableValue", "variableType" } );
  }

  @Override
  protected Map<String, FieldLoadSaveValidator<?>> createAttributeValidatorsMap() {
    Map<String, FieldLoadSaveValidator<?>> validators = new HashMap<String, FieldLoadSaveValidator<?>>();
    validators.put( "fileVariableType", new IntLoadSaveValidator( JobEntrySetVariables.variableTypeCode.length ) );

    int count = new Random().nextInt( 50 ) + 1;

    validators.put( "variableName", new ArrayLoadSaveValidator<String>( new StringLoadSaveValidator(), count ) );
    validators.put( "variableValue", new ArrayLoadSaveValidator<String>( new StringLoadSaveValidator(), count ) );
    validators.put( "variableType", new PrimitiveIntArrayLoadSaveValidator(
      new IntLoadSaveValidator( JobEntryPGPEncryptFiles.actionTypeCodes.length ), count ) );

    return validators;
  }
}
