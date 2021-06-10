/*******************************************************************************
 * Copyright (c) 2021 QPark Consulting S.a r.l.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.openapi;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

public class OpenApiNormalizer {

	private static class Node {
		private final Optional<Object> example;
		private final List<String> issues = new ArrayList<>();
		private final Optional<List<Object>> nodeList;
		private final Optional<LinkedHashMap<String, Object>> nodeMap;
		private final Optional<List<Object>> parameters;
		private final String path;
		private final Optional<LinkedHashMap<String, Object>> properties;
		private final List<String> required = new ArrayList<>();

		public Node(final String path, final Object value) {
			this.path = path;
			this.nodeMap = getMap(value);
			this.example = this.nodeMap.map(map -> map.get("example"));
			this.properties = this.nodeMap.map(map -> getMap(map.get("properties")).orElse(null));
			this.parameters = this.nodeMap.map(map -> getList(map.get("parameters")).orElse(null));
			this.nodeList = getList(value);
			this.nodeList.ifPresent(list -> validateList(list, this.issues));
			this.nodeMap.ifPresent(map -> {
				getList(map.get("required")).ifPresent(requiredList -> this.required
						.addAll(requiredList.stream().map(String::valueOf).collect(Collectors.toList())));
				sortMap(map);
			});
			validate(this);
		}

		public void addIssue(final String issue) {
			this.issues.add(issue);
		}

		public void cleanup() {
			this.nodeMap.ifPresent(map -> {
				if (exclusions.stream().filter(exclusion -> this.path.contains(exclusion)).count() == 0) {
					map.remove("tags");
					map.remove("summary");
					map.remove("description");
					map.remove("example");
				}
			});
			this.properties.ifPresent(ps -> ps.values().stream().map(OpenApiNormalizer::getMap)
					.filter(Optional::isPresent).map(Optional::get).forEach(propertyMap -> {
						propertyMap.remove("tags");
						propertyMap.remove("summary");
						propertyMap.remove("description");
						propertyMap.remove("example");
					}));
			this.parameters
					.ifPresent(ps -> ps.stream().forEach(parameter -> getMap(parameter).ifPresent(parameterMap -> {
						parameterMap.remove("tags");
						parameterMap.remove("summary");
						parameterMap.remove("description");
						parameterMap.remove("example");
					})));
		}

		public Optional<Object> getExample() {
			return this.example;
		}

		public Collection<String> getIssues() {
			return this.issues;
		}

		public Optional<List<Object>> getNodeList() {
			return this.nodeList;
		}

		public Optional<LinkedHashMap<String, Object>> getNodeMap() {
			return this.nodeMap;
		}


		public Optional<List<Object>> getParameters() {
			return this.parameters;
		}

		public String getPath() {
			return this.path;
		}

		public Optional<LinkedHashMap<String, Object>> getProperties() {
			return this.properties;
		}

		public Collection<String> getRequired() {
			return this.required;
		}
	}

	/* Name of parents, where nothing should get removed from. */
	private static final List<String> exclusions = Arrays.asList("/properties", "/headers", "/parameters",
			"/responses/");

	private static Comparator<Entry<String, Object>> mapKeyComparator = (o1, o2) -> {
		if (o1 == o2) {
			return 0;
		}
		if (o1 == null) {
			return 1;
		}
		if (o2 == null) {
			return -1;
		}
		if (o1.getKey().equals(o2.getKey())) {
			return 0;
		}
		int value = 0;
		if (o1.getKey().equals("name")) {
			value = -18;
		} else if (o2.getKey().equals("name")) {
			value = 18;
		} else if (o1.getKey().equals("required")) {
			value = -17;
		} else if (o2.getKey().equals("required")) {
			value = 17;
		} else if (o1.getKey().equals("type")) {
			value = -16;
		} else if (o2.getKey().equals("type")) {
			value = 16;
		} else if (o1.getKey().equals("format")) {
			value = -15;
		} else if (o2.getKey().equals("format")) {
			value = 15;
		} else if (o1.getKey().equals("id")) {
			value = -14;
		} else if (o2.getKey().equals("id")) {
			value = 14;
		} else if (o1.getKey().equals("@type")) {
			value = -13;
		} else if (o2.getKey().equals("@type")) {
			value = 13;
		} else if (o1.getKey().equals("header")) {
			value = -12;
		} else if (o2.getKey().equals("header")) {
			value = 12;
		} else if (o1.getKey().equals("content")) {
			value = -11;
		} else if (o2.getKey().equals("content")) {
			value = 11;
		} else if (o1.getKey().equals("url")) {
			value = 10;
		} else if (o2.getKey().equals("url")) {
			value = -10;
		} else if (o1.getKey().equals("description")) {
			value = 2;
		} else if (o2.getKey().equals("description")) {
			value = -2;
		} else if (o1.getKey().equals("enum")) {
			value = 1;
		} else if (o2.getKey().equals("enum")) {
			value = -1;
		} else {
			value = o1.getKey().toLowerCase().compareTo(o2.getKey().toLowerCase());
		}
		return value;
	};

	private static Optional<Entry<String, Object>> getEntry(final Object o) {
		return Optional.ofNullable(o).map(obj -> {
			if (Entry.class.isAssignableFrom(o.getClass())) {
				return (Entry<String, Object>) o;
			}
			return null;
		});
	}

	private static Optional<List<Object>> getList(final Object o) {
		return Optional.ofNullable(o).map(obj -> {
			if (List.class.isAssignableFrom(o.getClass())) {
				return (List<Object>) o;
			}
			return null;
		});
	}

	private static Optional<LinkedHashMap<String, Object>> getMap(final Object o) {
		return Optional.ofNullable(o).map(obj -> {
			if (LinkedHashMap.class.isAssignableFrom(o.getClass())) {
				return (LinkedHashMap<String, Object>) o;
			}
			return null;
		});
	}

	public static void main(final String[] args) {
		new OpenApiNormalizer().handleFile(
				"C:\\development\\src\\domain\\com.ses.domain.tmf\\api-ses-tmf675\\src\\main\\resources\\ses-tmf675.json");
	}

	private static List<Object> sortList(final List<Object> list) {
		if (!list.isEmpty() && Objects.nonNull(list.get(0)) && String.class.isAssignableFrom(list.get(0).getClass())) {
			final List<Object> sortedList = list.stream()
					.sorted((u1, u2) -> String.valueOf(u1).compareTo(String.valueOf(u2))).distinct()
					.collect(Collectors.toList());
			list.clear();
			list.addAll(sortedList);
		}
		return list;
	}

	private static final void sortMap(final LinkedHashMap<String, Object> map) {
		final List<Entry<String, Object>> sortedEntries = map.entrySet().stream().sorted(mapKeyComparator)
				.collect(Collectors.toList());
		map.clear();
		sortedEntries.stream().forEach(entry -> {
			map.put(entry.getKey(), entry.getValue());
		});
	}


	private static String stackToPath(final Stack<String> path) {
		return String.format("#/%s", path.stream().collect(Collectors.joining("/")));
	}

	public static Optional<String> toJson(final String yaml) {
		final ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
		Object obj;
		try {
			obj = yamlReader.readValue(yaml, Object.class);
			final ObjectMapper jsonWriter = new ObjectMapper();
			jsonWriter.configure(SerializationFeature.FLUSH_AFTER_WRITE_VALUE, true);
			final String s = jsonWriter.writeValueAsString(obj);
			return Optional.of(s);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

	public static String toYaml(final String string) {
		return Optional.ofNullable(string).map(json -> {
			JsonNode value = null;
			try {
				value = new ObjectMapper().readTree(string);
			} catch (final Exception e) {
				// Nothing to do.
			}
			return value;
		}).map(jsonNode -> {
			String value = null;
			try {
				value = new YAMLMapper().writeValueAsString(jsonNode);
			} catch (final Exception e) {
				// Nothing to do.
			}
			return value;
		}).orElse(string);
	}

	private static void travers(final Object value, final String valueName, final Optional<Map<String, Object>> parent,
			final Stack<String> path, final Map<String, Node> nodeMap) {
		path.add(valueName);
		final Node node = new Node(stackToPath(path), value);
		nodeMap.put(node.getPath(), node);
		node.getNodeMap().ifPresentOrElse(map -> {
			sortMap(map);
			map.entrySet().stream()
					.forEach(entry -> travers(entry.getValue(), entry.getKey(), Optional.of(map), path, nodeMap));
		}, () -> node.getNodeList().ifPresent(list -> {
			parent.ifPresent(p -> p.put(valueName, sortList(list)));
			list.stream().forEach(item -> travers(item, "item", Optional.empty(), path, nodeMap));
		}));
		path.remove(valueName);
	}

	private static void validate(final Node node) {
		if (node.getPath().equals("#/servers")) {
			node.getNodeList()
					.ifPresent(list -> list.stream().map(OpenApiNormalizer::getMap).filter(Optional::isPresent)
							.map(Optional::get).map(m -> Optional.ofNullable(m.get("url")))
							.forEach(optUrl -> optUrl.map(String::valueOf).ifPresentOrElse(url -> {
								try {
									validateUrl(url);
								} catch (final Exception e) {
									node.addIssue(String.format("#/servers 'url' is not a valid URL '%s'.", url));
								}
							}, () -> node.addIssue("#/servers entry missing required 'url' attribute."))));
		} else if (node.getPath().equals("#/info/contact")) {
			node.getNodeMap().ifPresent(map -> {
				getEntry(map.get("url")).map(String::valueOf).ifPresent(url -> {
					try {
						validateUrl(url);
					} catch (final Exception e) {
						node.addIssue(String.format("#/info/contact 'url' is not a valid URL '%s'.", url));
					}
				});
				getEntry(map.get("email")).map(String::valueOf).ifPresent(email -> {
					try {
						validateEmail(email);
					} catch (final Exception e) {
						node.addIssue(String.format("#/info/contact 'email' is not a valid email '%s'.", email));
					}
				});
			});
		} else if (node.getPath().equals("#/info/license")) {
			node.getNodeMap().ifPresent(map -> {
				Optional.ofNullable(map.get("url")).map(String::valueOf).ifPresent(url -> {
					try {
						validateUrl(url);
					} catch (final Exception e) {
						node.addIssue(String.format("#/info/license 'url' is not a valid URL '%s'.", url));
					}
				});
				Optional.ofNullable(map.get("name")).map(String::valueOf).ifPresentOrElse(name -> {
				}, () -> node.addIssue("#/info/license entry missing required 'name' attribute."));
			});
		}
	}

	private static void validateDateTime(final String dateTime) throws Exception {
		if (Objects.nonNull(dateTime)) {
			OffsetDateTime.parse(dateTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
		}
	}


	private static void validateDouble(final String number) throws Exception {
		if (Objects.nonNull(number)) {
			Double.parseDouble(number);
		}
	}

	private static void validateEmail(final String email) throws Exception {
		if (Objects.nonNull(email)
				&& !email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
			throw new Exception("Email does not match format.");
		}
	}

	private static void validateExample(final Node node, final Map<String, Node> refMap, final Yaml yaml) {
		node.getExample().ifPresent(exampleObject -> {
			final LinkedHashMap<String, Object> map = node.getNodeMap().get();
			Optional.ofNullable(map.get("type")).map(String::valueOf).ifPresentOrElse(objectType -> {
				if (String.class.isAssignableFrom(exampleObject.getClass())) {
					validateExampleString(node, map, String.valueOf(exampleObject), objectType);
				} else if (objectType.equals("array")) {
					getList(exampleObject).ifPresentOrElse(exampleList -> {
						Optional.ofNullable(map.get("items")).map(OpenApiNormalizer::getMap).filter(Optional::isPresent)
								.map(Optional::get).ifPresentOrElse(itemDefinition -> {
									Optional.ofNullable(itemDefinition.get("$ref")).map(String::valueOf)
											.ifPresentOrElse(itemRef -> {
												Optional.ofNullable(refMap.get(itemRef)).ifPresentOrElse(refNode -> {
													refNode.getProperties().ifPresent(properties -> {
														exampleList.stream().forEach(exampleListEntry -> {
															getMap(exampleListEntry).ifPresent(example -> {
																final List<String> missingRequiredAttributes = refNode
																		.getRequired().stream()
																		.filter(requiredAttribute -> !example
																				.containsKey(requiredAttribute))
																		.collect(Collectors.toList());
																if (missingRequiredAttributes.size() > 0) {
																	node.addIssue(String.format(
																			"Example missing required attributes %s of referred object type '%s' '%s'.",
																			missingRequiredAttributes,
																			refNode.getPath(),
																			String.valueOf(exampleObject)));
																}
																example.entrySet().forEach(examplePart -> {
																	getMap(properties.get(examplePart.getKey()))
																			.ifPresentOrElse(property -> {
																				Optional.ofNullable(
																						property.get("type"))
																						.map(String::valueOf)
																						.ifPresent(propertyType -> {
																							if (String.class
																									.isAssignableFrom(
																											exampleObject
																													.getClass())) {
																								validateExampleString(
																										node, map,
																										String.valueOf(
																												examplePart
																														.getValue()),
																										propertyType);
																							}
																						});
																			}, () -> node.addIssue(String.format(
																					"Example referred object type '%s' does not define property with name '%s' '%s'.",
																					refNode.getPath(),
																					examplePart.getKey(),
																					String.valueOf(exampleObject))));
																});
															});
														});
													});
												}, () -> node.addIssue(String.format(
														"Object 'type' is 'array' but attribute 'items' '$ref' is not defined in grammar '%s'.",
														String.valueOf(exampleObject))));
											}, () -> {
												Optional.ofNullable(itemDefinition.get("type")).map(String::valueOf)
														.ifPresentOrElse(itemType -> {
															exampleList.stream().forEach(exampleListEntry -> {
																validateExampleString(node, map,
																		String.valueOf(exampleListEntry), itemType);
															});
														}, () -> node.addIssue(String.format(
																"Object 'type' is 'array' but attribute 'items' does not define '$ref' '%s'.",
																String.valueOf(exampleObject))));
											});
								}, () -> node.addIssue(String.format(
										"Object 'type' is 'array' but example does not define a attribute 'items' '%s'.",
										String.valueOf(exampleObject))));
					}, () -> node
							.addIssue(String.format("Object 'type' is 'array' but example does not define a list '%s'.",
									String.valueOf(exampleObject))));
				} else if (objectType.equals("object")) {
					getMap(exampleObject).ifPresentOrElse(exampleMap -> {
						// TODO implement the object verification.
						// Keep in mind there could be a list of examples too!
					}, () -> node.addIssue(
							String.format("Object 'type' is 'object' but example does not define an object '%s'.",
									String.valueOf(exampleObject))));
				}
			}, () -> {
				node.addIssue("Object having example but does not define 'type' attribute.");
			});
		});
	}

	private static void validateExampleString(final Node node, final LinkedHashMap<String, Object> exampleMap,
			final String exampleString, final String exampleType) {
		Optional.ofNullable(exampleMap.get("format")).map(String::valueOf).ifPresent(format -> {
			try {
				if (format.equals("date-time")) {
					validateDateTime(exampleString);
				} else if (format.equals("guid")) {
					validateGuid(exampleString);
				} else if (format.equals("url")) {
					validateUrl(exampleString);
				}
			} catch (final Exception e) {
				node.addIssue(String.format("Example '%s' does not match format '%s'.", exampleString, format));
			}
		});
		Optional.ofNullable(exampleMap.get("pattern")).map(String::valueOf).ifPresent(pattern -> {
			try {
				final boolean matches = exampleString.matches(pattern);
				if (!matches) {
					node.addIssue(String.format("Example '%s' does not match pattern '%s'.", exampleString, pattern));
				}
			} catch (final Exception e) {
				node.addIssue(String.format("Example '%s' does not match pattern '%s'.", exampleString, pattern));
			}
		});
		try {
			if (exampleType.equals("number")) {
				validateNumber(exampleString);
			} else if (exampleType.equals("integer")) {
				validateInteger(exampleString);
			} else if (exampleType.equals("long")) {
				validateLong(exampleString);
			} else if (exampleType.equals("float")) {
				validateFloat(exampleString);
			} else if (exampleType.equals("double")) {
				validateDouble(exampleString);
			}
		} catch (final Exception e) {
			node.addIssue(String.format("Example '%s' does not match type '%s'.", exampleString, exampleType));
		}
	}

	private static void validateFloat(final String number) throws Exception {
		if (Objects.nonNull(number)) {
			Float.parseFloat(number);
		}
	}

	private static void validateGuid(final String guid) throws Exception {
		if (Objects.nonNull(guid)) {
			UUID.fromString(guid);
		}
	}

	private static void validateInteger(final String number) throws Exception {
		if (Objects.nonNull(number)) {
			Integer.parseInt(number);
		}
	}

	private static void validateList(final List<Object> list, final List<String> errors) {
		final Set<String> unique = new HashSet<>();
		list.stream().map(String::valueOf).forEach(s -> {
			if (!unique.add(s)) {
				errors.add(String.format("Entry list conatins multiple times value '%s'.", s));
			}
		});
	}

	private static void validateLong(final String number) throws Exception {
		if (Objects.nonNull(number)) {
			Long.parseLong(number);
		}
	}

	private static void validateNumber(final String number) throws Exception {
		if (Objects.nonNull(number)) {
			Double.parseDouble(number);
		}
	}

	private static List<String> validateOpenApi(final Map<String, Node> refMap) {
		final List<String> value = new ArrayList<>();
		if (!refMap.containsKey("#/openapi")) {
			value.add("Spec misses '#/openapi' definition.");
		}
		if (!refMap.containsKey("#/info")) {
			value.add("Spec misses '#/info' definition.");
		}
		if (!refMap.containsKey("#/info/title")) {
			value.add("Spec misses '#/info/title' definition.");
		}
		if (!refMap.containsKey("#/info/version")) {
			value.add("Spec misses '#/info/version' definition.");
		}
		if (!refMap.containsKey("#/paths")) {
			value.add("Spec misses '#/paths' definition.");
		}
		return value;
	}

	private static void validateReferences(final Node node, final Map<String, Node> refMap) {
		node.getNodeMap()
				.ifPresent(map -> Optional.ofNullable(map.get("$ref")).map(String::valueOf).ifPresent(itemRef -> {
					if (!refMap.containsKey(itemRef)) {
						node.addIssue(String.format("References to an undefined object %s.", itemRef));
					}
				}));
	}

	private static void validateUrl(final String url) throws Exception {
		if (Objects.nonNull(url)) {
			new URL(url).toURI();
		}
	}

	public void handleFile(final String fileName) {
		try (final InputStream is = new FileInputStream(new File(fileName))) {
			final String s = toYaml(new String(is.readAllBytes()));
			final DumperOptions options = new DumperOptions();
			options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
			options.setAllowUnicode(false);
			options.setExplicitStart(true);
			final Yaml yaml = new Yaml(options);

			getMap(yaml.load(s)).ifPresent(o -> {
				final Stack<String> path = new Stack<>();
				final Map<String, Node> refMap = new TreeMap<>();
				o.entrySet().stream().forEach(e -> {
					travers(e.getValue(), e.getKey(), Optional.of(o), path, refMap);
				});
				validateOpenApi(refMap);
				refMap.values().stream().forEach(node -> {
					validateReferences(node, refMap);
					validateExample(node, refMap, yaml);
				});
				refMap.values().stream().forEach(Node::cleanup);
				final Set<String> description = new TreeSet<>();
				// refMap.values().stream().filter(n -> n.getExample().isPresent())
				// .forEach(node -> description.add(String.format("%s: '%s'", node.getPath(),
				// String.valueOf(node.getExample().get()))));
				refMap.values().stream().forEach(node -> node.getIssues().stream()
						.forEach(issue -> description.add(String.format("%s: %s", node.getPath(), issue))));
				if (description.size() > 0) {
					o.put("description", String.format("%s", description.stream().collect(Collectors.joining("\n"))));
				}
				final String outputYaml = yaml.dump(o);
				// refMap.values().stream().forEach(node -> System.out.println(node.getPath()));
				try {
					Files.writeString(Path.of(String.format("./%s.out", fileName)), outputYaml,
							StandardOpenOption.CREATE);
				} catch (final Exception e1) {
					e1.printStackTrace();
				}
				System.out.println(outputYaml.toString());
			});
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
