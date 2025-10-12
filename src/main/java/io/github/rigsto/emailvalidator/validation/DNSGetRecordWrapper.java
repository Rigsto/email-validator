package io.github.rigsto.emailvalidator.validation;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import java.util.*;

/**
 * Wrapper class for performing DNS record lookups.
 * <p>
 * This class provides functionality to query DNS records (A, AAAA, MX)
 * for domain names using Java's JNDI DNS context. It handles the
 * conversion of DNS records into a standardized format.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class DNSGetRecordWrapper {

    /**
     * DNS record type constant for A records (IPv4 addresses).
     */
    public static final int DNS_A = 1;
    
    /**
     * DNS record type constant for AAAA records (IPv6 addresses).
     */
    public static final int DNS_AAAA = 2;
    
    /**
     * DNS record type constant for MX records (mail exchange).
     */
    public static final int DNS_MX = 4;

    /**
     * Retrieves DNS records for the specified host and record types.
     * <p>
     * Performs DNS lookups for the specified record types and returns
     * the results in a standardized format.
     * </p>
     * 
     * @param host the hostname to query
     * @param type bitwise combination of DNS record type constants
     * @return DNSRecords containing the query results
     */
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

    /**
     * Interface for mapping DNS record objects to standardized maps.
     */
    private interface Mapper {
        /**
         * Maps a DNS record object to a standardized map format.
         * 
         * @param record the DNS record object to map
         * @return a map containing the record data
         */
        Map<String, Object> map(Object record);
    }
}
