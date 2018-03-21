# Maven

## Phases
| Phase  | Plugin:goal |
| ------------- | ------------- |
| process-resources | resources:resources |
| compile | compiler:compile |
| process-test-resources | resources:testResources |
| test-compile | compiler:testCompile |
| test | surefire:test |
| package | jar:jar |
| install | install:install |
| deploy | deploy:deploy |

Все фазы: validate, initialize, generate-sources, process-sources, generate-resources, process-resources, compile,
process-classes, generate-test-sources, process-test-sources, generate-test-resources, process-test-resources,
test-compile, process-test-classes, test, prepare-package, package, pre-integration-test, integration-test, 
post-integration-test, verify, install, deploy, pre-clean, clean, post-clean, pre-site, site, post-site, site-deploy.

### Profiles
Команда с профилем: `mvn <фаза> -P <профиль>`

## Dependencies
### Dependency mediation
Если A->B->D.1.0 и A->C->D.2.0, происходит конфликт. Будет использована та зависимость, которая находится выше по дереву.
Если две или более зависимостей находятся на одном уровне, то будет выбрана та, которая объявлена первой.
### IntelliJ Dependencies Diagram
Кликом в любом месте pom.xml файла и выбором пункта Diagram -> Show Dependencies `[Ctrl+Alt+Shift+U]` можно открыть диаграмму зависимостей.
На ней красными стрелками обозначен путь к конфликтующим зависимостям, которые не будут использоваться.

## Plugins
Плагин включает в себя цели. (Plugin includes goals)

## Other
### POM inheritance
pom.xml, создаваемый для проекта, по умолчанию является наследником super-POM, предоставляемого Maven. 
В нем находятся объявления:
* репозитория Maven Central (repository)
* репозитория плагинов (pluginRepository)
* директорий исходных кодов, тестов, и т.д. (sourceDirectory, testSourceDirectory, outputDirectory, etc.)
* версии плагинов (pluginManagement)
* плагинов, привязанных к фазам сборки (plugins)

POM, включающий в себя текущий, всех родителей и корневой, можно увидеть командой `mvn help:effective-pom`
Скомпилировать тесты и положить в test-out, но не запускать: -DskipTests.
Пропустить тесты полностью: `-Dmaven.test.skip=true`