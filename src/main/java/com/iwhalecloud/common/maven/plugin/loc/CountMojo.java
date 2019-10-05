package com.iwhalecloud.common.maven.plugin.loc;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.lang.reflect.Field;

/**
 * Copyright: Copyright(c) 2019 iwhalecloud
 * <p>
 * 类说明：maven-loc-plugin
 * <p>
 * 类名称: CountMojo.java
 *
 * @author wu.yue
 * @version v1.0.0
 * @date 2019/10/5 19:41
 * <p>
 * Modification History:
 * Date         Author          Version            Description
 * ------------------------------------------------------------
 */
@Mojo(name = "loc", defaultPhase = LifecyclePhase.COMPILE)
public class CountMojo extends AbstractMojo {

    private static final String[] FILE_TYPE_INCLUDES = {"java", "xml"};

    @Parameter(defaultValue = "${project.basedir}")
    private File basedir;

    @Parameter(defaultValue = "${project.build.sourceDirectory}")
    private File sourceDirectory;

    @Override
    public void execute() throws MojoExecutionException {
        getLog().info("basedir: " + basedir.toString());
        getLog().info("sourceDirectory: " + sourceDirectory.toString());
//        Field
    }
}