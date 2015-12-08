Sparkbuilder
================

Sparkbuilder for "[Megam cloud platform](https://www.megam.io)". The Sparkbuilder builds a git repo which contains analytics template code based on machine learning using gradle natively.

We don't spin process, but natively integrate to Gradle using its tooling API.

## How can use this ?

Two ways, sbt or gradle.

## sbt

```

resolvers += "Bintray megamsys" at "https://dl.bintray.com/megamsys/scala/"

libraryDependencies ++= Seq("io.megam" %% "sparkbuilder" % "0.12")

```


### gradle

```
  repositories {
      maven {
        url 'http://dl.bintray.com/megamsys/scala'
      }
    }

    dependencies {
      compile 'io.megam:libcommon_2.11:0.20'
   }

```

## How do i build jars ?

Any git repo provided as input shall be built using gradle. You don't need gradle to be installed.

The below code build the project `https://github.com/megamsys/sparkbuilder.git` in `MEGAM_HOME/yonpis`

```scala

val yp = YonpiProject(new GitRepo(
  new java.io.File(sys.env("MEGAM_HOME") + java.io.File.separator + "megamgateway" + java.io.File.separator + "yonpis/sparkbuilder").getAbsolutePath, "https://github.com/megamsys/sparkbuilder.git"))

yp.buildJar()

yp.buildJar(true) //cleans the repo and build a clean jar.

```

The jars reside in `$MEGAM_HOME/megamgateway/yonpis/<reponame>/build/lib/<reponame>.jar`



## Where are we heading ?

Our yonpis can be used to predict using our custom built analytics workbench based on industry.

Yonpis' will get launched on a spark cluster using the jobserver API client available in [megamgateway](https://github.com/megamsys/megam_gateway.git)

### Publishing to bintray

Update the version flag in build.gradle. Refer our internal documentation (Build Megam) for the user/apikey details


```

gradle clean

gradle build

gradle bintrayUpload


```


### Devcenter article on gradle + scala

Refer [Gradle for scala](http://devcenter.megam.io/2015/12/08/gradle-for-scala/)


We are glad to help if you have questions, or request for new features..

[twitter @megamsys](http://twitter.com/megamsys) [email support@megam.io](<support@megam.io>)


# License

|                      |                                          |
|:---------------------|:-----------------------------------------|
| **Author:**          | KishorekumarNeelamegam (<nkishore@megam.io>)
| **Copyright:**       | Copyright (c) 2013-2015 Megam Systems.
| **License:**         | Apache License, Version 2.0

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
