### React Native Rules

#### react-native/no-unused-styles

Initialized but unused styles. [Example](#react-nativeno-unused-styles)

#### react-native/split-platform-components

[Not used] Using components for different platforms (iOS and Android) in one file.

#### react-native/no-inline-styles

Styles must be separated from markup. The rule detects inline styles inside markup.
[Example](#react-nativeno-inline-styles)

#### react-native/no-color-literals

All color values in styles should be extracted into constants so they can be reused and managed from a single place.
[Example](#react-nativeno-color-literals)

#### Examples

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


