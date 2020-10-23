# Negusta

A command line tool that helps you generate a lot of dummy data. You can specify a template string and how much data you would like to get and `negusta` will substitude the random values for you.


## Examples

Negusta includes all placeholders provided by [JavaFaker](https://github.com/DiUS/java-faker) which is port of the popular Ruby [faker](https://github.com/faker-ruby/faker) gem.

```bash
> negusta generate "{name.firstName}_{name.lastName}" --lines=5
Stephan_Luettgen
William_Walker
Rima_Hilpert
Deangelo_Pagac
Cherish_Dooley
```

In addition, you can create your own placeholders. All you have to do is place a `your-name.txt` file in `~/.negusta` directory and use all lines in it as `{custom.your-name}`.

```bash
> cat ~/.negusta/computer.txt
Macbook Pro
Macbook Air
Thinkpad T
Thinkpad L
Desktop
Xbox
Playstation
Jarvis

> negusta generate "{name.firstName} has {custom.computer}" --lines=5
Elwood has Macbook Air
Colton has Playstation
Rodney has Xbox
Matilde has Thinkpad T
Lavonna has Thinkpad L
```