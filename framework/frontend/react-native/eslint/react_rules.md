### React Rules

#### react/boolean-prop-naming

Does not allow defining a Boolean prop in PropTypes unless its name starts with `is` (the pattern can be configured; for
example, you can also allow `has`).

#### react/button-has-type

`<button />` must have a type (`"button"`, `"submit"`, or `"reset"`).

#### react/default-props-match-prop-types

[Not used]

#### react/destructuring-assignment

Destructure parameters before using them. Two modes: `always` and `never`. In the second mode the rule works in the
opposite direction — no destructuring.

#### react/display-name

[Not used] Requires setting a display name for the component. For example, if the component name is `Component`, then you
must set `Component.displayName = 'Component'`.

#### react/forbid-component-props

Forbids passing props to a component that can increase complexity: `className` and `style`.

#### react/forbid-dom-props

[Not used] — forbids passing specified props (configured in the rule) to a DOM element; does not apply to components.

#### react/forbid-elements

Configures a ban on using specified elements and can tell what to replace them with. Accepts two parameters: `forbid` and
`message`.

#### react/forbid-prop-types

When using PropTypes, prevents vague types like `any`, `array`, `object`. Assumes these should be replaced with more
specific forms (any other specific type, `arrayOf`, `shape` respectively).

#### react/forbid-foreign-prop-types

[Not used]

#### react/no-access-state-in-setstate

Prevents using `this.state` inside `setState(...)` calls. This can cause bugs when multiple state updates happen close
together (e.g., two increments).

#### react/no-array-index-key

Disallows using array index as the `key`. Otherwise, when sorting or removing items, React may re-render many DOM nodes
because keys no longer match.

#### react/no-children-prop

Children should be real JSX children between opening and closing tags, not passed via a `children` prop.

#### react/no-danger

Warns about using `dangerouslySetInnerHTML`, which is analogous to `innerHTML`.

#### react/no-danger-with-children

[Not used] Warns about using both `dangerouslySetInnerHTML` and `children` at the same time.

#### react/no-deprecated

Lets you see methods that are deprecated for the React version you use.

#### react/no-direct-mutation-state

Never mutate `this.state` directly; use `setState(...)`. State should be immutable in React’s architecture.

#### react/no-find-dom-node

Using `findDOMNode` is deprecated. Instead, use callback refs.

#### react/no-is-mounted

[Not used]

#### react/no-multi-comp

Declaring only one component per file improves readability and reuse.

#### react/no-redundant-should-component-update

[Not used]

#### react/no-render-return-value

Using the return value of `render()` is discouraged, because React may render components asynchronously.

#### react/no-set-state

[Not used]

#### Examples

##### react/boolean-prop-naming

```jsx harmony
Component.propTypes = {
  enabled: PropTypes.bool, // warning
  isEnabled: PropTypes.bool // is ok
}
```

##### react/button-has-type

```jsx harmony
const Hello = <button>Hello</button> // warning - type missing
const Hello = <button type="foo">Hello</button> // warning - invalid type
const Hello = <button type="button">Hello</button> // is ok
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

##### react/forbid-component-props

```jsx harmony
const Foo = <Hello className='foo' /> // warning
const Red = <Hello style={{color: 'red'}} /> // warning

const Foo = <div className='foo' /> // is ok
const Red = <div style={{color: 'red'}} /> // is ok
```

##### react/forbid-elements

```jsx harmony
// [1, { "forbid": ["button"] }]
<button /> // warning
React.createElement('button'); // warning
<Button /> // is ok
```

##### react/forbid-prop-types

```jsx harmony
Component.propTypes = {
  a: PropTypes.any,
  r: PropTypes.array,
  o: PropTypes.object
};
```

##### react/no-access-state-in-setstate

```jsx harmony
function increment() {
  this.setState({value: this.state.value + 1}); // warning
}

increment(); // setState({value: 1 + 1})
increment(); // setState({value: 1 + 1})

function increment() {
  this.setState(prevState => ({value: prevState.value + 1})); // is ok
}

increment(); // setState({value: 1 + 1})
increment(); // setState({value: 2 + 1})
```

##### react/no-array-index-key

```jsx harmony
things.map((thing, index) => (
  <Hello key={index} />
));

things.map((thing) => (
  <Hello key={thing.id} />
));
```

##### react/no-children-prop

```jsx harmony
<MyComponent children={<AnotherComponent />} />; // warning
<MyComponent children={['Child 1', 'Child 2']} />; // warning

<MyComponent>
  <span>Child 1</span> // is ok
  <span>Child 2</span>
</MyComponent>
```

##### react/no-danger

```jsx harmony
const Hello = <div dangerouslySetInnerHTML={{ __html: "Hello World" }}></div>; // warning
const Hello = <div>Hello World</div>; // is ok
```

##### react/no-direct-mutation-state

```jsx harmony
class Component extends React.Component {
  constructor(props) {
    super(props)
    this.state = 'bad'; // warning
  }
}
```

##### react/no-find-dom-node

```jsx harmony
class MyComponent extends Component {
  componentDidMount() {
    findDOMNode(this).scrollIntoView(); // warning
    this.node.scrollIntoView(); // is ok
  }

}
```

##### react/no-multi-comp

```jsx harmony
class Hello extends React.Component {
  // ...
}
class HelloJohn extends React.Component { // warning
  // ...
}
```

##### react/no-render-return-value

```jsx harmony
const inst = ReactDOM.render(<App />, document.body);
doSomethingWithInst(inst);
```


