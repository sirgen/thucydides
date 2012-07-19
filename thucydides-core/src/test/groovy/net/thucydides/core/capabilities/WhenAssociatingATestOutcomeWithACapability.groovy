package net.thucydides.core.capabilities

import net.thucydides.core.capabilities.stories.grow_potatoes.ASampleTestWithACapability
import net.thucydides.core.capabilities.stories.grow_potatoes.another_package.ASampleTestInAnotherPackage
import net.thucydides.core.capabilities.stories.grow_potatoes.grow_new_potatoes.ASampleNestedTestWithACapability
import net.thucydides.core.capabilities.stories.nocapacities.ASampleTestWithNoCapability
import net.thucydides.core.model.TestOutcome
import net.thucydides.core.model.TestTag
import net.thucydides.core.util.EnvironmentVariables
import net.thucydides.core.util.MockEnvironmentVariables
import spock.lang.Specification

class WhenAssociatingATestOutcomeWithACapability extends Specification {

    def "Should associate a test case to capability based on it's package"() {
        given: "We are using the default capabilities provider"
            EnvironmentVariables vars = new MockEnvironmentVariables();
            FileSystemCapabilityProvider capabilityProvider = new FileSystemCapabilityProvider("stories", 0, vars);
        and: "We define the root package in the 'thucydides.test.root' property"
            vars.setProperty("thucydides.test.root","net.thucydides.core.capabilities.stories")
        when: "We load capabilities with nested capability directories and no .narrative files"
            def testOutcome = new TestOutcome("someTest",ASampleTestWithACapability)
        then:
            capabilityProvider.getTagsFor(testOutcome) == [TestTag.withName("Grow potatoes").andType("capability")]
    }

    def "Should associate a nested test case to capability based on it's package"() {
        given: "We are using the default capabilities provider"
            EnvironmentVariables vars = new MockEnvironmentVariables();
            FileSystemCapabilityProvider capabilityProvider = new FileSystemCapabilityProvider("stories", 0, vars);
        and: "We define the root package in the 'thucydides.test.root' property"
            vars.setProperty("thucydides.test.root","net.thucydides.core.capabilities.stories")
        when: "We load capabilities with nested capability directories and no .narrative files"
            def testOutcome = new TestOutcome("someTest",ASampleNestedTestWithACapability)
        then:
            capabilityProvider.getTagsFor(testOutcome) == [TestTag.withName("Grow potatoes").andType("capability"),
                                                           TestTag.withName("Grow new potatoes").andType("feature")]
    }

    def "Should associate a nested test case to the nearest above capacity"() {
        given: "We are using the default capabilities provider"
            EnvironmentVariables vars = new MockEnvironmentVariables();
            FileSystemCapabilityProvider capabilityProvider = new FileSystemCapabilityProvider("stories", 0, vars);
        and: "We define the root package in the 'thucydides.test.root' property"
            vars.setProperty("thucydides.test.root","net.thucydides.core.capabilities.stories")
        when: "We load capabilities with nested capability directories and no .narrative files"
            def testOutcome = new TestOutcome("someTest",ASampleTestInAnotherPackage)
        then:
            capabilityProvider.getTagsFor(testOutcome) == [TestTag.withName("Grow potatoes").andType("capability")]
    }

    def "Should not associate a test case if there is no matching capability"() {
        given: "We are using the default capabilities provider"
            EnvironmentVariables vars = new MockEnvironmentVariables();
            FileSystemCapabilityProvider capabilityProvider = new FileSystemCapabilityProvider("stories", 0, vars);
        and: "We define the root package in the 'thucydides.test.root' property"
            vars.setProperty("thucydides.test.root","net.thucydides.core.capabilities.stories")
            when: "We load capabilities with nested capability directories and no .narrative files"
            def testOutcome = new TestOutcome("someTest",ASampleTestWithNoCapability)
        then:
            capabilityProvider.getTagsFor(testOutcome) == []
    }

}

