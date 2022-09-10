/**
 * Entrypoint of the plugin frontend
 * Written with ReactJS
 */

import React from 'react';
import './style.css';
import Initializer from 'sonar-ui-common/helpers/init';
import ProxymTeamsApp from "./components/ProxymTeamsApp";

Initializer.setUrlContext(window.baseUrl);

window.registerExtension('teamsnotifier/config', () => {
    return <ProxymTeamsApp />
  });