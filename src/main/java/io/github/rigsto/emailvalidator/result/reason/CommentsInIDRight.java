package io.github.rigsto.emailvalidator.result.reason;

/**
 * Reason for comments found in IDRight part of message-id.
 * <p>
 * This reason is generated when comments are found in the IDRight part
 * of a message-id, which is not allowed according to email standards.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class CommentsInIDRight implements Reason {

    /**
     * Returns the unique code for comments in IDRight reasons.
     * 
     * @return the reason code 400
     */
    @Override
    public int code() {
        return 400;
    }

    /**
     * Returns a description of the comments in IDRight reason.
     * 
     * @return the reason description
     */
    @Override
    public String description() {
        return "Comments are not allowed in IDRight for message-id";
    }
}
