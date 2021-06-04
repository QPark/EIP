/*******************************************************************************
 * Copyright (c) 2021 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.openapi;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

import org.yaml.snakeyaml.Yaml;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

public class OpenApiNormalizer {


	public static void main(final String[] args) {
		new OpenApiNormalizer().handleFile("/OpenApi-A.yaml");
		// new YamlCompareable().parse("/openApi-B.txt");
	}


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
			value = -8;
		} else if (o2.getKey().equals("name")) {
			value = 8;
		} else if (o1.getKey().equals("required")) {
			value = -7;
		} else if (o2.getKey().equals("required")) {
			value = 7;
		} else if (o1.getKey().equals("type")) {
			value = -6;
		} else if (o2.getKey().equals("type")) {
			value = 6;
		} else if (o1.getKey().equals("format")) {
			value = -5;
		} else if (o2.getKey().equals("format")) {
			value = 5;
		} else if (o1.getKey().equals("id")) {
			value = -4;
		} else if (o2.getKey().equals("id")) {
			value = 4;
		} else if (o1.getKey().equals("@type")) {
			value = -3;
		} else if (o2.getKey().equals("@type")) {
			value = 3;
		} else if (o1.getKey().equals("header")) {
			value = -2;
		} else if (o2.getKey().equals("header")) {
			value = 2;
		} else if (o1.getKey().equals("content")) {
			value = -1;
		} else if (o2.getKey().equals("content")) {
			value = 1;
		} else if (o1.getKey().equals("enum")) {
			value = 1;
		} else if (o2.getKey().equals("enum")) {
			value = -1;
		} else {
			value = o1.getKey().compareTo(o2.getKey());
		}
		return value;
	};

	private static String toYaml(final String string) {
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

	private static Optional<String> toJson(final String yaml) {
		ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
		Object obj;
		try {
			obj = yamlReader.readValue(yaml, Object.class);
			ObjectMapper jsonWriter = new ObjectMapper();
			String s = jsonWriter.writeValueAsString(obj);
			return Optional.of(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

	private static Optional<List<Object>> getList(final Object o, final Stack<String> path, final List<String> errors) {
		return Optional.ofNullable(o).map(obj -> {
			if (List.class.isAssignableFrom(o.getClass())) {
				checkListEntries((List<Object>) o, path, errors);
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

	/* Name of parents, where nothing should get removed from. */
	private static final List<String> exclusions = Arrays.asList("properties", "headers", "parameters");

	private static void putExample(final LinkedHashMap<String, Object> map, final Stack<String> path,
			final Map<String, Entry<Map<String, Object>, Object>> examples) {
		if (map.containsKey("example")) {
			Optional.ofNullable(map.get("example")).ifPresent(example -> {
				examples.put(stackToPath(path), new SimpleEntry<>(map, example));
			});
		}
	}

	private static void travers(final Object value, final String parentName, final Optional<Map<String, Object>> parent,
			final Stack<String> path, final Map<String, Entry<Map<String, Object>, Object>> examples,
			final List<String> errors) {
		path.add(parentName);
		getMap(value).ifPresentOrElse(map -> {
			putExample(map, path, examples);
			/* Cleanup */
			if (!exclusions.contains(parentName)) {
				map.remove("tags");
				map.remove("summary");
				map.remove("description");
				map.remove("example");
			}
			/* Sort */
			List<Entry<String, Object>> sortedEntries = map.entrySet().stream().sorted(mapKeyComparator)
					.collect(Collectors.toList());
			map.clear();
			sortedEntries.stream().forEach(p -> {
				map.put(p.getKey(), p.getValue());
			});
			map.entrySet().stream().forEach(
					entry -> travers(entry.getValue(), entry.getKey(), Optional.of(map), path, examples, errors));
		}, () -> getList(value, path, errors).ifPresent(list -> {
			if (!list.isEmpty() && Objects.nonNull(list.get(0))
					&& String.class.isAssignableFrom(list.get(0).getClass())) {
				parent.ifPresent(p -> p.put(parentName,
						list.stream().sorted((u1, u2) -> String.valueOf(u1).compareTo(String.valueOf(u2))).distinct()
								.collect(Collectors.toList())));
			}
			list.stream()
					.forEach(listEntry -> travers(listEntry, "listEntry", Optional.empty(), path, examples, errors));
		}));
		path.remove(parentName);
	}

	private static void checkListEntries(final List<Object> list, final Stack<String> path, final List<String> errors) {
		Set<String> unique = new HashSet<>();
		list.stream().map(String::valueOf).forEach(s -> {
			if (!unique.add(s)) {
				errors.add(String.format("Error in path '%s': Entry list conatins multiple times value '%s'.",
						stackToPath(path), s));
			}
		});
	}

	private static String stackToPath(final Stack<String> path) {
		return String.format("#/%s", path.stream().collect(Collectors.joining("/")));
	}

	private void handleFile(final String fileNamename) {
		try (final InputStream is = OpenApiNormalizer.class.getResourceAsStream(fileNamename)) {
			final String s = toYaml(new String(is.readAllBytes()));
			final Yaml yaml = new Yaml();
			getMap(yaml.load(s)).ifPresent(o -> {
				List<String> errors = new ArrayList<>();
				final Map<String, Entry<Map<String, Object>, Object>> examples = new HashMap<>();
				Stack<String> path = new Stack<>();
				o.entrySet().stream().forEach(e -> {
					travers(e.getValue(), e.getKey(), Optional.of(o), path, examples, errors);
				});
				List<String> description = new ArrayList<>();
				if (errors.size() > 0) {
					errors.stream().filter(Objects::nonNull).forEach(error -> description.add(error));
				}
				if (examples.size() > 0) {
					examples.entrySet().stream()
							.map(entry -> String.format("Example of path %s: %s", entry.getKey(),
									toJson(yaml.dump(String.valueOf(entry.getValue().getValue()))).orElse("-")))
							.forEach(example -> description.add(example));
				}
				if (description.size() > 0) {
					o.put("description", String.format("%s", description.stream().collect(Collectors.joining("\n"))));
				}
				try {
					Files.writeString(Path.of(String.format("./%s.out", fileNamename)), yaml.dumpAsMap(o),
							StandardOpenOption.CREATE);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				System.out.println(yaml.dumpAsMap(o));
			});
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
