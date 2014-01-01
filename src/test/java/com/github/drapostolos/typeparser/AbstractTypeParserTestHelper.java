package com.github.drapostolos.typeparser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.fest.assertions.api.Assertions;

import com.github.drapostolos.typeparser.StringToTypeParser;

abstract class AbstractTypeParserTestHelper {
    static final String NUMBER_FORMAT_ERROR_MSG = "Number format exception For input string: \"%s\".";
	private String value;
	private List<Class<?>> types = new ArrayList<Class<?>>();
	private Class<?> expectedExceptionType;
	private StringToTypeParser parser = StringToTypeParser.newBuilder().build();

	/*
	 * Mandatory to pass at least one type.
	 */
	public AbstractTypeParserTestHelper(Class<?> type, Class<?>... types) {
	    this.types.add(type);
	    this.types.addAll(Arrays.asList(types));
    }
	
	final AbstractTypeParserTestHelper assertThat(String value){
	    this.value = value;
	    return this;
	}

    final void isParsedTo(Object expected){
        for(Class<?> type : types){
            Object actual = parser.parse(value, type);
            Assertions.assertThat(actual).isEqualTo(expected);
        }
    }

    final AbstractTypeParserTestHelper throwsIllegalArgumentException() {
        this.expectedExceptionType = IllegalArgumentException.class;
        return this;
    }

    public void whereMessageEndsWih(String message, Object... args) {
        message = String.format(message, args);
        for(Class<?> type : types){
            String errorMsg = "StringToTypeParser.parseType(\"%s\", %s.class) is expected to throw "
                    + "Exception: <%s> where message ends with: <%s>.%n";
            errorMsg = String.format(errorMsg, value, type.getSimpleName(), expectedExceptionType.getName(), message);
            try {
                parser.parse(value, type);
                errorMsg.concat("But no exception was thrown.");
                Assert.fail(errorMsg);
            } catch (Exception e) {
                Assertions.assertThat(e)
                .overridingErrorMessage(errorMsg.concat("But got exception: <%s> instead."), expectedExceptionType.getName())
                .isInstanceOf(expectedExceptionType);
                Assertions.assertThat(e)
                .overridingErrorMessage(errorMsg.concat("But got exception message: <%s>."), e.getMessage())
                .hasMessageEndingWith(message);
            }
        }
        
    }
    
}
