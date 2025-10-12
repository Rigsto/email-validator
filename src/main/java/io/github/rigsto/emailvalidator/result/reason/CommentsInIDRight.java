package io.github.rigsto.emailvalidator.result.reason;

public class CommentsInIDRight implements Reason {

    @Override
    public int code() {
        return 400;
    }

    @Override
    public String description() {
        return "Comments are not allowed in IDRight for message-id";
    }
}
