package com.pedroluizforlan.rectiveflashcards.domain.exception;


import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;


import java.text.MessageFormat;
import java.util.ResourceBundle;

@RequiredArgsConstructor
public class BaseErrorMessage {
    private final String DEFAUL_RESOURCE = "message";

    public static final BaseErrorMessage GENERIC_EXCEPTION = new BaseErrorMessage("generic");
    public static final BaseErrorMessage GENERIC_NOT_FOUND = new BaseErrorMessage("generic.notFound");
    public static final BaseErrorMessage GENERIC_METHOD_NOT_ALLOW = new BaseErrorMessage("generic.methodNotAllow");


    private final String key;
    private String[] params;

    public BaseErrorMessage params(final String... params){
        this.params = ArrayUtils.clone(params);
        return this;
    }

    public String getMessage(){
        var message = tryGetMessageFromBundle();
        if(ArrayUtils.isNotEmpty(params)){
            final var fmt = new MessageFormat(message);
            message = fmt.format(params);
        }
        return message;
    }
    private String tryGetMessageFromBundle(){
        return getResource().getString(key);
    }

    public ResourceBundle getResource(){
        return ResourceBundle.getBundle(DEFAUL_RESOURCE);
    }
}
