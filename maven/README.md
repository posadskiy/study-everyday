# Maven

### Profiles
Команда с профилем: mvn <фаза> -P <профиль>

## Dependencies
### Dependency mediation
Если A->B->D.1.0 и A->C->D.2.0, происходит конфликт. Будет использована та зависимость, которая находится выше по дереву.
Если две или более зависимостей находятся на одном уровне, то будет выбрана та, которая объявлена первой.

## Plugins
Плагин включает в себя цели. (Plugin includes goals)

## Other
### POM inheritance
pom.xml, создаваемый для проекта, по умолчанию является наследником super-POM, предоставляемого Maven. В нем находятся объявления:
* репозитория Maven Central (repository)
* репозитория плагинов (pluginRepository)
* директорий исходных кодов, тестов, и т.д. (sourceDirectory, testSourceDirectory, outputDirectory, etc.)
* версии плагинов (pluginManagement)
* плагинов, привязанных к фазам сборки (plugins)

POM, включающий в себя текущий, всех родителей и корневой, можно увидеть командой `mvn help:effective-pom`