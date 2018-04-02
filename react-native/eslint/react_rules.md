### React Rules

`react/boolean-prop-naming` - не позволяет определить внутри PropTypes Boolean иначе, чем начинающийся с `is` (шаблон
проверки можно задать самому и разрешить еще, например, `has`)\
`react/button-has-type` - `<button />` должна иметь тип ("button", "submit" или "reset").\
`react/default-props-match-prop-types` [Не использую]\
`react/destructuring-assignment` - деструктуризация параметров перед использованием. Два режима: `always` и `never`. Во
втором случае правило работает наоборот - никакой деструктуризации.\
react/display-name
react/forbid-component-props
react/forbid-dom-props
react/forbid-elements
react/forbid-prop-types
react/forbid-foreign-prop-types
react/no-access-state-in-setstate
react/no-array-index-key
react/no-children-prop
react/no-danger
react/no-danger-with-children
react/no-deprecated
react/no-did-mount-set-state
react/no-did-update-set-state
react/no-direct-mutation-state
react/no-find-dom-node
react/no-is-mounted
react/no-multi-comp
react/no-redundant-should-component-update
react/no-render-return-value
react/no-set-state
react/no-typos
react/no-string-refs
react/no-this-in-sfc
react/no-unescaped-entities
react/no-unknown-property
react/no-unused-prop-types
react/no-unused-state
react/no-will-update-set-state
react/prefer-es6-class
react/prefer-stateless-function
react/prop-types
react/react-in-jsx-scope
react/require-default-props
react/require-optimization
react/require-render-return
react/self-closing-comp
react/sort-comp
react/sort-prop-types
react/style-prop-object
react/void-dom-elements-no-children

#### Примеры
##### react/boolean-prop-naming
```jsx harmony
const Hello = createReactClass({
  propTypes: {
    enabled: PropTypes.bool, // warning
    isEnabled: PropTypes.bool // is ok
  },
  render: function() { return <div />; }
})
```

##### react/button-has-type
```jsx harmony
const Hello = <button>Hello</button> // warning - not exist type
const Hello = <button type="foo">Hello</button> // warning - incorrect type
const Hello = <button type="button">Hello</button> //is ok
```

##### react/destructuring-assignment
```jsx harmony
const MyComponent = (props) => {
  const result = (<div id={props.id} />) // warning
  
  const { context: {
      foo
  }} = this;
  const result = <div>{this.context.foo}</div>; // is ok
  
  return result;
};
```