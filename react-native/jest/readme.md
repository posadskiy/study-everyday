# Тестирование
Для unit-тестирования кода на ReactNative в проекте используется библиотека Jest.

### Not
Лексема `not` используется для добавления отрицания в проверяющую конструкцию.
##### Пример
```javascript
expect(10).not.toBeNull();
expect(false).not.toBeTruthy();
````

### Общие проверки

| Операция  | Функция |
| --------- |-------- |
| == null | `toBeNull` |
| == undefined | `toBeUndefined` |
| != undefined  | `toBeDefined` |
| == true | `toBeTruthy` |
| == false | `toBeFalsy` |

#### Пример
```javascript
test('common check', () => {
  expect(null).toBeNull();
  expect( {} ).not.toBeNull();

  expect('Not empty string').toBeDefined();
  expect(null).toBeDefined(); //null != undefined

  expect(undefined).toBeUndefined();
  expect([]).not.toBeUndefined();
  expect(2 > 1).toBeTruthy();
  expect(2 < 1).toBeFalsy();
});
```

### Числа

| Операция  | Функция |
| --------- |-------- |
| >  | `toBeGreaterThan` |
| >= | `toBeGreaterThanOrEqual` |
| <  | `toBeLessThan` |
| <= | `toBeLessThanOrEqual` |
| == | `toBe` и `toEqual` |

#### Пример
```javascript
test('number check', () => {
  const value = 2 + 2;
  expect(value).toBeGreaterThan(3);
  expect(value).toBeGreaterThanOrEqual(3.5);
  expect(value).toBeLessThan(5);
  expect(value).toBeLessThanOrEqual(4.5);

  expect(value).toBe(4);
  expect(value).toEqual(4);
});
```

> Для дробных чисел актуально сравнение на равенство с погрешностью. Если сравниваются два вещественных числа A и B, то
проверка должна выглядеть так: A - B < Eps, где Eps - достаточно малое число. Например, 0.00001. В Jest эта проверка
реализована функцией `toBeCloseTo`.
#### Пример
```javascript
test('double number check', () => {
  const value = 0.1 + 0.2;
  //expect(value).toBe(0.3);  Не будет работать
  expect(value).toBeCloseTo(0.3); // Будет работать
});
```

### Строки
Строки можно проверять на содержание в них подстроки с помощью регулярных выражений. В Jest реализована функция `toMatch`

#### Пример
```javascript
test('Substring not in string check', () => {
  expect('String without any animals').not.toMatch(/Elephant/);
});

test('but there is a "stop" in Christoph', () => {
  expect('Jest is test framework for ReactNative').toMatch(/Jest/);
});
```


### Массивы
Массивы можно проверять на наличие в них элемента. Для этого используется функция `toContain`

#### Пример
```javascript
const animalsList = [
  'dog',
  'jaguar',
  'lion',
  'tiger',
  'emu',
];

test('Check that current animal exist in amimalsList', () => {
  expect(shoppingList).toContain('lion');
});
```
###  Объекты

## Мокирование

## Полная документация
https://facebook.github.io/jest/docs/en/expect.html​