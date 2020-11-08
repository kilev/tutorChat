package com.kil.tutor;

public class TutorChatException extends RuntimeException {
    public TutorChatException(String message) {
        super(message);
    }

    public TutorChatException(String message, Throwable cause) {
        super(message, cause);
    }
}
