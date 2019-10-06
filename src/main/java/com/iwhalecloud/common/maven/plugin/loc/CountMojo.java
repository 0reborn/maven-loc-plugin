package com.iwhalecloud.common.maven.plugin.loc;

import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    private static final String[] DEFAULT_INCLUDE_FILE_TYPE = {"java", "xml"};

    @Parameter(defaultValue = "${project.basedir}")
    private File basedir;

    @Parameter(defaultValue = "${project.build.sourceDirectory}")
    private File sourceDirectory;

    @Parameter(defaultValue = "${project.build.testSourceDirectory}")
    private File testSourceDirectory;

    @Parameter(defaultValue = "${project.build.resources}")
    private List<Resource> resources;

    @Parameter(defaultValue = "${project.build.testResources}")
    private List<Resource> testResources;

    @Parameter
    private String[] includes;

    @Override
    public void execute() throws MojoExecutionException {
        if (includes == null || includes.length == 0) {
            includes = DEFAULT_INCLUDE_FILE_TYPE;
        }
        try {
            countDir(sourceDirectory);
            countDir(testSourceDirectory);
            for (Resource resource : resources) {
                countDir(new File(resource.getDirectory()));
            }
            for (Resource testResource: testResources) {
                countDir(new File(testResource.getDirectory()));
            }
        } catch (IOException e) {
            throw new MojoExecutionException("Unable to count lines of code.", e);
        }
    }

    private void countDir(File dir) throws IOException {
        if (!dir.exists()) {
            return;
        }
        List<File> collected = new ArrayList<>();
        collectFiles(collected, dir);
        int lines = 0;
        for (File file : collected) {
            lines += countLine(file);
        }
        String path = dir.getAbsolutePath().substring(basedir.getAbsolutePath().length());
        getLog().info(path + ": " + lines + " lines of code in " + collected.size() + " files");
    }

    /**
     * 递归收集所有符合类型要求的文件
     * @param collected 待收集list
     * @param file 文件or文件夹
     * @return void
     * @author wu.yue
     * @date 2019/10/6 20:37
     */
    private void collectFiles(List<File> collected, File file) {
        if (file.isFile()) {
            for (String fileType : includes) {
                if (file.getName().endsWith("." + fileType)) {
                    collected.add(file);
                    break;
                }
            }
        } else {
            for (File subFile : file.listFiles()) {
                collectFiles(collected, subFile);
            }
        }
    }

    /**
     * 统计单个文件的行数
     * @param file 文件
     * @return int 行数
     * @author wu.yue
     * @date 2019/10/6 20:31
     */
    private int countLine(File file) throws IOException {
        int line = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.ready()) {
                reader.readLine();
                line++;
            }
        }
        return line;
    }
}