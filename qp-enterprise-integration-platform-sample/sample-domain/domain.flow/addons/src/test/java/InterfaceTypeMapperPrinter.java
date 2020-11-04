import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import javax.xml.datatype.XMLGregorianCalendar;

import com.qpark.eip.model.common.ReferenceDataType;

/**
 * @author bhausen
 */
public class InterfaceTypeMapperPrinter {
  static final Comparator<String> COMP = (o1, o2) -> {
    if (o1 == o2) {
      return 0;
    } else if (o1 == null) {
      return 1;
    } else if (o2 == null) {
      return -1;
    } else if (o1.toLowerCase().contains("id(") && !o2.toLowerCase().contains("id(")) {
      return -1;
    } else if (o2.toLowerCase().contains("id(") && !o1.toLowerCase().contains("id(")) {
      return 1;
    } else if (o1.toLowerCase().contains("index(") && !o2.toLowerCase().contains("index(")) {
      return -1;
    } else if (o2.toLowerCase().contains("index(") && !o1.toLowerCase().contains("index(")) {
      return 1;
    } else if (o1.toLowerCase().contains("name(") && !o2.toLowerCase().contains("name(")) {
      return -1;
    } else if (o2.toLowerCase().contains("name(") && !o1.toLowerCase().contains("name(")) {
      return 1;
    } else {
      return o1.compareTo(o2);
    }
  };

  private static final HashMap<Class<?>, String> defaults = new HashMap<>();

  private static boolean interfaceType = true;
  private static boolean outXMLGregHandler = false;
  private static Collection<String> propertiesCollection;
  private static Collection<String> propertiesSimple;
  private static boolean setterMethods = true;
  private static boolean setterMethodsWithInputGetterMethod = true;
  private static boolean sortMethods = false;
  private static Class<?> type = String.class;
  private static String varName = "o";
  private static String varName2 = "inf";

  private static String getXmlGregorianCalendarHandler() {

    final StringBuffer sb = new StringBuffer(512);
    if (outXMLGregHandler) {
      sb.append("\n\n");
      sb.append("\t/**\n");
      sb.append("\t* @param xgc\n");
      sb.append("\t* the {@link XMLGregorianCalendar}.\n");
      sb.append("\t* @return the dateTime of the {@link XMLGregorianCalendar}.\n");
      sb.append("\t*/\n");
      sb.append("private static XMLGregorianCalendar toDateTime(XMLGregorianCalendar xgc) {\n");
      sb.append("XMLGregorianCalendar dateTime = xgc;\n");
      sb.append("if (Objects.nonNull(xgc)) {\n");
      sb.append("try {\n");
      sb.append(
          "dateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(xgc.toGregorianCalendar());\n");
      sb.append("} catch (Exception e) {\n");
      sb.append("// Nothing to do.\n");
      sb.append("}\n");
      sb.append("}\n");
      sb.append("return dateTime;\n");
      sb.append("}\n");
    }
    return sb.toString();
  }

  private static void handleMethods() {
    init();
    if (sortMethods) {
      propertiesSimple = new TreeSet<>(COMP);
    } else {
      propertiesSimple = new ArrayList<>();
    }
    propertiesCollection = new TreeSet<>();
    final Method[] ms = type.getMethods();
    for (final Method m : ms) {
      if (setterMethods) {
        handleSetterMethods(m, ms);
      }
    }
  }

  private static void handleSetterMethods(final Method m, final Method[] ms) {
    final StringBuffer sb = new StringBuffer(256);
    final String methodName = m.getName();
    if (isSetter(m)) {
      /* Found a setter. */
      final Class<?> param = m.getParameterTypes()[0];
      if (isCollection(param)) {
      } else if (isSetterXMLGregorianCalendarHavingDateMethod(m, ms)) {
        sb.append(varName);
        sb.append(".");
        sb.append(methodName);
        sb.append("(toDateTime(");
        sb.append(varName2);
        sb.append(".get");
        sb.append(methodName.substring(3, methodName.length()));
        if (isSetterBoolean(m, ms)) {
          sb.append("().isReturn()");
        } else {
          sb.append("().getReturn()");
        }

        sb.append("));");
        // sb.append("}");
        propertiesSimple.add(sb.toString());
      } else if (isSetterDateHavingXMLGregorianCalendarMethod(m, ms)) {
      } else {
        sb.append(varName);
        sb.append(".");
        sb.append(methodName);
        sb.append("(");
        sb.append(varName2);
        sb.append(".get");
        sb.append(methodName.substring(3, methodName.length()));
        if (isSetterBoolean(m, ms)) {
          sb.append("().isReturn()");
        } else {
          sb.append("().getReturn()");
        }

        sb.append(");");
        propertiesSimple.add(sb.toString());
      }
    } else if (

    isGetter(m) && isCollection(m.getReturnType())) {
      /* Found a getter of a collection or array. */
      sb.append(varName);
      sb.append(".");
      sb.append(methodName);
      sb.append("().add(");
      if (varName2 == null) {
        // nothing
      } else {
        if (setterMethodsWithInputGetterMethod) {
          sb.append(varName2);
          sb.append(".");
          sb.append(methodName);
          sb.append("().get(x)");
        } else {
          sb.append(varName2);
          sb.append(".get()");
        }
      }
      sb.append(");");
      propertiesCollection.add(sb.toString());
    }
  }

  private static void init() {
    defaults.put(char.class, "\'c\'");
    defaults.put(double.class, "1.0");
    defaults.put(float.class, "1.0");
    defaults.put(int.class, "1");
    defaults.put(long.class, "1l");
    defaults.put(boolean.class, "false");
    defaults.put(String.class, "\"\"");
    defaults.put(BigDecimal.class, "BigDecimal.valueOf(1d)");
    defaults.put(List.class, "new java.util.ArrayList<?>()");
    defaults.put(Date.class, "new java.util.Date()");
    defaults.put(XMLGregorianCalendar.class, "DateUtil.get(new java.util.Date())");
  }

  private static boolean isCollection(final Class<?> c) {
    final boolean collection = c.isArray() || Collection.class.isAssignableFrom(c);
    return collection;
  }

  private static boolean isGetter(final Method m) {
    final boolean getter = (m.getName().startsWith("get") || m.getName().startsWith("is"))
        && !m.getName().startsWith("isSet") && !m.getName().equals("getClass")
        && !m.getName().equals("getHjid");
    return getter;
  }

  private static boolean isSetter(final Method m) {
    final boolean setter = m.getName().startsWith("set") && !m.getName().equals("setHjid")
        && m.getParameterTypes().length == 1;
    return setter;
  }

  private static boolean isSetterBoolean(final Method m, final Method[] ms) {
    boolean booleanType = false;
    if (boolean.class.isAssignableFrom(m.getParameterTypes()[0])) {
      booleanType = true;
    }
    return booleanType;
  }

  private static boolean isSetterDateHavingXMLGregorianCalendarMethod(final Method m,
      final Method[] ms) {
    boolean dateExists = false;
    if (Date.class.isAssignableFrom(m.getParameterTypes()[0]) && m.getName().endsWith("Item")) {
      for (final Method mx : ms) {
        if (mx.getName().equals(m.getName().substring(0, m.getName().length() - 4))
            && XMLGregorianCalendar.class.isAssignableFrom(mx.getParameterTypes()[0])) {
          dateExists = true;
        }
      }
    }
    return dateExists;
  }

  private static boolean isSetterXMLGregorianCalendarHavingDateMethod(final Method m,
      final Method[] ms) {
    boolean dateExists = false;
    if (XMLGregorianCalendar.class.isAssignableFrom(m.getParameterTypes()[0])) {
      for (final Method mx : ms) {
        if (mx.getName().equals(new StringBuffer(m.getName()).append("Item").toString())
            && Date.class.isAssignableFrom(mx.getParameterTypes()[0])) {
          dateExists = true;
        }
      }
    }
    outXMLGregHandler = true;
    return dateExists;
  }

  /**
   * Get the getter/setter methods of a {@link Class}.
   *
   * @param args
   */
  public static void main(final String[] args) {

    type = ReferenceDataType.class;

    handleMethods();
    outBasicMappingMethod();
  }

  private static void outBasicMappingMethod() {
    System.out.println("/**");
    System.out
        .println(String.format("* Map the {@link Object} to a {@link %s}.", type.getSimpleName()));
    System.out.println("* @param inf the {@link Object}.");
    System.out.println(
        String.format("* @return the {@link Optional} of {@link %s}.", type.getSimpleName()));
    System.out.println("*/");

    System.out.println(String.format("public static final Optional<%s> to%s(final Object inf) {",
        type.getSimpleName(), type.getSimpleName()));
    System.out.println(String.format("Optional<%s> value = Optional.empty();", type.getSimpleName(),
        type.getSimpleName()));
    System.out.println("if (Objects.nonNull(inf)) {");
    System.out
        .println(String.format("%s o = new %s();", type.getSimpleName(), type.getSimpleName()));
    System.out.println("value = Optional.of(o);\n");

    for (final String s : propertiesSimple) {
      System.out.println(s);
    }
    if (propertiesCollection.size() > 0) {
      System.out.println("\n// List properties");
      for (final String s : propertiesCollection) {
        System.out.println(s);
      }
    }
    System.out.println("}");
    System.out.println("return value;");
    System.out.println("}");

    System.out.println(getXmlGregorianCalendarHandler());
  }
}
