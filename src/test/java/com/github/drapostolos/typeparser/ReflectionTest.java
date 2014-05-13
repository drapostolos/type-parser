package com.github.drapostolos.typeparser;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ReflectionTest extends TestBase {

    @Test
    public void canParseTypeArgumentsAndCallMethodThroughReflection() throws Exception {
        // given
        Method m = getMethod("m");
        String[] strArgs = { "1, 2, 3", "a,b,c" };
        List<Object> args = new ArrayList<Object>();
        Type[] types = m.getGenericParameterTypes();
        for (int i = 0; i < types.length; i++) {
            args.add(parser.parseType(strArgs[i], types[i]));
        }

        // when
        m.invoke(this, args.toArray());

    }

    private Method getMethod(String methodName) throws Exception {
        for (Method m : getClass().getMethods()) {
            if (m.getName().equals(methodName)) {
                return m;
            }
        }
        throw new AssertionError("Method NOT found: " + methodName);
    }

    public void m(List<Integer> numbers, Character[] letters) {
        assertThat(numbers).containsOnly(1, 2, 3);
        assertThat(letters).containsOnly('a', 'b', 'c');
    }

}
