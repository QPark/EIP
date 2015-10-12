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
package com.qpark.eip.core.spring;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.springframework.integration.router.AbstractMappingMessageRouter;
import org.springframework.messaging.Message;
import org.springframework.util.CollectionUtils;

/**
 * @author bhausen
 */
public class JAXBElementAwarePayloadTypeRouter extends AbstractMappingMessageRouter {
    private static final String ARRAY_SUFFIX = "[]";

    /**
     * Selects the most appropriate channel name matching channel identifiers
     * which are the fully qualified class names encountered while traversing
     * the payload type hierarchy. To resolve ties and conflicts (e.g.,
     * Serializable and String) it will match: 1. Type name to channel
     * identifier else... 2. Name of the subclass of the type to channel
     * identifier else... 3. Name of the Interface of the type to channel
     * identifier while also preferring direct interface over indirect subclass
     */
    @Override
    protected List<Object> getChannelKeys(final Message<?> message) {
	if (CollectionUtils.isEmpty(this.getChannelMappings())) {
	    return null;
	}
	Object o = message.getPayload();
	Class<?> type = message.getPayload().getClass();
	if (o.getClass().equals(JAXBElement.class)) {
	    type = ((JAXBElement<?>) o).getDeclaredType();
	}
	boolean isArray = type.isArray();
	if (isArray) {
	    type = type.getComponentType();
	}
	String closestMatch = this.findClosestMatch(type, isArray);
	return closestMatch != null ? Collections.<Object> singletonList(closestMatch) : null;
    }

    private String findClosestMatch(final Class<?> type, final boolean isArray) {
	int minTypeDiffWeight = Integer.MAX_VALUE;
	List<String> matches = new ArrayList<String>();
	for (String candidate : this.getChannelMappings().keySet()) {
	    if (isArray) {
		if (!candidate.endsWith(ARRAY_SUFFIX)) {
		    continue;
		}
		// trim the suffix
		candidate = candidate.substring(0, candidate.length() - ARRAY_SUFFIX.length());
	    } else if (candidate.endsWith(ARRAY_SUFFIX)) {
		continue;
	    }
	    int typeDiffWeight = this.determineTypeDifferenceWeight(candidate, type, 0);
	    if (typeDiffWeight < minTypeDiffWeight) {
		minTypeDiffWeight = typeDiffWeight;
		// new winner, start accumulating matches from scratch
		matches.clear();
		matches.add(isArray ? candidate + ARRAY_SUFFIX : candidate);
	    } else if (typeDiffWeight == minTypeDiffWeight && typeDiffWeight != Integer.MAX_VALUE) {
		// candidate tied with current winner, keep track
		matches.add(candidate);
	    }
	}
	if (matches.size() > 1) { // ambiguity
	    throw new IllegalStateException("Unresolvable ambiguity while attempting to find closest match for ["
		    + type.getName() + "]. Found: " + matches);
	}
	if (CollectionUtils.isEmpty(matches)) { // no match
	    return null;
	}
	// we have a winner
	return matches.get(0);
    }

    private int determineTypeDifferenceWeight(final String candidate, final Class<?> type, final int level) {
	if (type.getName().equals(candidate)) {
	    return level;
	}
	for (Class<?> iface : type.getInterfaces()) {
	    if (iface.getName().equals(candidate)) {
		return level % 2 == 1 ? level + 2 : level + 1;
	    }
	    // no match at this level, continue up the hierarchy
	    for (Class<?> superInterface : iface.getInterfaces()) {
		int weight = this.determineTypeDifferenceWeight(candidate, superInterface, level + 3);
		if (weight < Integer.MAX_VALUE) {
		    return weight;
		}
	    }
	}
	if (type.getSuperclass() == null) {
	    // exhausted hierarchy and found no match
	    return Integer.MAX_VALUE;
	}
	return this.determineTypeDifferenceWeight(candidate, type.getSuperclass(), level + 2);
    }
}
