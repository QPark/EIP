import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.qpark.eip.model.common.ReferenceDataType;

/**
 * @author bhausen
 */
public class InterfaceTypeGenerator {
  private static Class<?> type = String.class;
  private static boolean addElementAnnotation = false;

  public static void main(final String[] args) {

    type = ReferenceDataType.class;
    addElementAnnotation = true;

    outMethods();
  }

  private static boolean isCollection(final Class<?> c) {
    final boolean collection = c.isArray() || Collection.class.isAssignableFrom(c);
    return collection;
  }

  private static void getFields(final Class<?> c, final List<Field> fs) {
    if (c.getSuperclass() != null && !c.getSuperclass().equals(Object.class)) {
      getFields(c.getSuperclass(), fs);
    }
    fs.addAll(Arrays.asList(c.getDeclaredFields()));
  }

  private static void outMethods() {
    final StringBuffer sb = new StringBuffer();
    sb.append("\t<complexType name=\"");
    sb.append(type.getSimpleName().endsWith("Type")
        ? type.getSimpleName().substring(0, type.getSimpleName().length() - 4)
        : type.getSimpleName());
    sb.append("InterfaceType\">\n");
    sb.append("\t\t<annotation><documentation>Interface type to set ");
    sb.append(type.getName());
    sb.append("</documentation></annotation>\n");
    sb.append("\t\t<complexContent>\n");
    sb.append("\t\t\t<extension base=\"MPmp:InterfaceReferenceType\">\n");
    sb.append("\t\t\t\t<sequence>\n");
    final List<Field> fs = new ArrayList<>();
    getFields(type, fs);
    fs.stream().filter(f -> !f.getName().equalsIgnoreCase("hjid")).forEach(f -> {
      sb.append("\t\t\t\t\t<element name=\"");
      sb.append(f.getName());
      sb.append("\" type=\"string\" ");
      if (isCollection(f.getType())) {
        sb.append("minOccurs=\"0\" maxOccurs=\"unbounded\" ");
      } else {
        sb.append("minOccurs=\"0\" ");
      }
      if (addElementAnnotation) {
        sb.append(">\n");
        sb.append("\t\t\t\t\t\t<annotation><documentation>The ");
        sb.append(f.getName());
        sb.append(".</documentation></annotation>\n");
        sb.append("\t\t\t\t\t</element>\n");
      } else {
        sb.append("/>\n");
      }
    });
    sb.append("\t\t\t\t</sequence>\n");
    sb.append("\t\t\t</extension>\n");
    sb.append("\t\t</complexContent>\n");
    sb.append("\t</complexType>\n");

    System.out.println(sb.toString());
  }
}
