package com.annotation.processor;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
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
            String parentClassName = className.substring(className.lastIndexOf('.') + 1, className.length());
            String packageName = className.substring(0, className.lastIndexOf('.'));

            VelocityContext context = new VelocityContext();
            context.put("packageName", packageName);
            context.put("parentClassName", parentClassName);

            ArrayList<HashMap> fields = new ArrayList<>();

            for (Element element : roundEnv.getElementsAnnotatedWith(BindViewValidator.class)) {
                BindViewValidator antn = element.getAnnotation(BindViewValidator.class);
                String targetClassName = element.getEnclosingElement().asType().toString();

                if (!className.equals(targetClassName)) continue;

                HashMap<String, Object> params = new HashMap<>();
                params.put("className", ((DeclaredType) element.asType()).asElement());
                params.put("viewResId", antn.view());
                params.put("type", antn.type());
                if (!antn.related().isEmpty())
                    params.put("related", antn.related());
                if (!antn.errorMessage().isEmpty())
                    params.put("errorMessage", antn.errorMessage());
                if (!antn.field().isEmpty())
                    params.put("field", antn.field());
                if (!antn.pattern().isEmpty())
                    params.put("pattern", antn.pattern().replace("\\", "\\\\"));

                fields.add(params);
            }

            context.put("fields", fields);

            try {
                Properties props = new Properties();
                props.put("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.SystemLogChute");
                props.put("resource.loader", "classpath");
                props.put("classpath.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

                VelocityEngine ve = new VelocityEngine(props);
                ve.init();

                Template vt = ve.getTemplate("Binder.tpl");

                JavaFileObject source = processingEnv.getFiler().createSourceFile(className + "_BindValidator");
                Writer writer = source.openWriter();

                vt.merge(context, writer);
                writer.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return true;
    }
}
