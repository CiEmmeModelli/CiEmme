/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009 Red Hat Inc. and/or its affiliates and other contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.container.impl.client.deployment;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import org.jboss.arquillian.config.descriptor.api.ArquillianDescriptor;
import org.jboss.arquillian.config.descriptor.api.EngineDef;
import org.jboss.arquillian.container.spi.client.deployment.DeploymentDescription;
import org.jboss.arquillian.container.spi.event.container.BeforeDeploy;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.exporter.ExplodedExporter;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;

/**
 * Handler that will export the generated {@link Archive} to the file system. <br/>
 * Used for debugging the deployment.
 *
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 * @version $Revision: $
 */
public class ArchiveDeploymentExporter {
    private static final Logger log = Logger.getLogger(ArchiveDeploymentExporter.class.getName());

    @Inject
    private Instance<ArquillianDescriptor> configuration;

    private void exportDeployment(File exportDir, Archive<?> deployment, String exportFileName, boolean exportExploded) {
        final File fileToExport = new File(exportDir, exportFileName);
        try {
            deleteIfExists(fileToExport);
        } catch (IOException e) {
            e.printStackTrace();
        }
    
       /* if (exportExploded) {
            deployment.as(ExplodedExporter.class).exportExploded(
                exportDir, exportFileName);
        } else {
            deployment.as(ZipExporter.class).exportTo(fileToExport, true);
        }*/ 

        if (exportExploded) {
            deployment.as(ExplodedExporter.class).exportExploded(exportDir, exportFileName);
        } else {
            deployment.as(ZipExporter.class).exportTo(fileToExport, true);
        }
        
    }

    private String getExportPath(EngineDef engine) {
        String systemExport = SecurityActions.getProperty("arquillian.deploymentExportPath");
        return (systemExport == null || systemExport.length() == 0) ? engine.getDeploymentExportPath() : systemExport;
    }
    
    public void callback(@Observes BeforeDeploy event) throws ExpException {
        ArquillianDescriptor descriptor = configuration.get();
        if (descriptor == null) {
            return;
        }
        EngineDef engine = descriptor.engine();

        String systemExport = SecurityActions.getProperty("arquillian.deploymentExportPath");
        String systemExportExploded = SecurityActions.getProperty("arquillian.deploymentExportExploded");
        String exportPath = getExportPath(engine);
        Boolean exportExploded =
            (systemExportExploded == null || systemExportExploded.length() == 0) ? engine.getDeploymentExportExploded()
                : Boolean.parseBoolean(systemExport);

        if (exportPath != null && event.getDeployment().isArchiveDeployment()) {
            File exportDir = new File(exportPath);
            if (exportDir.isFile()) {
                log.warning(String.format("Deployment export disabled. Export path points to an existing file: %s", exportPath));

                return;
            } else if (!exportDir.isDirectory() && !exportDir.mkdirs()) {
                log.warning(String.format("Deployment export directory could not be created:  %s", exportPath));
                return;
            }

            Archive<?> deployment;
            if (event.getDeployment().testable()) {
                deployment = event.getDeployment().getTestableArchive();
            } else {
                deployment = event.getDeployment().getArchive();
            }
            exportDeployment(exportDir, deployment, createFileName(event.getDeployment(), deployment), exportExploded);
        }
    }

    private String createFileName(DeploymentDescription deployment, Archive<?> archive) {
        // TODO: where do we get TestClass name from ?
        return deployment.getTarget().getName() + "_" + deployment.getName() + "_" + archive.getName();
    }

    private void deleteIfExists(File file) throws IOException {
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            for (File sub : file.listFiles()) {
                deleteIfExists(sub);
            }
        }

        if (!file.delete()) {
            throw new IOException("Failed to delete file: " + file);
        }
    }
}
