/**
 * Entrypoint of the plugin frontend
 * Written with ReactJS
 */

import React from 'react';
import './style.css';
import Initializer from 'sonar-ui-common/helpers/init';
import SonarTeamsNotifierApplication from "./component/SonarTeamsNotifierApplication";

Initializer.setUrlContext(window.baseUrl);

window.registerExtension('sonar/notifier', () => {
    return <SonarTeamsNotifierApplication />
});