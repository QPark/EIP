package example;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBIntrospector;
import javax.xml.namespace.QName;

public class Demo {

    public static void main(String[] args) throws Exception {
        Class[] classes = new Class[3];
 //       classes[0] = String.class;
        
        JAXBContext jc = JAXBContext.newInstance(classes);
jc.
        JAXBIntrospector ji = jc.createJAXBIntrospector();
        Map<QName, Class> classByQName = new HashMap<QName, Class>(classes.length);
        for(Class clazz : classes) {
            QName qName = ji.getElementName(clazz.newInstance());
            if(null != qName) {
                classByQName.put(qName, clazz);
            }
        }

        QName qName = new QName("http://www.example.com", "EH");
        System.out.println(classByQName.get(qName));
    }

}