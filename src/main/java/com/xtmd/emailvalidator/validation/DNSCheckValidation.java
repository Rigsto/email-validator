package com.xtmd.emailvalidator.validation;

import com.xtmd.emailvalidator.EmailLexer;
import com.xtmd.emailvalidator.result.InvalidEmail;
import com.xtmd.emailvalidator.result.reason.DomainAcceptsNoMail;
import com.xtmd.emailvalidator.result.reason.LocalOrReservedDomain;
import com.xtmd.emailvalidator.result.reason.NoDNSRecord;
import com.xtmd.emailvalidator.result.reason.UnableToGetDNSRecord;
import com.xtmd.emailvalidator.warning.NoDNSMXRecord;
import com.xtmd.emailvalidator.warning.Warning;

import java.net.IDN;
import java.util.*;

public class DNSCheckValidation implements EmailValidation {

    public static final List<String> RESERVED_DNS_TOP_LEVEL_NAMES = List.of(
            // Reserved TLDs
            "test", "example", "invalid", "localhost",
            // mDNS
            "local",
            // Private namespaces
            "intranet", "internal", "private", "corp", "home", "lan"
    );

    private final Set<Warning> warnings = new HashSet<>();
    private InvalidEmail error = null;
    private final List<Map<String, Object>> mxRecords = new ArrayList<>();
    private final DNSGetRecordWrapper wrapper;

    public DNSCheckValidation(DNSGetRecordWrapper dnsGetRecordWrapper) {
        this.wrapper = (dnsGetRecordWrapper != null) ? dnsGetRecordWrapper : new DNSGetRecordWrapper();
    }

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
        String ascii = IDN.toASCII(host, IDN.USE_STD3_ASCII_RULES);
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
