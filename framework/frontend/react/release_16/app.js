import React, { Component } from 'react';
import ReactDOM from 'react-dom';

import ErrorBoundary from "./ErrorBoundary";

class App extends Component {

    render() {
        return (
            <div>
                <h1>OHOHHOH</h1>
                <ErrorBoundary>
                    <h1>Main Content</h1>
                </ErrorBoundary>
            </div>
        )
    }

}

ReactDOM.render(<App />, document.getElementById('app'));