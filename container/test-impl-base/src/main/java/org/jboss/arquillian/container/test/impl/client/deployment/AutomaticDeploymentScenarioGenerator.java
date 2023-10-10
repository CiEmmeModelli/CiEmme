package org.jboss.arquillian.container.test.impl.client.deployment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import org.jboss.arquillian.container.spi.client.deployment.DeploymentScenario;
import org.jboss.arquillian.container.test.api.BeforeDeployment;
import org.jboss.arquillian.container.test.api.DeploymentConfiguration;
import org.jboss.arquillian.container.test.spi.client.deployment.AutomaticDeployment;
import org.jboss.arquillian.container.test.spi.client.deployment.DeploymentScenarioGenerator;
import org.jboss.arquillian.container.test.spi.util.ServiceLoader;
import org.jboss.arquillian.test.spi.TestClass;
import org.jboss.shrinkwrap.api.Archive;

/**
 * {@link DeploymentScenarioGenerator} that builds a {@link DeploymentScenario} based on
 * the {@link AutomaticDeployment} registered services.
 *
 * @version $Revision: $
 */
public class AutomaticDeploymentScenarioGenerator extends AbstractDeploymentScenarioGenerator implements DeploymentScenarioGenerator {

    AutomaticDeploymentLocator automaticDeploymentLocator;
    private static String stringP1 = "Method annotated with ";

    public AutomaticDeploymentScenarioGenerator() {
        automaticDeploymentLocator = new AutomaticDeploymentLocator() {
            public ServiceLoader<AutomaticDeployment> find() {
                return ServiceLoader.load(AutomaticDeployment.class);
            }
        };
    }

    protected List<DeploymentConfiguration> generateDeploymentContent(TestClass testClass) {

        final List<DeploymentConfiguration> deploymentConfigurations = new ArrayList<DeploymentConfiguration>();
        final ServiceLoader<AutomaticDeployment> deploymentSpis = automaticDeploymentLocator.find();

        for (AutomaticDeployment deploymentSpi : deploymentSpis) {
            final DeploymentConfiguration deploymentConfiguration =
                deploymentSpi.generateDeploymentScenario(testClass);

            if (deploymentConfiguration != null) {
                deploymentConfigurations.add(deploymentConfiguration);
            }
        }

        return deploymentConfigurations;
    }

    @SuppressWarnings("java:S3740")
    protected Archive manipulateArchive(TestClass testClass, String deploymentName, Archive archive) {
        final Method[] beforeDeploymentMethods = testClass.getMethods(BeforeDeployment.class);

        for (Method beforeDeploymentMethod : beforeDeploymentMethods) {
            final BeforeDeployment beforeDeploymentMethodAnnotation = beforeDeploymentMethod.getAnnotation(BeforeDeployment.class);

            if (beforeDeploymentMethodAnnotation.name().equals(deploymentName)) {
                validate(beforeDeploymentMethod);
                archive = invoke(Archive.class, beforeDeploymentMethod, archive);
                break;
            }
        }

        return archive;
    }


    @SuppressWarnings("java:S3740")
    /**
     * @param deploymentMethod to invoke
     * @return result
     */
    private <T> T invoke(Class<T> type, Method deploymentMethod, Archive currentArchive) {
        try {
            return type.cast(deploymentMethod.invoke(null, currentArchive));
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new CustomRunTimeExc("Could not invoke deployment method: " + deploymentMethod, e);
        }
    }

    private void validate(Method deploymentMethod) {
        if (!Modifier.isStatic(deploymentMethod.getModifiers())) {
            throw new IllegalArgumentException(
                stringP1 + BeforeDeployment.class.getName() + " is not static. " + deploymentMethod);
        }
        if (!Archive.class.isAssignableFrom(deploymentMethod.getReturnType())) {
            throw new IllegalArgumentException(
                stringP1
                    + BeforeDeployment.class.getName()
                    +
                    " must have return type "
                    + Archive.class.getName()
                    + ". "
                    + deploymentMethod);
        }
        if (deploymentMethod.getParameterTypes().length != 1
            && Archive.class == deploymentMethod.getParameterTypes()[0]) {
            throw new IllegalArgumentException(stringP1
                + BeforeDeployment.class.getName()
                + " only accept one parameter of type ." + Archive.class + " "
                + deploymentMethod);
        }
    }

    interface AutomaticDeploymentLocator {
        ServiceLoader<AutomaticDeployment> find();
    }

}
