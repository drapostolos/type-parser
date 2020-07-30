package com.github.drapostolos.typeparser;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;

import org.junit.Test;

public class PropertyEditorTest extends TestBase {

    @Test
    public void canSkipPropertyEditor() throws Exception {
        //given
        parser = builder.enablePropertyEditor().build();

        //then
        shouldThrow(NoSuchRegisteredParserException.class)
                .whenParsing(DUMMY_STRING)
                .to(PropertyEditorTest.class);

    }

    @Test
    public void canUsePropertyEditor() throws Exception {
        //given
        PropertyEditorManager.registerEditor(TypeToUseWithPropertyEditor.class, MyPropertyEditor.class);

        //when
        parser = builder.enablePropertyEditor().build();
        Object o = parser.parse("alex", TypeToUseWithPropertyEditor.class);

        //then
        assertThat(o).isInstanceOf(TypeToUseWithPropertyEditor.class);
    }

    public static class TypeToUseWithPropertyEditor {

        public TypeToUseWithPropertyEditor(int i, String text) {
            // dummy constructor
        }

    }

    public static class MyPropertyEditor implements PropertyEditor {

        private String text;

        @Override
        public void setValue(Object value) {}

        @Override
        public Object getValue() {
            return new TypeToUseWithPropertyEditor(1, text);
        }

        @Override
        public boolean isPaintable() {
            return false;
        }

        @Override
        public void paintValue(Graphics gfx, Rectangle box) {
        }

        @Override
        public String getJavaInitializationString() {
            return null;
        }

        @Override
        public String getAsText() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            this.text = text;
        }

        @Override
        public String[] getTags() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Component getCustomEditor() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public boolean supportsCustomEditor() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener listener) {
            // TODO Auto-generated method stub

        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener listener) {
            // TODO Auto-generated method stub

        }

    }

}
