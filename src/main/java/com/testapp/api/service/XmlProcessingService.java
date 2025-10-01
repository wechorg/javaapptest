package com.testapp.api.service;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.StringReader;

@Service
public class XmlProcessingService {
    
    // VULNERABILITY: XML External Entity (XXE) Injection - no protection against XXE
    public Document parseXml(String xmlContent) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // Missing security configurations to prevent XXE:
            // factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            // factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            // factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(new ByteArrayInputStream(xmlContent.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // VULNERABILITY: XXE via SAXParser
    public void parseXmlWithSax(String xmlContent) {
        try {
            javax.xml.parsers.SAXParserFactory saxFactory = javax.xml.parsers.SAXParserFactory.newInstance();
            javax.xml.parsers.SAXParser saxParser = saxFactory.newSAXParser();
            
            org.xml.sax.helpers.DefaultHandler handler = new org.xml.sax.helpers.DefaultHandler();
            saxParser.parse(new ByteArrayInputStream(xmlContent.getBytes()), handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // VULNERABILITY: XSLT Injection
    public String transformXml(String xmlContent, String xsltContent) {
        try {
            javax.xml.transform.TransformerFactory transformerFactory = 
                javax.xml.transform.TransformerFactory.newInstance();
            
            javax.xml.transform.Transformer transformer = transformerFactory.newTransformer(
                new javax.xml.transform.stream.StreamSource(new StringReader(xsltContent))
            );
            
            javax.xml.transform.stream.StreamSource source = 
                new javax.xml.transform.stream.StreamSource(new StringReader(xmlContent));
            
            java.io.StringWriter writer = new java.io.StringWriter();
            javax.xml.transform.stream.StreamResult result = 
                new javax.xml.transform.stream.StreamResult(writer);
            
            transformer.transform(source, result);
            return writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
