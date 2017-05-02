package com.annotation.processor;

import com.validator.ValidatorType;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes("com.annotation.processor.BindViewValidator")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class ValidatorProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        ArrayList<String> classList = new ArrayList<>();
        for (Element element : roundEnv.getElementsAnnotatedWith(BindViewValidator.class)) {
            String fullClassName = element.getEnclosingElement().asType().toString();
            if (!classList.contains(fullClassName))
                classList.add(fullClassName);
        }

        for (String className : classList) {
            StringBuilder builder = new StringBuilder();
            String parentClassName = className.substring(className.lastIndexOf('.') + 1, className.length());
            String packageName = className.substring(0, className.lastIndexOf('.'));

            builder.append("package " + packageName + ";\n" +
                    "\n" +
                    "import android.support.design.widget.TextInputLayout;\n" +
                    "import com.validator.*;\n" +
                    "import com.validator.bind.IBindValidator;\n" +
                    "import java.util.ArrayList;\n" +
                    "import java.util.Map;\n"
            );

            builder.append("public final class " + parentClassName + "_BindValidator<T extends " + parentClassName + "> implements IBindValidator{\n\n" +
                    "private T view;\n" +
                    "private ArrayList<FieldExtension> fields = new ArrayList<>();\n" +
                    "public " + parentClassName + "_BindValidator(Object target) {\n" +
                    "view = (T) target;\n");

            for (Element element : roundEnv.getElementsAnnotatedWith(BindViewValidator.class)) {
                BindViewValidator antn = element.getAnnotation(BindViewValidator.class);
                String targetClassName = element.getEnclosingElement().asType().toString();
                if (!className.equals(targetClassName)) continue;

                String textView = element.getSimpleName().toString();

                String fieldType = null;
                if (antn.type().equals(ValidatorType.PASSWORD))
                    fieldType = "String[]";
                else if (antn.type().equals(ValidatorType.CHECK))
                    fieldType = "Boolean";
                else
                    fieldType = "String";
                if (antn.type().equals(ValidatorType.CUSTOM))
                    builder.append("fields.add(new FieldExtension<String>(new " + antn.type()
                            + "(view." + textView + ".getId(), \"" + antn.pattern().replace("\\", "\\\\") + "\")" + (antn.field().isEmpty() ? "" : ", \"" + antn.field() + "\"")
                            + ") {\n" + "   @Override\n");
                else
                    builder.append("fields.add(new FieldExtension<" + fieldType + ">(new " + antn.type()
                            + "(view." + textView + ".getId())" + (antn.field().isEmpty() ? "" : ", \"" + antn.field() + "\"")
                            + ") {\n" + "   @Override\n");
                if (antn.type().equals(ValidatorType.PASSWORD))
                    builder.append("   public " + fieldType + " getValue() {\n" +
                            "       return new String[] { view." + textView + ".getText().toString()" +
                            (antn.related().isEmpty() ? "" : ", view." + antn.related() + ".getText().toString()") + "};\n");
                else if (antn.type().equals(ValidatorType.CHECK))
                    builder.append("   public " + fieldType + " getValue() {\n" +
                            "       return view." + textView + ".isChecked();\n");
                else
                    builder.append("   public " + fieldType + " getValue() {\n" +
                            "       return view." + textView + ".getText().toString();\n");
                builder.append("   }\n" +

                        "   @Override\n" +
                        "   public void setError(String message) {\n");
                if (antn.hasLayout())
                    builder.append("       ((TextInputLayout) view." + textView + ".getParent().getParent()).setError(message);\n}\n");
                else
                    builder.append("       view." + textView + ".setError(message);\n}\n");

                if (antn.hasLayout())
                    builder.append("@Override\n" +
                            "public boolean isActive() {\n" +
                            "   return  ((TextInputLayout) view." + textView + ".getParent().getParent()).getVisibility() == 0 && active;\n" +
                            "}");
                else
                    builder.append("@Override\n" +
                            "public boolean isActive() {\n" +
                            "   return  view." + textView + ".getVisibility() == 0 && active;\n" +
                            "}");
                builder.append("});\n\n");

            }
            builder.append("\n}\n\n");

            builder.append("public ValidatorGroup getValidator() {\n" +
                    "        ValidatorGroup validator = new ValidatorGroup();\n" +
                    "        for (FieldExtension field : fields) {\n" +
                    "            field.setError(null);\n" +
                    "            if (!field.isActive()) continue;\n" +
                    "            validator.add(field.getValidator());\n" +
                    "        }\n" +
                    "        \n" +
                    "        return validator;\n" +
                    "    }\n");

            builder.append("public void setErrors(Map<String, String> errors) {\n" +
                    "        for (Map.Entry<String, String> entry : errors.entrySet()) {\n" +
                    "            for (FieldExtension field : fields) {\n" +
                    "                if (field.equals(entry.getKey())) {\n" +
                    "                    field.setError(entry.getValue());\n" +
                    "                    break;\n" +
                    "                }\n" +
                    "            }\n" +
                    "        }\n" +
                    "}");

            builder.append("public void enableAll() {\n" +
                    "        for (FieldExtension field : fields) {\n" +
                    "            field.active = true;\n" +
                    "        }\n" +
                    "    }\n" +
                    "\n" +
                    "    public void disable(int[] ids) {\n" +
                    "        for (int id : ids) {\n" +
                    "            for (FieldExtension field : fields) {\n" +
                    "                if (field.equals(String.valueOf(id))) {\n" +
                    "                    field.active = false;\n" +
                    "                    break;\n" +
                    "                }\n" +
                    "            }\n" +
                    "        }\n" +
                    "    }");

            builder.append("\n\nclass FieldExtension<T> {\n" +
                    "        private final String field;\n" +
                    "        protected boolean active = true;\n" +
                    "        private final IValidator validator;\n" +
                    "\n" +
                    "        public FieldExtension(IValidator validator) {\n" +
                    "            this(validator, null);\n" +
                    "        }\n" +
                    "\n" +
                    "        public FieldExtension(IValidator validator, String field) {\n" +
                    "            this.field = field;\n" +
                    "            this.validator = validator;\n" +
                    "        }\n" +
                    "\n" +
                    "        @Override\n" +
                    "        public boolean equals(Object obj) {\n" +
                    "            return String.valueOf(validator.getId()).equals(obj) \n" +
                    "                    || field != null && field.equals(obj);\n" +
                    "        }\n" +
                    "\n" +
                    "        public T getValue() {\n" +
                    "            return null;\n" +
                    "        }\n" +
                    "\n" +
                    "        public void setError(String message) {\n" +
                    "        }\n" +
                    "\n" +
                    "        public IValidator getValidator() {\n" +
                    "            validator.setValue(getValue());\n" +
                    "            return validator;\n" +
                    "        }\n" +
                    "        public boolean isActive() {\n" +
                    "            return active;\n" +
                    "        }" +
                    "" +
                    "}");
            builder.append("}");
            try {
                JavaFileObject source = processingEnv.getFiler().createSourceFile(className + "_BindValidator");

                Writer writer = source.openWriter();
                writer.write(builder.toString());
                writer.flush();
                writer.close();
            } catch (IOException e) {
            }
        }

        return true;
    }
}
