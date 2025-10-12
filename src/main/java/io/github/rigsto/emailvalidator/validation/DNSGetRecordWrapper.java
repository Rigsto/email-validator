package io.github.rigsto.emailvalidator.validation;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import java.util.*;

public class DNSGetRecordWrapper {

    public static final int DNS_A = 1;
    public static final int DNS_AAAA = 2;
    public static final int DNS_MX = 4;

    public DNSRecords getRecords(String host, int type) {
        try {
            List<Map<String, Object>> out = new ArrayList<>();
            Map<String, String> env = new HashMap<>();
            env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");

            InitialDirContext idc = new InitialDirContext(new Hashtable<>(env));

            if ((type & DNS_MX) != 0) {
                out.addAll(queryAttr(idc, host, "MX", record -> {
                    String s = String.valueOf(record);

                    Map<String, Object> map = new HashMap<>();
                    map.put("type", "MX");
                    map.put("exchange", s);

                    String[] parts = s.trim().split("\\s+");
                    if (parts.length >= 2) {
                        map.put("target", parts[1]);
                        map.put("pri", parts[0]);
                    } else {
                        map.put("target", s);
                    }

                    return map;
                }));
            }

            if ((type & DNS_A) != 0) {
                out.addAll(queryAttr(idc, host, "A", record -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("type", "A");
                    map.put("ip", String.valueOf(record));

                    return map;
                }));
            }

            if ((type & DNS_AAAA) != 0) {
                out.addAll(queryAttr(idc, host, "AAAA", record -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("type", "AAAA");
                    map.put("ipv6", String.valueOf(record));

                    return map;
                }));
            }

            return new DNSRecords(out, false);
        } catch (Exception e) {
            return new DNSRecords(Collections.emptyList(), true);
        }
    }

    private List<Map<String, Object>> queryAttr(InitialDirContext idc, String host, String attrName, Mapper mapper) {
        List<Map<String, Object>> out = new ArrayList<>();

        try {
            Attributes attrs = idc.getAttributes(host, new String[]{attrName});
            Attribute attr = attrs.get(attrName);

            if (attr == null) {
                return out;
            }

            NamingEnumeration<?> all = attr.getAll();
            while (all.hasMore()) {
                Object rec = all.next();
                out.add(mapper.map(rec));
            }
        } catch (Exception ignored) {}

        return out;
    }

    private interface Mapper {
        Map<String, Object> map(Object record);
    }
}
