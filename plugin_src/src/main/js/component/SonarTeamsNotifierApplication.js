
import React from "react";

import DeferredSpinner from 'sonar-ui-common/components/ui/DeferredSpinner';
export default class SonarTeamsNotifierApplication extends React.PureComponent {
    render() {
        if (this.state.loading) {
            return <div className="page page-limited"><DeferredSpinner /></div>;
        }

        return (
            <div>Testing notifier template</div>
        );
    }
}