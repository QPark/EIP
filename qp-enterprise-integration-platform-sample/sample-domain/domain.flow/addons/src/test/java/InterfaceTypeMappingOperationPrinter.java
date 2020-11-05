import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import com.qpark.eip.model.docmodel.DefaultMappingType;
import com.qpark.eip.model.docmodel.DirectMappingType;
import com.samples.platform.inf.iss.tech.support.type.TechnicalSupportSystemUserReportType;

/**
 * @author bhausen
 */
public class InterfaceTypeMappingOperationPrinter {
  /**
   * @param args
   */
  public static void main(final String[] args) {
    final Class<?> type = TechnicalSupportSystemUserReportType.class;

    final List<String> methodNameExcludes =
        Arrays.asList("setInterfaceName", "getIssues", "setValid", "getError", "setDeleted",
            "setLastChange", "getSource", "setOrchestrationDate", "setNotes");
    System.out.println(getMappingOperation(type, methodNameExcludes));

  }

  private static class MappingDefinition {
    private String mapper;
    private Class<?> mappingType;
    private Method method;

    public String getMapper() {
      return this.mapper;
    }

    public Class<?> getMappingType() {
      return this.mappingType;
    }

    public Method getMethod() {
      return this.method;
    }

    public void setMapper(final String mapper) {
      this.mapper = mapper;
    }

    public void setMappingType(final Class<?> mappingType) {
      this.mappingType = mappingType;
    }

    public void setMethod(final Method m) {
      this.method = m;
    }
  }

  private static String getMappingOperation(final Class<?> c,
      final List<String> methodNameExcludes) {
    final StringBuffer sb = new StringBuffer(512);
    sb.append("\n");
    sb.append("\n");
    final String operationName =
        c.getSimpleName().replaceAll("InterfaceType", "MappingOperationImpl");

    final List<Method> ms = getSetterAndCollectionMethods(c, new ArrayList<>()).stream()
        .filter(m -> !methodNameExcludes.contains(m.getName())).collect(Collectors.toList());
    final List<MappingDefinition> definitions = new ArrayList<>();
    ms.stream().forEach(m -> {
      final MappingDefinition def = new MappingDefinition();
      definitions.add(def);
      def.setMethod(m);
      if (isSetter(m)) {
        def.setMappingType(m.getParameterTypes()[0]);
      } else {
        final String retName = m.getGenericReturnType().getTypeName();
        try {
          def.setMappingType(
              Class.forName(retName.substring(retName.indexOf('<') + 1, retName.length() - 1)));
        } catch (final ClassNotFoundException e) {
          e.printStackTrace();
        }
      }
      if (DirectMappingType.class.isAssignableFrom(def.getMappingType())
          || DefaultMappingType.class.isAssignableFrom(def.getMappingType())) {
        def.setMapper(
            String.format("%s.mapper.direct.%s", def.getMappingType().getPackage().getName(),
                def.getMappingType().getSimpleName().replaceAll("MappingType", "Mapper")));
      } else {
        def.setMapper(
            String.format("%s.mapper.complex.%s", def.getMappingType().getPackage().getName(),
                def.getMappingType().getSimpleName().replaceAll("MappingType", "Mapper")));
      }

    });

    final Set<String> imports = new TreeSet<>();
    imports.add("com.qpark.eip.inf.FlowContext");
    imports.add("org.springframework.stereotype.Component");
    imports.add("org.slf4j.Logger");
    imports.add("org.slf4j.LoggerFactory");
    imports.add("java.util.List");
    imports.add("java.util.ArrayList");
    imports.add(c.getName().toString());
    imports.add(String.format("%s.ObjectFactory", c.getPackage().getName()));
    ms.stream()
        .forEach(m -> Arrays.asList(m.getParameterTypes()).stream()
            .filter(p -> !p.getPackage().getName().startsWith("java"))
            .forEach(p -> imports.add(p.getName().toString())));
    definitions.stream().forEach(def -> {
      imports.add(def.getMapper());
    });

    imports.stream().forEach(i -> sb.append("import ").append(i).append(";\n"));

    sb.append("\n");
    sb.append("/**\n");
    sb.append(" * The implementation to map the {@link ");
    sb.append(c.getSimpleName());
    sb.append("}.\n");
    sb.append(" * @author bhausen\n");
    sb.append(" */\n");
    sb.append("@Component\n");
    sb.append("public class ");
    sb.append(operationName);
    sb.append(" {\n");

    sb.append("\t/** The {@link org.slf4j.Logger}. */\n");
    sb.append("\tprivate Logger logger = LoggerFactory.getLogger(");
    sb.append(operationName);
    sb.append(".class);\n");
    sb.append("\t/** The {@link ObjectFactory}. */\n");
    sb.append("\tprivate final ObjectFactory of = new ObjectFactory();\n");

    sb.append("\n");

    definitions.stream().map(def -> def.getMapper()).filter(mapper -> !mapper.startsWith("java."))
        .distinct().forEach(mapper -> {
          final String simpleName = mapper.substring(mapper.lastIndexOf('.') + 1, mapper.length());
          sb.append("\t/** The {@link ");
          sb.append(simpleName);
          sb.append("}. */\n");
          sb.append("\t@Autowired\n");
          sb.append("\tprivate ");
          sb.append(simpleName);
          sb.append(" ");
          sb.append(lowerize(simpleName));
          sb.append(";\n");
        });

    sb.append("\n");
    sb.append("\t/**\n");
    sb.append("\t * @param input the input\n");
    sb.append("\t * @param flowContext the {@link FlowContext}.\n");
    sb.append("\t * @return the mapped {@link ");
    sb.append(c.getSimpleName());
    sb.append("}.\n");
    sb.append("\t */\n");

    sb.append("\tpublic ");
    sb.append(c.getSimpleName());
    sb.append(" invokeMapping(Object request, FlowContext flowContext) {\n");

    sb.append("\t\tthis.logger.trace(\"+invokeMapping {}\", flowContext.getSessionId());\n");

    sb.append("\t\t/* The ");
    sb.append(c.getSimpleName());
    sb.append(" to map. */\n");
    sb.append("\t\t").append(c.getSimpleName()).append(" value = of.create")
        .append(c.getSimpleName()).append("();\n");

    definitions.stream().forEach(def ->

    {
      final Method m = def.getMethod();

      final String mappingType = lowerize(m.getName().substring(3));
      if (m.getName().startsWith("set")) {
        final String mapperSimpleName = lowerize(def.getMapper()
            .substring(def.getMapper().lastIndexOf('.') + 1, def.getMapper().length()));
        sb.append("\n\t\t/* Set ").append(mappingType).append(" of ").append(c.getSimpleName())
            .append(".*/\n");
        sb.append("\t\t// TODO ").append(mappingType).append(" of ").append(c.getSimpleName())
            .append(".\n");
        sb.append("\t\t").append(m.getParameterTypes()[0].getSimpleName()).append(" ")
            .append(mappingType).append(" = new ").append(m.getParameterTypes()[0].getSimpleName())
            .append("();\n");
        sb.append("\t\t\t// ").append(mapperSimpleName)
            .append(".createMappingType(request, flowContext);\n");
        sb.append("\t\tvalue.").append(m.getName()).append("(").append(mappingType).append(");\n");
      } else {
        sb.append("\n\t\t/* Add to collection ").append(mappingType).append(" of ")
            .append(c.getSimpleName()).append(".*/\n");
        sb.append("\t\t// TODO Add to collection ").append(mappingType).append(" of ")
            .append(c.getSimpleName()).append(".\n");
        sb.append("\t\t")
            .append(m.getGenericReturnType().getTypeName().replaceAll("java.util.", "")).append(" ")
            .append(mappingType).append(" = new ArrayList<>();\n");
        sb.append("\t\tvalue.").append(m.getName()).append("().addAll(").append(mappingType)
            .append(");\n");
      }
    });

    sb.append("\n\n\t\tthis.logger.trace(\"-invokeMapping {}\", flowContext.getSessionId());\n");
    sb.append("\t\treturn value;\n");
    sb.append("\t}\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * @param c
   * @param methods
   * @return get setter and collection methods.
   */
  public static List<Method> getSetterAndCollectionMethods(final Class<?> c,
      final ArrayList<Method> methods) {
    if (Objects.nonNull(c.getSuperclass())) {
      getSetterAndCollectionMethods(c.getSuperclass(), methods);
    }
    Arrays.asList(c.getDeclaredMethods()).stream()
        .filter(m -> isSetter(m) || isCollection(m.getReturnType())).forEach(m -> methods.add(m));
    return methods;
  }

  /**
   * @param c
   * @return is the {@link Class} a {@link Collection} or an Array.
   */
  public static boolean isCollection(final Class<?> c) {
    final boolean collection = c.isArray() || Collection.class.isAssignableFrom(c);
    return collection;
  }

  /**
   * @param m
   * @return <code>true</code>, if the {@link Method} is a getter.
   */
  public static boolean isGetter(final Method m) {
    final boolean getter = (m.getName().startsWith("get") || m.getName().startsWith("is"))
        && !m.getName().startsWith("isSet") && !m.getName().equals("getClass")
        && !m.getName().equals("getHjid");
    return getter;
  }

  /**
   * @param m
   * @return <code>true</code>, if the {@link Method} is a setter.
   */
  public static boolean isSetter(final Method m) {
    final boolean setter = m.getName().startsWith("set") && !m.getName().equals("setHjid")
        && m.getParameterTypes().length == 1;
    return setter;
  }

  /**
   * @param m
   * @param ms
   * @return is the setter taking a boolean.
   */
  public static boolean isSetterBoolean(final Method m, final Method[] ms) {
    boolean booleanType = false;
    if (boolean.class.isAssignableFrom(m.getParameterTypes()[0])) {
      booleanType = true;
    }
    return booleanType;
  }

  /**
   * @param name
   * @return the name with the first char in lower case.
   */
  public static String lowerize(final String name) {
    String s = name;
    if (s != null && s.length() > 0) {
      final StringBuffer sb = new StringBuffer(s.length());
      sb.append(s.substring(0, 1).toLowerCase());
      if (s.length() > 1) {
        sb.append(s.substring(1, s.length()));
      }
      s = sb.toString();
    }
    return s;
  }
}
