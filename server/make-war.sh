#!/bin/bash

sed -i -e 's/\[io\.pedestal\/pedestal\.jetty \"0\.2\.0\-SNAPSHOT\"\]/;\[io\.pedestal\/pedestal\.jetty \"0\.2\.0\-SNAPSHOT\"\]/g' project.clj

mkdir -p target/war/WEB-INF/classes
cp -R src/* config/* target/war/WEB-INF/classes
cp web.xml target/war/WEB-INF
jar cvf target/server.war -C target/war WEB-INF


sed -i -e 's/;\[io\.pedestal\/pedestal\.jetty \"0\.2\.0\-SNAPSHOT\"\]/\[io\.pedestal\/pedestal\.jetty \"0\.2\.0\-SNAPSHOT\"\]/g' project.clj
