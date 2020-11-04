
import java.time.Duration;
import java.time.Instant;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Formats xml/xsd
 * <pre>
 * Download JavaFX and install/unzip in C:\development\bin\javafx-sdk-11.0.2
 * Add --module-path C:\development\bin\javafx-sdk-11.0.2\lib --add-modules javafx.controls,javafx.fxml to run config.
 * </pre>
 * <pre>
 * 		<dependency>
			<groupId>org.openjfx</groupId>
			<artifactId>javafx-controls</artifactId>
			<version>12</version>
		</dependency>
		<dependency>
			<groupId>org.openjfx</groupId>
			<artifactId>javafx-fxml</artifactId>
			<version>12</version>
		</dependency>
 * </pre>
 *
 * @author bhausen
 */
public class XmlFormatter extends Application {
  /** A Tag. */
  private static class Tag {
    private static String toXsdAttributes(final String tabs,
        final List<SimpleEntry<String, String>> attributes) {
      final StringBuffer sb = new StringBuffer();
      final Map<String, String> asMap = new HashMap<>();
      attributes.stream().forEach(a -> asMap.put(a.getKey(), a.getValue()));

      final String targetNamespace =
          attributes.stream().filter(a -> a.getKey().equals("targetNamespace"))
              .map(a -> a.getValue()).findAny().orElse("");
      final String tnsXmlns = attributes.stream().filter(a -> a.getKey().startsWith("xmlns:"))
          .filter(a -> a.getValue().equals(targetNamespace)).map(a -> a.getKey()).findAny()
          .orElse("");

      String key = "name";
      if (asMap.containsKey(key)) {
        sb.append(String.format(" %s=\"%s\"", key, asMap.get(key)));
      }
      key = "type";
      if (asMap.containsKey(key)) {
        sb.append(String.format(" %s=\"%s\"", key, asMap.get(key)));
      }

      // minOccurs && maxOccurs
      if (asMap.containsKey("minOccurs") && asMap.containsKey("maxOccurs")
          && asMap.get("minOccurs").equals("1") && asMap.get("maxOccurs").equals("1")) {
        // Noting to add!
      } else {
        key = "minOccurs";
        if (asMap.containsKey(key)) {
          sb.append(String.format(" %s=\"%s\"", key, asMap.get(key)));
        }
        key = "maxOccurs";
        if (asMap.containsKey(key) && asMap.get(key).equals("1") && asMap.containsKey("minOccurs")
            && asMap.get("minOccurs").equals("0")) {
          // Nothing to add!
        } else if (asMap.containsKey(key)) {
          sb.append(String.format(" %s=\"%s\"", key, asMap.get(key)));
        }
      }

      sb.append(attributes.stream()
          .filter(a -> !Arrays.asList("name", "type", "minOccurs", "maxOccurs", "targetNamespace")
              .contains(a.getKey()))
          .filter(a -> !a.getKey().startsWith("xmlns:"))
          .map(a -> String.format(" %s=\"%s\"", a.getKey(), a.getValue()))
          .collect(Collectors.joining()));
      key = "xmlns:jaxb";
      if (asMap.containsKey(key)) {
        sb.append(String.format("\n%s%s=\"%s\"\n", tabs, key, asMap.get(key)));
      }

      sb.append(attributes.stream()
          .filter(a -> !Arrays.asList(tnsXmlns, "xmlns:jaxb").contains(a.getKey()))
          .filter(a -> a.getKey().startsWith("xmlns:"))
          .map(a -> String.format("\n%s%s=\"%s\"", tabs, a.getKey(), a.getValue()))
          .collect(Collectors.joining()));
      key = tnsXmlns;
      if (asMap.containsKey(key)) {
        sb.append(String.format("\n\n%s%s=\"%s\"", tabs, key, asMap.get(key)));
      }
      key = "targetNamespace";
      if (asMap.containsKey(key)) {
        sb.append(String.format("\n%s%s=\"%s\"\n", tabs, key, asMap.get(key)));
      }

      return sb.toString();
    }

    private static String toXsdInnerText(final String tabs, final String innerText) {
      final List<String> lines = new ArrayList<>();
      Arrays.asList(innerText.split("\n")).stream().forEach(a -> Arrays.asList(a.split("\r"))
          .stream().forEach(b -> Arrays.asList(b.split("\n\r")).stream().forEach(c -> {
            final String s = c.replaceAll("^(\\t)+", " ").replaceAll("^( )+", " ")
                .replaceAll("(\\t)+$", " ").replaceAll("( )+$", " ").trim();
            if (s.length() > 0) {
              lines.add(s);
            }
          })));
      return lines.stream().collect(Collectors.joining("\n"));
    }

    /** The list of attributes. */
    private final List<SimpleEntry<String, String>> attributes = new ArrayList<>();
    /** The children {@link Tag}s. */
    private final List<Tag> children = new ArrayList<>();
    /** The end position. */
    private int endPos;
    /** The inner text of the tag. */
    private String innerText = "";
    /** The tag name. */
    private String name;
    /** Is comment or not. */
    private boolean isComment = false;

    /**
     * @return the attributes
     */
    public List<SimpleEntry<String, String>> getAttributes() {
      return this.attributes;
    }

    /** Set the comment flag to true. */
    public void setIsComment() {
      this.isComment = true;
      this.name = "";
    }

    /**
     * @return the children
     */
    public List<Tag> getChildren() {
      return this.children;
    }

    /**
     * @return the endPos
     */
    public int getEndPos() {
      return this.endPos;
    }

    /**
     * @return the name
     */
    public String getName() {
      return this.name;
    }

    /**
     * @param endPos the endPos to set
     */
    public void setEndPos(final int endPos) {
      this.endPos = endPos;
    }

    /**
     * @param innerText the innerText to set
     */
    public void setInnerText(final String innerText) {
      if (Objects.nonNull(innerText)) {
        this.innerText = innerText;
      }
    }

    /**
     * @param name the name to set
     */
    public void setName(final String name) {
      this.name = name;
    }

    /**
     * @param tabs
     * @return xml.
     */
    public String toXml(final String tabs) {
      final StringBuffer sb = new StringBuffer();
      if (this.isComment) {
        sb.append(tabs).append("<");
        if (this.innerText.length() > 0) {
          sb.append(this.innerText);
        }
        sb.append(">");
      } else {
        sb.append(tabs).append("<").append(this.name);
        if (this.attributes.size() > 0) {
          sb.append(" ")
              .append(this.attributes.stream()
                  .map(a -> String.format(" %s=\"%s\"", a.getKey(), a.getValue()))
                  .collect(Collectors.joining()));
        }
        if (this.children.isEmpty() && this.innerText.length() == 0) {
          sb.append("/>");
        } else {
          sb.append(">");
          if (this.innerText.length() > 0) {
            sb.append(this.innerText);
          }
          if (this.children.size() > 0) {
            this.children.stream()
                .forEach(t -> sb.append("\n").append(t.toXml(String.format("%s\t", tabs))));
            sb.append("\n").append(tabs).append("</").append(this.name).append(">");
          } else {
            sb.append("</").append(this.name).append(">");
          }
        }
      }
      return sb.toString();
    }

    /**
     * @param tabs
     * @return xml.
     */
    public String toXsd(final String tabs) {
      final StringBuffer sb = new StringBuffer();
      if (this.isComment) {
        sb.append(tabs).append("<");
        if (this.innerText.length() > 0) {
          sb.append(this.innerText);
        }
        sb.append(">");
      } else {
        if (this.name.equals("complexType")) {
          sb.append("\n");
        }
        sb.append(tabs).append("<").append(this.name);
        if (this.attributes.size() > 0) {
          sb.append(toXsdAttributes(String.format("%s\t", tabs), this.attributes));
        }
        if (this.children.isEmpty() && this.innerText.length() == 0) {
          sb.append("/>");
        } else {
          sb.append(">");
          if (this.innerText.length() > 0) {
            sb.append(toXsdInnerText(tabs, this.innerText));
          }
          if (this.children.size() > 0) {
            if (this.name.equals("schema")) {
              sb.append("\n");
            }
            if (this.name.equals("annotation")
                && this.children.get(0).getName().equals("appinfo")) {
              sb.insert(0, "\n");
              this.children.stream()
                  .forEach(t -> sb.append("\n").append(t.toXsd(String.format("%s\t", tabs))));
              sb.append("\n").append(tabs).append("</").append(this.name).append(">\n");
            } else if (this.name.equals("annotation") && this.children.size() == 1
                && this.children.get(0).getName().equals("documentation")) {
              sb.append(this.children.get(0).toXsd(""));
              sb.append("</").append(this.name).append(">");
            } else if (this.name.equals("element") && this.children.size() == 1
                && this.children.get(0).getName().equals("annotation")) {
              sb.append("\n").append(this.children.get(0).toXsd(String.format("%s\t", tabs)));
              sb.append("</").append(this.name).append(">");
            } else {
              this.children.stream()
                  .forEach(t -> sb.append("\n").append(t.toXsd(String.format("%s\t", tabs))));
              sb.append("\n").append(tabs).append("</").append(this.name).append(">");
            }
          } else {
            sb.append("</").append(this.name).append(">");
          }
        }
      }
      return sb.toString();
    }
  }

  /**
   * @param attributes
   * @return the {@link SimpleEntry} of the attribute.
   */
  private static List<SimpleEntry<String, String>> getAttributes(final String... attributes) {
    final List<SimpleEntry<String, String>> value = new ArrayList<>();
    /* attributes[0] is name */
    if (attributes.length > 1) {
      for (int i = 1; i < attributes.length; i++) {
        final String[] attr = attributes[i].split("=");
        if (attr.length == 2) {
          value.add(new SimpleEntry<>(attr[0], attr[1].replace("\"", "")));

        }
      }
    }
    return value;
  }

  /**
   * Starting the application.
   *
   * @param args
   */
  public static void main(final String[] args) {
    Application.launch(args);
  }

  /**
   * @param input
   * @return the formatted xml/xsd
   * @throws Exception
   */
  private String format(final String input) throws Exception {
    final AtomicReference<String> value = new AtomicReference<>(input);
    if (value.get().trim().length() > 0) {
      String preamble = "";
      String xsd = value.get();
      if (xsd.trim().startsWith("<?") && xsd.indexOf("?>") > 0) {
        preamble = xsd.substring(0, xsd.indexOf("?>") + 2);
        xsd = xsd.substring(xsd.indexOf("?>") + 2);
      }
      if (xsd.trim().startsWith("<complexType")) {
        xsd = String.format("<schema>%s</schema>", xsd);
      }
      final Optional<Tag> tag = this.getTag(xsd);
      if (tag.isPresent()) {
        if (tag.get().getName().equals("schema")) {
          if (preamble.length() > 0) {
            value.set(String.format("%s\n%s", preamble, tag.get().toXsd("")));
          } else {
            value.set(tag.get().toXsd(""));
          }
        } else {
          if (preamble.length() > 0) {
            value.set(String.format("%s\n%s", preamble, tag.get().toXml("")));
          } else {
            value.set(tag.get().toXml(""));
          }
        }
      }
    }
    return value.get();
  }

  /**
   * @param xml
   * @return the {@link Tag} from the xml.
   */
  private Optional<Tag> getTag(final String xml) {
    final AtomicReference<Tag> value = new AtomicReference<>();
    boolean directCloseingTag = false;
    final int startPos = xml.indexOf('<');
    int openingEndPos = xml.indexOf('>');
    int endPos = openingEndPos;

    if (startPos > -1 && openingEndPos > 0 && startPos < openingEndPos
        && xml.charAt(startPos + 1) != '/') {
      final Tag t = new Tag();
      if (xml.charAt(openingEndPos - 1) == '/') {
        openingEndPos--;
        directCloseingTag = true;
        endPos++;
      }
      if (xml.indexOf("!--") == startPos + 1) {
        endPos = xml.indexOf("-->") + 3;
        t.setIsComment();
        t.setInnerText(xml.substring(startPos + 1, endPos - 1));
        value.set(t);
      } else if (openingEndPos > startPos) {
        value.set(t);
        final String[] attributes =
            xml.substring(startPos + 1, openingEndPos).replaceAll("\\n", " ").replaceAll("\\r", " ")
                .replaceAll("(\\t)+", " ").replaceAll("( )+", " ").split(" ");
        if (attributes.length > 0) {
          t.setName(attributes[0]);
        }
        t.getAttributes().addAll(getAttributes(attributes));
        if (!directCloseingTag) {
          t.getChildren().addAll(this.getTags(xml.substring(openingEndPos + 1)));
          final String closingTag = String.format("</%s>", t.getName());
          final int lastChildPos = openingEndPos + 1
              + t.getChildren().stream().mapToInt(child -> child.getEndPos()).sum();
          final int posClosingTag = xml.indexOf(closingTag, lastChildPos);
          if (lastChildPos < posClosingTag) {
            t.setInnerText(xml.substring(lastChildPos, posClosingTag));
          }
          endPos = posClosingTag + closingTag.length();
        }
      }
      t.setEndPos(endPos);
    }
    return Optional.ofNullable(value.get());
  }

  /**
   * @param xml
   * @return the list of tags.
   */
  private List<Tag> getTags(final String xml) {
    final List<Tag> value = new ArrayList<>();
    String s = xml;
    String testString = s;
    do {
      final Optional<Tag> tag = this.getTag(s);
      if (tag.isPresent()) {
        value.add(tag.get());
        if (s.length() > tag.get().getEndPos()) {
          s = s.substring(tag.get().getEndPos());
        } else {
          s = "";
        }
      } else {
        s = "";
      }
      if (s.indexOf('<') >= 0 && s.length() > s.indexOf('<')
          && s.charAt(s.indexOf('<') + 1) != '/') {
        testString = s;
      } else {
        testString = s.replaceAll("<\\/.*?>", "").replaceAll("\\n", " ").replaceAll("\\r", " ")
            .replaceAll("(\\t)+", " ").replaceAll("( )+", " ").trim();
      }
    } while (testString.length() > 0);
    return value;
  }

  /**
   * @see javafx.application.Application#start(javafx.stage.Stage)
   */
  @Override
  public void start(final Stage stage) {
    final HBox buttonBox = new HBox();
    buttonBox.setStyle("-fx-border-style: none;-fx-border-width: 100%;-fx-border-color: none");
    final VBox root = new VBox();
    final Scene scene = new Scene(root);

    final TextArea textArea = new TextArea();
    textArea.setPrefHeight(600);
    textArea.setPrefWidth(800);

    final Label status = new Label();
    status.setAlignment(Pos.BASELINE_CENTER);

    final Button formatXml = new Button("Format xml/xsd");
    formatXml.setOnAction(arg0 -> {
      status.setText("");
      final ProgressBar progressIndicator = new ProgressBar();
      final HBox l = new HBox();
      buttonBox.getChildren().add(l);
      buttonBox.getChildren().add(progressIndicator);
      final Task<Void> format = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
          final Instant start = Instant.now();
          final String xml = XmlFormatter.this.format(textArea.getText());
          Platform.runLater(() -> {
            textArea.setText(xml);
            status.setText(String.format("   %,d bytes in %s ", xml.length(),
                Duration.between(start, Instant.now()).toString()));
            buttonBox.getChildren().remove(progressIndicator);
            buttonBox.getChildren().remove(l);
          });
          return null;
        }
      };
      new Thread(format).start();
    });
    buttonBox.setPadding(new Insets(0, 8, 0, 8));
    buttonBox.setAlignment(Pos.CENTER_LEFT);
    buttonBox.getChildren().addAll(formatXml, status);
    HBox.setHgrow(buttonBox, Priority.ALWAYS);

    root.setPadding(new Insets(8, 8, 8, 8));
    root.setSpacing(5);
    root.setAlignment(Pos.BOTTOM_LEFT);
    root.getChildren().addAll(textArea, buttonBox);

    stage.setScene(scene);
    stage.setTitle("A simple xml/xsd formatting");
    stage.show();
  }
}
