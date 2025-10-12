package io.github.rigsto.emailvalidator;

import io.github.rigsto.emailvalidator.result.InvalidEmail;
import io.github.rigsto.emailvalidator.validation.DNSCheckValidation;
import io.github.rigsto.emailvalidator.validation.EmailValidation;
import io.github.rigsto.emailvalidator.warning.Warning;

import java.util.List;

public class EmailDemo {

    public static void main(String[] args) {
        EmailValidator validator = new EmailValidator();
        EmailValidation rule = new DNSCheckValidation();

        List<String> samples = List.of(
                // Valid-looking
                "john.doe@gmail.com",
                "jane_doe+tag@outlook.com",
                "admin@mail.example.com",
                "sales@sub.domain.co.uk",
                "user@xn--bcher-kva.example",        // punycode tld form (bücher)
                "\"quoted.local\"@example.com",
                "first.last@iana.org",
                "simple@example.co",
                "UPPER@EXAMPLE.COM",
                "a@b.io",

                // Edge / tricky
                "dot..dot@example.com",               // double dot in local
                ".startdot@example.com",              // dot at start local
                "enddot.@example.com",                // dot at end local
                "no-at-symbol.example.com",           // missing '@'
                "user@localhost",                     // reserved/local
                "user@invalid",                       // reserved TLD
                "user@domain-with-hyphen-.com",       // hyphen at the end of label
                "user@-badprefix.com",                // hyphen at start of label
                "user@domain..double",                // double dot in domain
                "user@[127.0.0.1]",                   // IPv4 literal

                // IPv6 literal variants
                "user@[IPv6:2001:db8::1]",
                "user@[IPv6:::1]",                    // double colon edge
                "user@[IPv6:2001:db8:zzzz::1]",       // bad hex
                "user@domain.",                       // trailing dot
                "user@subdomain.local",               // mDNS reserved
                "user@例え.テスト",                     // unicode domain (IDN)
                "aurigaaristo@gmail.dom"
        );

        System.out.println("=== Email Validation Demo (DNSCheckValidation) ===");
        for (String email : samples) {
            try {
                boolean ok = validator.isValid(email, rule);
                System.out.println("\nEmail: " + email);
                System.out.println("Valid: " + ok);

                if (!ok && validator.getError() != null) {
                    InvalidEmail error = validator.getError();
                    System.out.println("Error : " + error.getReason().description());
                }

                if (validator.hasWarnings()) {
                    System.out.println("Warnings:");
                    for (Warning w : validator.getWarnings()) {
                        System.out.println(" - code=" + w.code() + " | " + w.message());
                    }
                }
            } catch (Exception ex) {
                System.out.println("\nEmail: " + email);
                System.out.println("Valid: false");
                System.out.println("Exception during validation: " + ex.getClass().getSimpleName() + " - " + ex.getMessage());
            }
        }

        System.out.println("\n=== Done ===");
    }
}
