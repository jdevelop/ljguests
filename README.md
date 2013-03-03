ljguests  [![Build Status](https://secure.travis-ci.org/jdevelop/ljguests.png)](http://travis-ci.org/jdevelop/ljguests)
========

Livejournal Guests tracker and history aggregator

Configuration
========
Configuration file **must** reside in **user home directory**, for example:

- /home/user/.ljguests
- C:\User\Username\ .ljguests

it's content is formatted as JSON:

    {
      "username" : "username",
      "password" : "password",
      "lastDate" : "03-03-2013",
      "timezone" : "EST"
    }

**lastDate** and **timezone** are optional elements.

Running
========
- Build assembly with `mvn clean assembly:assembly`
- Create configuration file as above
- Run as `java -jar ljguests-0.1.jar`
- RSS feed will be dumped to stdout, feel free to redirect it to a file or RSS reader or whatever.
- Enjoy
