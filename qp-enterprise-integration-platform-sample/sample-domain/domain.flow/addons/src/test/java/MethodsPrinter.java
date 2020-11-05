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
public class MethodsPrinter {
  /**
   * Get the getter/setter methods of a {@link Class}.
   *
   * @param args
   */
  public static void main(final String[] args) {
    sortMethods = false;

    setterMethodsWithInputGetterMethod = true;

    numberCalls = false;

    setterMethods = true;

    interfaceType = true;

    type = ReferenceDataType.class;
    varName = "bus";
    varName2 = null;

    varName2 = "inf";

    handleMethods();
    outMethods();
  }

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
  private static boolean numberCalls = false;
  private static Collection<String> propertiesCollection;
  private static Collection<String> propertiesSimple;
  private static boolean setterMethods = true;
  private static boolean setterMethodsWithInputGetterMethod = true;
  private static boolean sortMethods = true;
  private static boolean interfaceType = false;
  private static Class<?> type = String.class;
  private static String varName = "x";
  private static String varName2 = "y";

  private static void handleGetterMethods(final Method m, final Method[] ms) {
    final StringBuffer sb = new StringBuffer(256);
    final String methodName = m.getName();
    if (isGetter(m)) {
      if (isCollection(m.getReturnType())) {
        sb.append("if (");
        sb.append(varName);
        sb.append(".");
        sb.append(methodName);
        sb.append("() != null && ");
        sb.append(varName);
        sb.append(".");
        sb.append(methodName);
        sb.append("().size() > 0) {");
        sb.append(varName);
        sb.append(".");
        sb.append(methodName);
        sb.append("().get(0);}");
        propertiesCollection.add(sb.toString());
      } else if (isGetterXMLGregorianCalendarWithDate(m, ms)) {
        // Nothing to do here
      } else {
        sb.append(varName);
        sb.append(".");
        sb.append(methodName);
        sb.append("();");
        propertiesSimple.add(sb.toString());
      }
    }
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
      } else {
        handleGetterMethods(m, ms);
      }
    }
  }

  private static void handleSetterMethods(final Method m, final Method[] ms) {
    final StringBuffer sb = new StringBuffer(256);
    final String methodName = m.getName();
    final String propertyName = methodName.substring(3, methodName.length());
    if (isSetter(m)) {
      /* Found a setter. */
      final Class<?> param = m.getParameterTypes()[0];
      if (isCollection(param)) {
        // nothing to do here
      } else if (isSetterXMLGregorianCalendarWithDate(m, ms)) {
        // nothing to do here
      } else {
        sb.append(varName);
        sb.append(".");
        sb.append(methodName);
        sb.append("(");
        if (varName2 == null) {
          /* No second name is given - us a default. */
          if (param.equals(String.class)) {
            sb.append("\"");
            sb.append(propertyName);
            sb.append("\"");
          } else {
            sb.append(defaults.get(param));
          }
        } else {
          /*
           * A second name is given. Use the getter of the second one.
           */
          if (setterMethodsWithInputGetterMethod) {
            sb.append(varName2);
            sb.append(".get");
            sb.append(methodName.substring(3, methodName.length()));
            // sb.append("()");
            if (interfaceType) {
              sb.append("().getReturn()");
            } else {
              sb.append("()");
            }
          } else {
            sb.append(varName2);
            sb.append(".get()");
          }
        }
        sb.append(");");
        propertiesSimple.add(sb.toString());
      }
    } else if (isGetter(m) && isCollection(m.getReturnType())) {
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

  private static boolean isGetterXMLGregorianCalendarWithDate(final Method m, final Method[] ms) {
    boolean dateExists = false;
    if (XMLGregorianCalendar.class.isAssignableFrom(m.getReturnType())) {
      for (final Method mx : ms) {
        if (mx.getName().equals(new StringBuffer(m.getName()).append("Item").toString())
            && Date.class.isAssignableFrom(mx.getReturnType())) {
          dateExists = true;
        }
      }
    }
    return dateExists;
  }

  private static boolean isSetter(final Method m) {
    final boolean setter = m.getName().startsWith("set") && !m.getName().equals("setHjid")
        && m.getParameterTypes().length == 1;
    return setter;
  }

  private static boolean isSetterXMLGregorianCalendarWithDate(final Method m, final Method[] ms) {
    boolean dateExists = false;
    if (XMLGregorianCalendar.class.isAssignableFrom(m.getParameterTypes()[0])) {
      for (final Method mx : ms) {
        if (mx.getName().equals(new StringBuffer(m.getName()).append("Item").toString())
            && Date.class.isAssignableFrom(mx.getParameterTypes()[0])) {
          dateExists = true;
        }
      }
    }
    return dateExists;
  }

  private static void outMethods() {
    int i = 0;
    for (final String s : propertiesSimple) {
      if (numberCalls) {
        System.out.println("// " + ++i + ". TODO");
      }
      System.out.println(s);
    }
    if (propertiesCollection.size() > 0) {
      System.out.println("\n// List properties");
      for (final String s : propertiesCollection) {
        if (numberCalls) {
          System.out.println("// " + ++i + ". TODO");
        }
        System.out.println(s);
      }
    }
  }
}
