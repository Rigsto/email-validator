package com.xtmd.emailvalidator.validation;

import com.xtmd.emailvalidator.EmailValidator;
import com.xtmd.emailvalidator.exception.EmptyValidationList;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class IsEmailFunctionTests {

    @ParameterizedTest(name = "is_email_tests.xml case #{index}: {0}")
    @MethodSource("isEmailTestSuite")
    void testAgainstIsEmailTestSuite(String email) throws EmptyValidationList {
        EmailValidator validator = new EmailValidator();
        MultipleValidationWithAnd validations = new MultipleValidationWithAnd(
                List.of(
                        new NoRFCWarningsValidation(),
                        new DNSCheckValidation()
                )
        );

        assertFalse(validator.isValid(email, validations), "Tested email " + email);
    }


    static Stream<String> isEmailTestSuite() {
        try {
            InputStream in = IsEmailFunctionTests.class.getResourceAsStream("/is_email_tests.xml");
            if (in == null) {
                throw new IllegalStateException("Resource not found: /is_email_tests.xml");
            }

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(false);
            dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);

            Document doc = dbf.newDocumentBuilder().parse(in);

            NodeList testNodes = doc.getElementsByTagName("test");
            List<String> emails = new ArrayList<>(testNodes.getLength());

            for (int i = 0; i < testNodes.getLength(); i++) {
                Node testNode = testNodes.item(i);
                NodeList childNodes = testNode.getChildNodes();
                String value = null;

                for (int c = 0; c < childNodes.getLength(); c++) {
                    Node child = childNodes.item(c);

                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        Element elem = (Element) child;

                        if (elem.hasAttribute("value")) {
                            value = elem.getAttribute("value");
                            break;
                        }
                    }
                }

                if (value != null) {
                    emails.add(value);
                }
            }

            return emails.stream();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load is_email_tests.xml", e);
        }
    }
}
