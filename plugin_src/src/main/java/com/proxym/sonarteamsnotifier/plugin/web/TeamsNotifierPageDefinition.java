package com.proxym.sonarteamsnotifier.plugin.web;

import com.proxym.sonarteamsnotifier.plugin.DataProvider;
import com.proxym.sonarteamsnotifier.plugin.constants.Constants;
import org.sonar.api.web.page.Context;
import org.sonar.api.web.page.Page;
import org.sonar.api.web.page.PageDefinition;

public class TeamsNotifierPageDefinition implements PageDefinition {
    @Override
    public void define(Context context) {
        Page.Builder page = Page.builder(DataProvider.getProperty(Constants.HOME_PAGE_URL));
        page.setName(DataProvider.getProperty(Constants.HOME_PAGE_NAME));
        page.setScope(Page.Scope.GLOBAL);
        context.addPage(page.build());
    }
}
