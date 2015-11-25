package com.qpark.maven.xmlbeans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qpark.maven.Util;

/**
 * @author bhausen
 */
public class ServiceIdRegistry {

	private static final String SERVICE_DEFINITION = ".service.";

	private static final Map<String, ServiceIdEntry> serviceIdMap = new TreeMap<String, ServiceIdEntry>();
	private static final Map<String, ServiceIdEntry> serviceIdPackageNameMap = new TreeMap<String, ServiceIdEntry>();
	private static final Set<String> serviceIds = new TreeSet<String>();
	private static final Map<String, ServiceIdEntry> serviceIdTargetNamespaceMap = new TreeMap<String, ServiceIdEntry>();

	/**
	 * @return
	 */
	public static Collection<String> getAllServiceIds() {
		Set<String> ts = new TreeSet<String>();
		ts.addAll(serviceIds);
		return ts;
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
	static String getServiceId(final String packageName,
			final String targetNamespace, final String messageSuffix,
			final String deltaPackageNameSuffix) {
		String serviceId = "";
		ServiceIdEntry entry = serviceIdPackageNameMap.get(packageName);
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
				serviceIdPackageNameMap.put(packageName, entry);
				serviceIdTargetNamespaceMap.put(targetNamespace, entry);
				serviceIdMap.put(serviceId, entry);
				serviceIds.add(serviceId);
			}
		}
		return serviceId;
	}

	public static ServiceIdEntry getServiceIdEntry(final String serviceId) {
		return serviceIdMap.get(serviceId);
	}

	public static List<String> splitServiceIds(final String serviceId) {
		List<String> list = splitByCommaAndSpace(serviceId);
		return list;
	}

	public static String getCombinedMarshallerContextPath(
			final String serviceIds) {
		StringBuffer sb = new StringBuffer(128);
		Set<String> sids = new TreeSet<String>();
		List<String> list = splitByCommaAndSpace(serviceIds);
		if (list.isEmpty()) {
			sids.addAll(getAllServiceIds());
		} else {
			sids.addAll(list);
			for (String sid : list) {
				try {
					sids.addAll(
							getServiceIdEntry(sid).getTotalServiceIdImports());
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
			sb.append(getServiceIdEntry(sid).getPackageName());
		}
		return sb.toString();
	}

	public static boolean isValidServiceId(final String elementServiceId,
			final String serviceId) {
		boolean valid = false;
		Collection<String> serviceIds = splitServiceIds(serviceId);
		if (serviceIds.isEmpty()) {
			valid = true;
		} else {
			ServiceIdEntry entry;
			for (String sid : serviceIds) {
				entry = getServiceIdEntry(sid);
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

	public static void main(final String[] args) {
		System.out.println(splitByCommaAndSpace(
				"appcontrolling, monitoring, busappmonics"));
		System.out.println(
				getServiceId("com.ses.osp.bus.service.directory.v20.delta.msg",
						"httpx", "msg", "delta"));
		System.out.println(
				getServiceId("com.ses.osp.bus.service.directory.v20.msg",
						"httpx", "msg", "delta"));
		for (String s : splitServiceIds("abc.cde")) {
			System.out.println(s);
		}
		System.out.println("#########");
		for (String s : splitServiceIds("abc.cde, efg.hij")) {
			System.out.println(s);
		}
		System.out.println("#########");
		System.out.println(getCombinedServiceIdName("abc.cde"));
		System.out
				.println(getCombinedServiceIdCapitalizedPackageName("abc.cde"));
		System.out.println("#########");
		System.out.println(getCombinedServiceIdName("abc.cde, efg.hij"));
		System.out.println(
				getCombinedServiceIdCapitalizedPackageName("abc.cde, efg.hij"));

		System.out.println(isValidServiceId("directory.v20", "directory.v20"));
		System.out.println(
				getCombinedServiceIdName("iss.common,  commmon,     libRary"));
		System.out.println(getCombinedServiceIdCapitalizedPackageName(
				"iss.common,  commmon,     libRary"));
	}

	/** The {@link org.slf4j.Logger}. */
	private static Logger logger = LoggerFactory
			.getLogger(ServiceIdRegistry.class);

	private static void setServiceEntryImports(final ServiceIdEntry entry,
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
				child = serviceIdTargetNamespaceMap
						.get(importedTargetNamespace);
				if (child != null) {
					entry.getImportedServiceEntries().add(child);
					setServiceEntryImports(child, map);
				}
			}
		}
	}

	static void setupServiceIdTree(final XsdsUtil xsds) {
		ServiceIdEntry entry;
		for (String serviceId : serviceIds) {
			entry = serviceIdMap.get(serviceId);
			if (entry == null) {
				throw new IllegalStateException(
						"No ServiceIdEntry found for serviceId \"" + serviceId
								+ "\"!");
			} else {
				setServiceEntryImports(entry, xsds.getXsdContainerMap());
			}
		}
	}

	private static List<String> splitByCommaAndSpace(final String s) {
		List<String> list = new ArrayList<String>();
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
