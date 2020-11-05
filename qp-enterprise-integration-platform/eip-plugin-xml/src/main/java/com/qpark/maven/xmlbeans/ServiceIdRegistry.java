/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.maven.xmlbeans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qpark.maven.Util;

/**
 * @author bhausen
 */
public class ServiceIdRegistry {

	private static final String SERVICE_DEFINITION = ".service.";

	private final Map<String, ServiceIdEntry> serviceIdMap = Collections
			.synchronizedSortedMap(new TreeMap<>());
	private final Map<String, ServiceIdEntry> serviceIdPackageNameMap = Collections
			.synchronizedSortedMap(new TreeMap<>());
	private final Set<String> serviceIds = Collections
			.synchronizedSet(new TreeSet<String>());
	private final Map<String, ServiceIdEntry> serviceIdTargetNamespaceMap = Collections
			.synchronizedSortedMap(new TreeMap<>());

	/**
	 * @return
	 */
	public Collection<String> getAllServiceIds() {
		return this.serviceIds.stream().collect(Collectors.toList());
	}

	private static int getIndexDeltaSuffix(final String packageName,
			final String deltaPackageNameSuffix) {
		int index = -1;
		if (deltaPackageNameSuffix != null) {
			List<String> deltaSuffixes = splitByCommaAndSpace(
					deltaPackageNameSuffix);
			StringBuffer sb = new StringBuffer(16);
			for (String deltaSuffix : deltaSuffixes) {
				if (deltaSuffix.charAt(0) != '.') {
					sb.setLength(0);
					deltaSuffix = sb.append(".").append(deltaSuffix).toString();
				}
				if (deltaSuffix.charAt(deltaSuffix.length() - 1) != '.') {
					sb.setLength(0);
					deltaSuffix = sb.append(deltaSuffix).append(".").toString();
				}
				index = packageName.lastIndexOf(deltaSuffix);
				if (index > -1) {
					break;
				}
			}
		}
		return index;
	}

	private static int getIndexMessageSuffix(final String packageName,
			final String messageSuffix) {
		int index = -1;
		if (messageSuffix != null) {
			List<String> msgSuffixes = splitByCommaAndSpace(messageSuffix);
			StringBuffer sb = new StringBuffer(16);
			for (String msgSuffix : msgSuffixes) {
				if (msgSuffix.charAt(0) != '.') {
					sb.setLength(0);
					msgSuffix = sb.append(".").append(msgSuffix).toString();
				}
				index = packageName.lastIndexOf(msgSuffix);
				if (index > -1) {
					break;
				}
			}
		}
		return index;
	}

	public static String capitalize(final String serviceId) {
		String s = Util.capitalizePackageName(serviceId);
		return s;
	}

	/**
	 * Get the service id out of the package name.
	 *
	 * @param packageName
	 * @param targetNamespace
	 * @param messageSuffix
	 * @param deltaPackageNameSuffix
	 * @return the service id.
	 */
	String getServiceId(final String packageName, final String targetNamespace,
			final String messageSuffix, final String deltaPackageNameSuffix) {
		String serviceId = "";
		ServiceIdEntry entry = this.serviceIdPackageNameMap.get(packageName);
		if (entry != null) {
			serviceId = entry.getServiceId();
		} else {
			/* Service id definition. */
			String delta = deltaPackageNameSuffix;
			if (delta != null && delta.trim().length() > 0) {
				if (delta.charAt(0) != '.') {
					delta = new StringBuffer(delta.length() + 1).append(".")
							.append(delta).toString();
				}
				if (delta.charAt(delta.length() - 1) != '.') {
					delta = new StringBuffer(delta.length() + 1).append(delta)
							.append(".").toString();
				}
			}
			if (delta != null && delta.trim().length() == 0) {
				delta = null;
			}
			int indexMsg = getIndexMessageSuffix(packageName, messageSuffix);
			int indexService = packageName.indexOf(SERVICE_DEFINITION);
			if (indexService > 0 && indexMsg > 0 && indexService < indexMsg) {
				int indexDelta = getIndexDeltaSuffix(packageName,
						deltaPackageNameSuffix);
				if (indexDelta > 0 && indexService < indexDelta) {
					serviceId = packageName.substring(
							indexService + SERVICE_DEFINITION.length(),
							indexDelta);
				} else {
					serviceId = packageName.substring(
							indexService + SERVICE_DEFINITION.length(),
							indexMsg);
				}
				entry = new ServiceIdEntry(serviceId, packageName,
						targetNamespace);
				logger.info("Found {}", entry);
				this.serviceIdPackageNameMap.put(packageName, entry);
				this.serviceIdTargetNamespaceMap.put(targetNamespace, entry);
				this.serviceIdMap.put(serviceId, entry);
				this.serviceIds.add(serviceId);
			}
		}
		return serviceId;
	}

	public ServiceIdEntry getServiceIdEntry(final String serviceId) {
		return this.serviceIdMap.get(serviceId);
	}

	public static List<String> splitServiceIds(final String serviceId) {
		List<String> list = splitByCommaAndSpace(serviceId);
		return list;
	}

	public String getCombinedMarshallerContextPath(final String serviceIds) {
		StringBuffer sb = new StringBuffer(128);
		Set<String> sids = new TreeSet<>();
		List<String> list = splitByCommaAndSpace(serviceIds);
		if (list.isEmpty()) {
			sids.addAll(this.getAllServiceIds());
		} else {
			sids.addAll(list);
			for (String sid : list) {
				try {
					sids.addAll(this.getServiceIdEntry(sid)
							.getTotalServiceIdImports());
				} catch (RuntimeException e) {
					throw new RuntimeException(new StringBuffer(64)
							.append("Can not find the serviceId \"").append(sid)
							.append("\" in the ServiceIdRegistry!").toString());
				}
			}
		}
		for (String sid : sids) {
			if (sb.length() > 0) {
				sb.append(":");
			}
			sb.append(this.getServiceIdEntry(sid).getPackageName());
		}
		return sb.toString();
	}

	public boolean isValidServiceId(final String elementServiceId,
			final String serviceId) {
		boolean valid = false;
		Collection<String> serviceIds = splitServiceIds(serviceId);
		if (serviceIds.isEmpty()) {
			valid = true;
		} else {
			ServiceIdEntry entry;
			for (String sid : serviceIds) {
				entry = this.getServiceIdEntry(sid);
				if (entry != null) {
					valid = entry.getServiceId().equals(elementServiceId)
							|| entry.getTotalServiceIdImports()
									.contains(elementServiceId);
					if (valid) {
						break;
					}
				}
			}
		}
		return valid;
	}

	public static String getCombinedServiceIdCapitalizedPackageName(
			final String serviceIds) {
		String s = serviceIds;
		if (serviceIds != null) {
			s = serviceIds.replaceAll("-", ".").replaceAll("( )+", ".")
					.replaceAll("(_)+", ".").replaceAll(",", ".")
					.replaceAll("(\\.)+", ".");
			s = Util.capitalizePackageName(s);
		}
		return s;
	}

	public static String getCombinedServiceIdName(final String serviceIds) {
		String s = serviceIds;
		if (serviceIds != null) {
			s = serviceIds.replaceAll("-", ".").replaceAll("( )+", ".")
					.replaceAll("(_)+", ".").replaceAll(",", ".")
					.replaceAll("(\\.)+", ".");
		}
		return s;
	}

	/** The {@link org.slf4j.Logger}. */
	private static Logger logger = LoggerFactory
			.getLogger(ServiceIdRegistry.class);

	private void setServiceEntryImports(final ServiceIdEntry entry,
			final Map<String, XsdContainer> map) {
		XsdContainer container = map.get(entry.getTargetNamespace());
		if (container == null) {
			throw new IllegalStateException(
					"No ServiceIdEntry found for target namespace \""
							+ entry.getTargetNamespace() + "\"!");
		} else {
			ServiceIdEntry child;
			for (String importedTargetNamespace : container
					.getImportedTargetNamespaces()) {
				child = this.serviceIdTargetNamespaceMap
						.get(importedTargetNamespace);
				if (child != null) {
					entry.getImportedServiceEntries().add(child);
					this.setServiceEntryImports(child, map);
				}
			}
		}
	}

	void setupServiceIdTree(final XsdsUtil xsds) {
		ServiceIdEntry entry;
		for (String serviceId : this.serviceIds) {
			entry = this.serviceIdMap.get(serviceId);
			if (entry == null) {
				throw new IllegalStateException(
						"No ServiceIdEntry found for serviceId \"" + serviceId
								+ "\"!");
			} else {
				XsdContainer container = xsds.getXsdContainerMap()
						.get(entry.getTargetNamespace());
				entry.setAnnotationDocumentation(
						container.getAnnotationDocumentation());
				this.setServiceEntryImports(entry, xsds.getXsdContainerMap());
			}
		}
	}

	private static List<String> splitByCommaAndSpace(final String s) {
		List<String> list = new ArrayList<>();
		if (s != null && s.trim().length() > 0) {
			for (String stringa : s.split(",")) {
				if (stringa.trim().length() > 0) {
					for (String stringb : stringa.split(" ")) {
						if (stringb.trim().length() > 0
								&& !list.contains(stringb.trim())) {
							list.add(stringb.trim());
						}
					}
				}
			}
		}
		return list;
	}

}
