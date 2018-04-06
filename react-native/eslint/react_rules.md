### React Rules

#### react/boolean-prop-naming
Не позволяет определить внутри PropTypes Boolean иначе, чем начинающийся с `is` (шаблон проверки можно задать самому и 
разрешить еще, например, `has`)

#### react/button-has-type
`<button />` должна иметь тип ("button", "submit" или "reset").

#### react/default-props-match-prop-types
[Не использую]

#### react/destructuring-assignment
Деструктуризация параметров перед использованием. Два режима: `always` и `never`. Во втором случае правило работает 
наоборот - никакой деструктуризации.

#### react/display-name
[Не использую] Требует задания отображаемого имени у компонента. Например, если название компонента - `Component`, то 
необходимо указать `Component.displayName = 'Component'`

#### react/forbid-component-props
Запрещает передачу в компонент данных, которые могут увеличить сложность понимания: `className` и `style`.

#### react/forbid-dom-props
[Не использую] - запрещает передачу указанных в настройках правила props в DOM-элемент, а на компоненты не реагирует.

#### react/forbid-elements
Настраивает запрет на использование указанных в правиле элементов и может сообщать, на какие их следует заменить.
Принимает два параметра - `forbid` и `message`.

#### react/forbid-prop-types
При использовании PropTypes предотвращает появление нечетких типов, как `any`, `array`, `object`. Предполагается, что
эти типы должны быть заменены на более конкретные (любой другой, `arrayOf`, `shape` соответственно).

#### react/forbid-foreign-prop-types
[Не использую]

#### react/no-access-state-in-setstate
Предотвращает использование this.state в функциях setState(...). Это может вызвать ошибку, когда в одном блоке изменений
встречается два и более изменения одной переменной из state. Например, два инкремента. 

#### react/no-array-index-key
Не позволяет использовать номер сущности в массиве в качестве key элемента DOM-дерева. В противном случае, при изменении
сортировки или удалении сущности из массива, произойдет перерисовка всех элементов DOM-дерева в связи не совпадением
key до изменения массива и после.

#### react/no-children-prop
Дети должны быть настоящими детьми и располагаться между открывающим и закрывающим тегами, а не быть переданными, как
тег children.

#### react/no-danger
Предостерегает от использования dangerouslySetInnerHTML, являющегося аналогом innerHTML.

#### react/no-danger-with-children
[Не использую] Предостерегает от одновременного использования dangerouslySetInnerHTML и children.

#### react/no-deprecated
Позволяет увидеть методы, признанные устаревшими для используемой версии React.

#### react/no-did-mount-set-state
#### react/no-did-update-set-state
#### react/no-direct-mutation-state
Никогда нельзя менять `this.state` напрямую. Он преобразовывается использованием метода `setState(...)`. Стейт всегда
должен быть неизменным, согласно архитектуре React. 

#### react/no-find-dom-node
Использование `findDOMNode` является устаревшим. Вместо этого рекомендуется использовать обратные вызовы

#### react/no-is-mounted
[Не использую]

#### react/no-multi-comp
Объявление только одного компонента на файл улучшает читаемость и повторное использование компонентов.

#### react/no-redundant-should-component-update
[Не использую]

#### react/no-render-return-value
Не рекомендуется использовать возвращаемое значение от функции render(). Это связано с тем, что React может рендерить
компоненты ассинхронно.

#### react/no-set-state
[Не использую]

#### react/no-typos
#### react/no-string-refs
#### react/no-this-in-sfc
#### react/no-unescaped-entities
#### react/no-unknown-property
#### react/no-unused-prop-types
#### react/no-unused-state
#### react/no-will-update-set-state
#### react/prefer-es6-class
#### react/prefer-stateless-function
#### react/prop-types
#### react/react-in-jsx-scope
#### react/require-default-props
#### react/require-optimization
#### react/require-render-return
#### react/self-closing-comp
#### react/sort-comp
#### react/sort-prop-types
#### react/style-prop-object
#### react/void-dom-elements-no-children

#### Примеры
##### react/boolean-prop-naming
```jsx harmony
Component.propTypes = {
  enabled: PropTypes.bool, // warning
  isEnabled: PropTypes.bool // is ok
}
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