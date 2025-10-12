package io.github.rigsto.emailvalidator.validation;

import io.github.rigsto.emailvalidator.EmailLexer;
import io.github.rigsto.emailvalidator.result.InvalidEmail;
import io.github.rigsto.emailvalidator.result.reason.DomainAcceptsNoMail;
import io.github.rigsto.emailvalidator.result.reason.LocalOrReservedDomain;
import io.github.rigsto.emailvalidator.result.reason.NoDNSRecord;
import io.github.rigsto.emailvalidator.result.reason.UnableToGetDNSRecord;
import io.github.rigsto.emailvalidator.warning.NoDNSMXRecord;
import io.github.rigsto.emailvalidator.warning.Warning;

import java.net.IDN;
import java.util.*;

/**
 * Validation strategy for DNS-based email validation.
 * <p>
 * This validation performs DNS lookups to verify that the domain part of
 * an email address has valid DNS records (A, AAAA, MX). It checks for
 * reserved domains, local domains, and validates MX records according to
 * RFC standards.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class DNSCheckValidation implements EmailValidation {

    /**
     * List of reserved DNS top-level domain names that should be rejected.
     */
    public static final List<String> RESERVED_DNS_TOP_LEVEL_NAMES = List.of(
            // Reserved TLDs
            "test", "example", "invalid", "localhost",
            // mDNS
            "local",
            // Private namespaces
            "intranet", "internal", "private", "corp", "home", "lan"
    );

    /**
     * Set of warnings collected during DNS validation.
     */
    private final Set<Warning> warnings = new HashSet<>();
    
    /**
     * The error from the last validation, if any.
     */
    private InvalidEmail error = null;
    
    /**
     * List of MX records found during validation.
     */
    private final List<Map<String, Object>> mxRecords = new ArrayList<>();
    
    /**
     * The DNS record wrapper for performing DNS lookups.
     */
    private final DNSGetRecordWrapper wrapper;

    /**
     * Creates a new DNSCheckValidation with the specified DNS wrapper.
     * 
     * @param dnsGetRecordWrapper the DNS wrapper to use for lookups
     */
    public DNSCheckValidation(DNSGetRecordWrapper dnsGetRecordWrapper) {
        this.wrapper = (dnsGetRecordWrapper != null) ? dnsGetRecordWrapper : new DNSGetRecordWrapper();
    }

    /**
     * Creates a new DNSCheckValidation with a default DNS wrapper.
     */
    public DNSCheckValidation() {
        this(null);
    }

    @Override
    public boolean isValid(String email, EmailLexer emailLexer) {
        String host = email;
        int at = email.lastIndexOf('@');

        if (at != -1 && at < email.length() - 1) {
            host = email.substring(at + 1);
        }

        String[] hostParts = host.split("\\.");
        boolean isLocalDomain = hostParts.length <= 1;
        String lastLabel = hostParts.length == 0 ? "" : hostParts[hostParts.length - 1].toLowerCase(Locale.ROOT);
        boolean isReservedTopLevel = RESERVED_DNS_TOP_LEVEL_NAMES.contains(lastLabel);

        if (isLocalDomain || isReservedTopLevel) {
            this.error = new InvalidEmail(new LocalOrReservedDomain(), host);
            return false;
        }

        return checkDns(host);
    }

    @Override
    public InvalidEmail getError() {
        return this.error;
    }

    @Override
    public List<Warning> getWarnings() {
        return new ArrayList<>(this.warnings);
    }

    protected boolean checkDns(String host) {
        String ascii = "";

        try {
            ascii = IDN.toASCII(host, IDN.USE_STD3_ASCII_RULES);
        } catch (IllegalArgumentException e) {
            return false;
        }

        ascii = ascii.replaceAll("\\.$", "");

        Deque<String> parts = new ArrayDeque<>(Arrays.asList(ascii.split("\\.")));
        if (parts.isEmpty()) {
            return false;
        }

        String current = parts.removeLast();
        do {
            if (!parts.isEmpty()) {
                current = parts.removeLast() + "." + current;
            }

            if (validateDnsRecords(current)) {
                return true;
            }

        } while (!parts.isEmpty());

        return false;
    }

    private boolean validateDnsRecords(String host) {
        DNSRecords dnsRecordsResult = this.wrapper.getRecords(host, DNSGetRecordWrapper.DNS_A | DNSGetRecordWrapper.DNS_MX);
        if (dnsRecordsResult.withError()) {
            this.error = new InvalidEmail(new UnableToGetDNSRecord(), "");
            return false;
        }

        List<Map<String, Object>> dnsRecords = new ArrayList<>(dnsRecordsResult.getRecords());

        DNSRecords aaaaRecordsResult = this.wrapper.getRecords(host, DNSGetRecordWrapper.DNS_AAAA);
        if (!aaaaRecordsResult.withError()) {
            dnsRecords.addAll(aaaaRecordsResult.getRecords());
        }

        if (dnsRecords.isEmpty()) {
            this.error = new InvalidEmail(new NoDNSRecord(), "");
            return false;
        }

        boolean anyInvalid = false;

        for (Map<String, Object> record : dnsRecords) {
            if (!validateMxRecord(record)) {
                anyInvalid = true;

                if (this.mxRecords.isEmpty()) {
                    this.warnings.add(new NoDNSMXRecord());
                }
            }
        }

        return !anyInvalid || !this.mxRecords.isEmpty();
    }

    private boolean validateMxRecord(Map<String, Object> dnsRecord) {
        Object typeObj = dnsRecord.get("type");
        if (typeObj == null) {
            this.error = new InvalidEmail(new NoDNSRecord(), "");
            return false;
        }

        String type = String.valueOf(typeObj).toUpperCase(Locale.ROOT);
        if (!"MX".equals(type)) {
            return true;
        }

        Object targetObj = dnsRecord.get("target");
        String target = targetObj == null ? "" : String.valueOf(targetObj).trim();

        if (target.isEmpty() || ".".equals(target)) {
            this.error = new InvalidEmail(new DomainAcceptsNoMail(), "");
            return false;
        }

        this.mxRecords.add(dnsRecord);
        return true;
    }
}
