import React, { Component } from 'react';

class ErrorBoundary extends Component {
    constructor(props) {
        super(props);

        this.state = {
            hasError: false,
        };
    }

    componentDidCatch(error, errorInfo) {
        console.log("error", error, errorInfo);
        this.setState({ hasError: true });
    }

    render() {
        if (this.state.hasError) {
            return "Sorry! We have a problem. Please, report to contact mail";
        } else {
            return this.props.children;
        }
    }
}

export default ErrorBoundary;