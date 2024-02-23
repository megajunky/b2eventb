# b2eventb

## Installation

Download from https://github.com/megajunky/b2eventb

## Usage

Run the translate one or more B machines to Rodin projects directly.
For each .mch file a project is created at the path specified with `-o`.

    $ clojure -M:run-m -o eventb b/Benchmarks/scheduler.mch

ABZ 2020 example can be run with:

    $ clojure -X b2eventb.b2eventb/abz-2020

Run the project's CI pipeline and build an uberjar (this will fail until you edit the tests to pass):

    $ clojure -T:build ci

This will produce an updated `pom.xml` file with synchronized dependencies inside the `META-INF`
directory inside `target/classes` and the uberjar in `target`. You can update the version (and SCM tag)
information in generated `pom.xml` by updating `build.clj`.

If you don't want the `pom.xml` file in your project, you can remove it. The `ci` task will
still generate a minimal `pom.xml` as part of the `uber` task, unless you remove `version`
from `build.clj`.

Run that uberjar:

    $ java -jar target/net.clojars.b2eventb/b2eventb-0.1.0-SNAPSHOT.jar

