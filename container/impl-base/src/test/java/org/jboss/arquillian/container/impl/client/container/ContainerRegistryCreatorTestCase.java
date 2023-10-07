/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
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
package org.jboss.arquillian.container.impl.client.container;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import org.jboss.arquillian.config.descriptor.api.ArquillianDescriptor;
import org.jboss.arquillian.container.spi.ContainerRegistry;
import org.jboss.arquillian.container.spi.client.container.DeployableContainer;
import org.jboss.arquillian.container.test.AbstractContainerTestBase;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.spi.ServiceLoader;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * ContainerRegistryCreatorTestCase
 *
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 * @version $Revision: $
 */
@RunWith(MockitoJUnitRunner.class)
public class ContainerRegistryCreatorTestCase extends AbstractContainerTestBase {
    private static final String CONTAINER_1 = "container_1";
    private static final String CONTAINER_2 = "container_2";
    private static final String GROUP_1 = "group_1";
    private static final String GROUP_2 = "group_2";

    @Inject
    private Instance<ContainerRegistry> regInst;

    @Mock
    private ServiceLoader serviceLoader;

    @Mock
    private DeployableContainer<?> deployableContainer;

    @Before
    public void addServiceLoader() {
        Mockito.when(serviceLoader.onlyOne(DeployableContainer.class)).thenReturn(deployableContainer);
        bind(ApplicationScoped.class, ServiceLoader.class, serviceLoader);
    }

    @Override
    protected void addExtensions(List<Class<?>> extensions) {
        extensions.add(ContainerRegistryCreator.class);
    }

    @Test
    public void shouldRegisterDefaultContainer() {
        fire(
            Descriptors.create(ArquillianDescriptor.class)
                .container(CONTAINER_1)); // not set as default

        verifyRegistry("default");
    }

    @Test
    public void shouldRegisterContainerMarkedDefault() {
        fire(
            Descriptors.create(ArquillianDescriptor.class)
                .container(CONTAINER_1)
                .setDefault());

        verifyRegistry(CONTAINER_1);
    }

    @Test
    public void shouldRegisterContainerMarkedDefaultWhenMultipleDefined() {
        fire(
            Descriptors.create(ArquillianDescriptor.class)
                .container(CONTAINER_1)
                .setDefault()
                .container(CONTAINER_2));

        verifyRegistry(CONTAINER_1);
    }

    @Test
    public void shouldRegisterGroupMarkedDefault() {
        fire(
            Descriptors.create(ArquillianDescriptor.class)
                .group(GROUP_1)
                .setGroupDefault()
                .container(CONTAINER_1));

        verifyRegistry(CONTAINER_1);
    }

    @Test
    public void shouldRegisterContainerDefinedBySystemProperty() {
        System.setProperty(ContainerRegistryCreator.ARQUILLIAN_LAUNCH_PROPERTY, CONTAINER_1);
        try {
            fire(
                Descriptors.create(ArquillianDescriptor.class)
                    .container(CONTAINER_1)
                    .group(GROUP_1)
                    .setGroupDefault()
                    .container(CONTAINER_2));

            verifyRegistry(CONTAINER_1);
        } finally {
            System.setProperty(ContainerRegistryCreator.ARQUILLIAN_LAUNCH_PROPERTY, "");
        }
    }

    @Test
    public void shouldRegisterGroupDefinedBySystemProperty() {
        System.setProperty(ContainerRegistryCreator.ARQUILLIAN_LAUNCH_PROPERTY, GROUP_1);
        try {
            fire(
                Descriptors.create(ArquillianDescriptor.class)
                    .container(CONTAINER_1)
                    .setDefault()
                    .group(GROUP_1)
                    .container(CONTAINER_2));

            verifyRegistry(CONTAINER_2);
        } finally {
            System.setProperty(ContainerRegistryCreator.ARQUILLIAN_LAUNCH_PROPERTY, "");
        }
    }

    @SuppressWarnings("java:S5778")
    @Test
    public void shouldThrowExceptionIfMultipleContainersSetAsDefault() {
        try {
            fire(
                Descriptors.create(ArquillianDescriptor.class)
                    .container(CONTAINER_1)
                    .setDefault()
                    .container(CONTAINER_2)
                    .setDefault()
            );
            fail("Expected IllegalStateException was not thrown");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().startsWith("Multiple Containers defined as default"));
        }
    }

    @SuppressWarnings("java:S5778")
    @Test
    public void shouldThrowExceptionIfMultipleGroupsSetAsDefault() throws IllegalStateException {
        try {
            fire(
                Descriptors.create(ArquillianDescriptor.class)
                    .group(GROUP_1)
                    .setGroupDefault()
                    .group(GROUP_2)
                    .setGroupDefault());
            fail("IllegalStateException Expected");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().startsWith("Multiple Groups defined as default"));
            throw e;
        }
    }

    @SuppressWarnings("java:S5778")
    @Test
    public void shouldThrowExceptionIfMultipleGroupsOrContainersSetAsDefault() throws IllegalStateException {
        try {
            fire(
                Descriptors.create(ArquillianDescriptor.class)
                    .container(CONTAINER_1)
                    .setDefault()
                    .group(GROUP_1)
                    .setGroupDefault());
            fail("IllegalStateException");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().startsWith("Multiple Containers/Groups defined as default"));
            throw e;
        }
    }

    @SuppressWarnings("java:S5778")
    @Test
    public void shouldThrowExceptionIfMultipleContainersInGroupSetAsDefault() throws IllegalStateException {
        try {
            fire(Descriptors.create(ArquillianDescriptor.class)
                .group(GROUP_1)
                .container(CONTAINER_1)
                .setDefault()
                .container(CONTAINER_2)
                .setDefault());
            fail("IllegalStateException");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().startsWith("Multiple Containers within Group defined as default"));
            throw e;
        }
    }

    /*
     *  ARQ-619, multiple DeployableContainer on classpath is not currently allowed, but not reported.
     */
    @SuppressWarnings("java:S5778")
    @Test
    public void shouldThrowExceptionIfMultipleDeployableContainersFoundOnClassapth() {
        Mockito.when(serviceLoader.onlyOne(DeployableContainer.class))
            .thenThrow(new IllegalStateException("Multiple service implementations found for ..."));

        try {
            fire(Descriptors.create(ArquillianDescriptor.class));
            fail("Failed");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().startsWith("Could not add a default container"));
            throw e;
        }
    }

    @SuppressWarnings("java:S5778")
    @Test
    public void shouldThrowExceptionIfFailedToCreateDefaultDeployableContainerInstance() {
        Mockito.when(serviceLoader.onlyOne(DeployableContainer.class))
            .thenThrow(new RuntimeException("Class yatta yatta not found..."));

        try {
            fire(Descriptors.create(ArquillianDescriptor.class));
            fail("Failed");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().startsWith("Could not create the default"));
            throw e;
        }
    }
    
    private void verifyRegistry(String... containerNames) {
        ContainerRegistry registry = regInst.get();
    
        verifyContainersRegistered(registry);
        verifyContainerCount(registry, containerNames.length);
        verifyCorrectContainersRegistered(registry, containerNames);
    }
    
    private void verifyContainersRegistered(ContainerRegistry registry) {
        Assert.assertNotNull("Verify Containers registered", registry.getContainers());
    }
    
    private void verifyContainerCount(ContainerRegistry registry, int expectedCount) {
        Assert.assertEquals("Verify " + expectedCount + " Container(s) registered", expectedCount, registry.getContainers().size());
    }
    
    private void verifyCorrectContainersRegistered(ContainerRegistry registry, String... containerNames) {
        for (int i = 0; i < containerNames.length; i++) {
            String containerName = containerNames[i];
            Assert.assertEquals("Verify correct Container registered", containerName, registry.getContainers().get(i).getName());
        }
    }
    
}