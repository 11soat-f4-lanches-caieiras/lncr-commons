package br.com.tp.lncr.commons.bdd;

import io.cucumber.junit.platform.engine.Constants;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("br/com/tp/lncr/commons/bdd")
@ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME, value = "br.com.tp.lncr.commons.bdd")
@ConfigurationParameter(key = Constants.PLUGIN_PROPERTY_NAME, value = "pretty, html:target/cucumber-reports/commons-bdd.html, json:target/cucumber-reports/commons-bdd.json")
public class AllBddRunners {
}

