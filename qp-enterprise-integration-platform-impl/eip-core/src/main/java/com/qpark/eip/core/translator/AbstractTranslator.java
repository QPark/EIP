/*******************************************************************************
 * Copyright (c) 2013 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Bernhard Hausen - Initial API and implementation
 *
 ******************************************************************************/
package com.qpark.eip.core.translator;

import java.util.Map;
import java.util.TreeMap;

/**
 * Translates properties from source to translated.
 * 
 * @author bhausen
 */
public abstract class AbstractTranslator {
    /** The map containing the key and values. */
    private TreeMap<String, String> translationMap = new TreeMap<String, String>();

    /**
     * @return the properties with the translation values.
     */
    protected abstract Map<String, String> getTranslationProperties();

    /** Initialize. */
    private void init() {
	Map<String, String> map = this.getTranslationProperties();
	String number;
	for (String s0 : map.keySet()) {
	    if (s0.indexOf('.') > 0) {
		number = s0.substring(0, s0.indexOf('.'));
		for (String s1 : map.keySet()) {
		    if (s1.startsWith(number) && !s1.equals(s0)) {
			this.translationMap.put(map.get(s0), map.get(s1));
		    }
		}
	    }
	}
    }

    /**
     * @param source
     *            The source.
     * @return the translated value.
     */
    protected String translate(final String source) {
	if (this.translationMap.size() == 0) {
	    this.init();
	}
	return this.translationMap.get(source);
    }
}
