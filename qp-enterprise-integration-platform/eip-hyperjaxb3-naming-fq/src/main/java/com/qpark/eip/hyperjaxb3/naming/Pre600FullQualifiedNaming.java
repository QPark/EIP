/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
/* From http://confluence.highsource.org/display/HJ3/License which is listed as licence URL of hyperjaxb3 version 0.5.6 */
/*******************************************************************************
 * Copyright (c) 2006-2009, Aleksei Valikov
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of Alexey Valikov nor the name of Highsource nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
package com.qpark.eip.hyperjaxb3.naming;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang3.Validate;
import org.jvnet.hyperjaxb3.ejb.strategy.mapping.Mapping;
import org.jvnet.hyperjaxb3.ejb.strategy.naming.Naming;
import org.jvnet.hyperjaxb3.ejb.strategy.naming.impl.DefaultNaming;
import org.jvnet.jaxb2_commons.util.CodeModelUtils;
import org.springframework.beans.factory.InitializingBean;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JType;
import com.sun.tools.xjc.model.Aspect;
import com.sun.tools.xjc.model.nav.NType;
import com.sun.tools.xjc.outline.Outline;

/**
 * This class provides the hyperjaxb3 version 0.5.6 naming.
 *
 * @author bhausen
 */
public class Pre600FullQualifiedNaming extends DefaultNaming
		implements Naming, InitializingBean {
	private Map<String, String> keyNameMap = new TreeMap<String, String>();
	private Map<String, String> nameKeyMap = new TreeMap<String, String>();
	@SuppressWarnings("unused")
	private boolean updated = false;

	@Override
	public String getEntityName(Mapping context, Outline outline, NType type) {
		final JType theType = type.toType(outline, Aspect.EXPOSED);
		assert theType instanceof JClass;
		final JClass theClass = (JClass) theType;
		return CodeModelUtils.getPackagedClassName(theClass);
	}

	/**
	 * @see org.jvnet.hyperjaxb3.ejb.strategy.naming.impl.DefaultNaming#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info(
				"Using com.qpark.eip.hyperjaxb3.naming.Pre600FullQualifiedNaming to generate EntityNames!");
		final Set<Entry<Object, Object>> entries = this.getReservedNames()
				.entrySet();
		for (final Entry<Object, Object> entry : entries) {
			final Object entryKey = entry.getKey();
			if (entryKey != null) {
				final String key = entryKey.toString().toUpperCase();
				final Object entryValue = entry.getValue();
				final String value = entryValue == null
						|| "".equals(entryValue.toString().trim()) ? key + "_"
								: entryValue.toString();
				this.nameKeyMap.put(key, value);
				this.keyNameMap.put(value, key);
			}
		}
	}

	/**
	 * @see org.jvnet.hyperjaxb3.ejb.strategy.naming.impl.DefaultNaming#getName(org.jvnet.hyperjaxb3.ejb.strategy.mapping.Mapping,
	 *      java.lang.String)
	 */
	@Override
	public String getName(final Mapping context, final String draftName) {
		return this.getName(draftName);
	}

	/**
	 * The old name implementation.
	 *
	 * @param draftName the drafted name.
	 * @return the name to use in hyperjaxb3.
	 */
	public String getName(final String draftName) {
		Validate.notNull(draftName, "Name must not be null.");
		final String name = draftName.replace('$', '_').toUpperCase();
		if (this.nameKeyMap.containsKey(name)) {
			return this.nameKeyMap.get(name);
		} else if (name.length() >= this.getMaxIdentifierLength()) {
			for (int i = 0;; i++) {
				final String suffix = Integer.toString(i);
				final String prefix = name.substring(0,
						super.getMaxIdentifierLength() - suffix.length() - 1);
				final String identifier = prefix + "_" + suffix;
				if (!this.keyNameMap.containsKey(identifier)) {
					this.nameKeyMap.put(name, identifier);
					this.keyNameMap.put(identifier, name);
					this.updated = true;
					return identifier;
				}
			}
		} else if (this.getReservedNames().contains(name.toUpperCase())) {
			return name + "_";
		} else {
			return name;
		}
	}
}
