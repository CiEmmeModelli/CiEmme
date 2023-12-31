/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010 Red Hat Inc. and/or its affiliates and other contributors
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
package org.jboss.arquillian.container.test.impl.client.deployment;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import org.jboss.arquillian.container.spi.client.deployment.DeploymentScenario;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.DeploymentConfiguration;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.container.test.spi.client.deployment.DeploymentScenarioGenerator;
import org.jboss.arquillian.test.spi.TestClass;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.descriptor.api.Descriptor;

/**
 * {@link DeploymentScenarioGenerator} that builds a {@link DeploymentScenario} based on
 * the standard Arquillian API annotations.
 *
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 * @version $Revision: $
 */
public class AnnotationDeploymentScenarioGenerator extends AbstractDeploymentScenarioGenerator implements DeploymentScenarioGenerator {
    private static String methodAnnotation="Method annotated with ";

    protected List<DeploymentConfiguration> generateDeploymentContent(TestClass testClass) {

        List<DeploymentConfiguration> deployments = new ArrayList<DeploymentConfiguration>();
        Method[] deploymentMethods = testClass.getMethods(Deployment.class);

        for (Method deploymentMethod : deploymentMethods) {
            validate(deploymentMethod);
            deployments.add(generateDeploymentContent(deploymentMethod));
        }

        return deployments;
    }

    private void validate(Method deploymentMethod) {
        if (!Modifier.isStatic(deploymentMethod.getModifiers())) {
            throw new IllegalArgumentException(
                methodAnnotation + Deployment.class.getName() + " is not static. " + deploymentMethod);
        }
        if (!Archive.class.isAssignableFrom(deploymentMethod.getReturnType()) && !Descriptor.class.isAssignableFrom(
            deploymentMethod.getReturnType())) {
            throw new IllegalArgumentException(
                methodAnnotation
                    + Deployment.class.getName()
                    +
                    " must have return type "
                    + Archive.class.getName()
                    + " or "
                    + Descriptor.class.getName()
                    + ". "
                    + deploymentMethod);
        }
        if (deploymentMethod.getParameterTypes().length != 0) {
            throw new IllegalArgumentException(methodAnnotation
                + Deployment.class.getName()
                + " can not accept parameters. "
                + deploymentMethod);
        }
    }

    /**
     * @param deploymentMethod
     * @return
     */
    private DeploymentConfiguration generateDeploymentContent(Method deploymentMethod) {

        Deployment deploymentAnnotation = deploymentMethod.getAnnotation(Deployment.class);
        DeploymentConfiguration.DeploymentContentBuilder deploymentContentBuilder = null;
        if (Archive.class.isAssignableFrom(deploymentMethod.getReturnType())) {
            deploymentContentBuilder = new DeploymentConfiguration.DeploymentContentBuilder(invoke(Archive.class, deploymentMethod));
        } else if (Descriptor.class.isAssignableFrom(deploymentMethod.getReturnType())) {
            deploymentContentBuilder = new DeploymentConfiguration.DeploymentContentBuilder(invoke(Descriptor.class, deploymentMethod));
        }
        if (deploymentMethod.isAnnotationPresent(OverProtocol.class)) {
            OverProtocol overProtocolAnnotation = deploymentMethod.getAnnotation(OverProtocol.class);
            if (overProtocolAnnotation != null && deploymentContentBuilder != null) {
                deploymentContentBuilder.withOverProtocol(overProtocolAnnotation.value());
            }
        }
        if (deploymentMethod.isAnnotationPresent(TargetsContainer.class)) {
            TargetsContainer targetsContainerAnnotation = deploymentMethod.getAnnotation(TargetsContainer.class);
            if (targetsContainerAnnotation != null && deploymentContentBuilder != null) {
                deploymentContentBuilder.withTargetsContainer(targetsContainerAnnotation.value());
            }
        }
        if (deploymentMethod.isAnnotationPresent(ShouldThrowException.class) && deploymentContentBuilder != null) {
            final ShouldThrowException shouldThrowException = deploymentMethod.getAnnotation(ShouldThrowException.class);
            deploymentContentBuilder.withShouldThrowException(shouldThrowException.value(), shouldThrowException.testable());
        }

       if(deploymentContentBuilder!=null){
        deploymentContentBuilder = deploymentContentBuilder.withDeployment()
            .withManaged(deploymentAnnotation.managed())
            .withName(deploymentAnnotation.name())
            .withOrder(deploymentAnnotation.order())
            .withTestable(deploymentAnnotation.testable())
            .build();

        
        return deploymentContentBuilder.get();}
        else return null;
    }


    /**
     * @param deploymentMethod
     * @return
     */
    private <T> T invoke(Class<T> type, Method deploymentMethod) {
        try {
            return type.cast(deploymentMethod.invoke(null));
        } catch (Exception e) {
            throw new DeplException("Could not invoke deployment method: " + deploymentMethod, e);
        }
    }

}
