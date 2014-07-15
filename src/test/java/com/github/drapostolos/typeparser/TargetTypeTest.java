package com.github.drapostolos.typeparser;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class TargetTypeTest extends TestBase {

    @Test
    public void testToString() throws Exception {
        TargetType type = new TargetType(String.class);
        assertThat(type.toString()).isEqualTo(String.class.toString());
    }

}
