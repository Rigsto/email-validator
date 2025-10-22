# Email Validator

A library for validating emails against several RFC.

## Supported RFCs
This library aims to support RFC:

* [5321](https://tools.ietf.org/html/rfc5321),
* [5322](https://tools.ietf.org/html/rfc5322),
* [6530](https://tools.ietf.org/html/rfc6530),
* [6531](https://tools.ietf.org/html/rfc6531),
* [6532](https://tools.ietf.org/html/rfc6532),
* [1035](https://tools.ietf.org/html/rfc1035) 

## Supported Versions
| Version  | Released    | EOL |
|----------|-------------|-----|
| **v1.x** | 21-Oct-2025 | -   |

## Getting Started

`EmailValidator` requires you to decide which (or combination of them)
validation/s strategy/ies you'd like to follow for each
[validation](#available-validations).

A basic example with the RFC validation

```java
import io.github.rigsto.emailvalidator.EmailValidator;
import io.github.rigsto.emailvalidator.validation.RFCValidation;

public class Example {
    public static void main(String[] args) {
        EmailValidator validator = new EmailValidator();
        boolean isValid = validator.isValid("example@example.com", new RFCValidation()); // true
    }
}
```

### Available validations

1. [RFCValidation](/src/main/java/io/github/rigsto/emailvalidator/validation/RFCValidation.java): Standard RFC-like email validation.
2. [NoRFCWarningsValidation](/src/main/java/io/github/rigsto/emailvalidator/validation/NoRFCWarningsValidation.java):
   RFC-like validation that will fail when warnings* are found.
3. [DNSCheckValidation](/src/main/java/io/github/rigsto/emailvalidator/validation/DNSCheckValidation.java):
   Will check if there are DNS records that signal that the server accepts emails. This does not entail that the email exists.
4. [MultipleValidationWithAnd](/src/main/java/io/github/rigsto/emailvalidator/validation/MultipleValidationWithAnd.java):
   It is a validation that operates over other validations performing a logical and (&&) over the result of each validation.
5. [MessageIDValidation](/src/main/java/io/github/rigsto/emailvalidator/validation/MessageIDValidation.java):
   Follows [RFC2822 for message-id](https://tools.ietf.org/html/rfc2822#section-3.6.4) to validate that field, that has some differences in the domain part.
6. [Your own validation](#how-to-extend): You can extend the library behaviour
   by implementing your own validations.

*warnings: Warnings are deviations from the RFC that in a broader interpretation
are accepted.

```java
import io.github.rigsto.emailvalidator.EmailValidator;
import io.github.rigsto.emailvalidator.validation.DNSCheckValidation;
import io.github.rigsto.emailvalidator.validation.MultipleValidationWithAnd;
import io.github.rigsto.emailvalidator.validation.RFCValidation;

import java.util.List;

public class Example {
    public static void main(String[] args) {
        EmailValidator validator = new EmailValidator();
        MultipleValidationWithAnd validations = new MultipleValidationWithAnd(
           List.of(
                new RFCValidation(),
                new DNSCheckValidation()
           )
        );
        
        // google.com has MX records signaling a server with email capabilities
        boolean isValid = validator.isValid("admin@google.com", validations); // true
    }
}
```

#### Additional Validations

Validations not present in the RFCs

1. [SpoofCheckValidation](/src/main/java/io/github/rigsto/emailvalidator/validation/extra/SpoofCheckValidation.java):
   Will check for multi-utf-8 chars that can signal an erroneous email name.

### How to extend

It's easy! You just need to implement
[EmailValidation](/src/main/java/io/github/rigsto/emailvalidator/validation/EmailValidation.java) and you can use your own
validation.

## License

Released under the MIT License attached with this code.