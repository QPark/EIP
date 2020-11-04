/*******************************************************************************
 * Copyright (c) 2013 - 2020 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.domain.jaxb.doc.test;

import org.junit.Test;

/**
 * @author bhausen
 */
public class DomainDocExcelTest {
  /** Just the trigger ...  */
  @Test
  public void test() {
    try {
      new DomainDocReport().run();
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }
}
