#### Webpack

Webpack is a tool for bundling a project (js, css, images, ...) into a single bundle and for running tasks (linting,
tests, ...).

Install Webpack:

`npm i -g webpack`

Initialize a project:

`npm init`

This step creates `package.json` with scripts, dependencies, and other project attributes.

##### Run

Run by calling `webpack`.

##### Configuration

Webpack uses a config file. By default it is called `webpack.config.js`. You can specify a different config file with
the `--config` flag:

`webpack --config <fileName>.config.js`

Example:

```javascript
const path = require("path");

module.exports = {
    mode: 'development',
    entry: './app.js',
    output: {
        path: path.resolve(__dirname, 'dist'),
        filename: 'bundle.js'
    },
    module: {
        rules: [
            {
                test: /\.js$/,
                exclude: /node_modules/,
                use: {
                    loader: "babel-loader"
                }
            }
        ]
    }
};
```

`mode` can be `production` or `development` (also `none`). It controls build optimizations and defaults. For example, in
production mode Webpack minifies output. If you pass `--mode <mode_name>` when running webpack, that value overrides the
one in the config.

`entry` is the project entry file. It can be a string (as above) or an object. Objects are used for multi-bundle setups,
e.g.:

```js
entry: {
    dashboard: 'dashboard/app.js',
    profile: 'profile/app.js'
}
```

`output` specifies output path and filename. In multi-bundle setups you can use `[name].js`.

`module` describes how different module types are built. Example rules:

```js
rules: [
    {
        test: /\.js$/,
        exclude: /node_modules/,
        use: {
            loader: "babel-loader"
        }
    }
]
```

Here, for all `.js` files we use the `babel` loader.


