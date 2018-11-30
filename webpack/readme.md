#### Webpack

Webpack - инструмент для сборки проекта (js, css, image, ...) в единый бандл
и запуска задач (линтинг, тесты, ...).

Установка Webpack:
`npm i -g webpack`

Инициализация проекта:
`npm init`

На данном шаге создастся файл package.json с скриптами запуска, зависимостями и прочими
атрибутами проекта.

##### Запуск
Запуск происходит вызовом команды `webpack`.

##### Конфигурация

Для Webpack создается конфигурационный файл. По умолчанию он имеет название `webpack.config.js`.
Можно указывать и другой конфигурационный файл с помощью флага *--config*: 
`webpack --config <fileName>.config.js`.

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

**mode** - *production* или *development* (еще есть *none*). Задают режимы сборки проекта и
необходимостью применения к нему плагинов. Например, в режиме production применяется
UglifyJs. Если при запуске webpack передан параметр `--mode <mode_name>`, то значение здесь
игнорируется.

**entry** - имя файла входа в проект. Аргументом может быть как строка в примере, так и объект.
Объект используется для многобандльной структуры. Например:
```
entry: {
    dashboard: 'dashboard/app.js',
    profile: 'profile/app.js'
}
```

**output** - параметры выходного файла. Путь и название. При многобандльной структуре имя
можно задавать как `[name].js`.

**module** - описание модулей сборки.
```
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

Здесь мы для всех `.js` файлов указываем лоадер *babel*.