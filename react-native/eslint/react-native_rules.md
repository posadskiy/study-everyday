### React Native Rules

#### react-native/no-unused-styles
Инициализированные, но неиспользованные стили. [Пример](#react-nativeno-unused-styles)

#### react-native/split-platform-components
[Не использую] Использование компонент разных платформ (iOS и Android) в одном файле.

#### react-native/no-inline-styles
Стили должны быть отделены от разметки. Правило обнаруживает inline-стили внутри разметки.
[Пример](#react-nativeno-inline-styles)

#### react-native/no-color-literals
Все значения цвета в стилях должны быть вынесены в константы для переиспользования и
управления из единого расположения. [Пример](#react-nativeno-color-literals)

#### Примеры
##### react-native/no-unused-styles
```jsx harmony
const styles = StyleSheet.create({
  text: {}
});

const Hello = React.createClass({
  render: function() {
    return <Text>Hello {this.props.name}</Text>;
  }
});
```

##### react-native/no-inline-styles
```jsx harmony
const Hello = React.createClass({
  render: function() {
    return <Text style={{backgroundColor: '#FFF'}}>Hello {this.props.name}</Text>;
  }
});
```

##### react-native/no-color-literals
```jsx harmony
<Text style={{backgroundColor: '#FFF'}}>Hello</Text>;
```