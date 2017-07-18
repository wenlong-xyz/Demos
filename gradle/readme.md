# Gradle 学习
Gradle 学习笔记
1. [学习资料1-优达学城Gradle课程](https://classroom.udacity.com/courses/ud867)
2. [Gradle学习系列](http://www.cnblogs.com/davenkin/p/gradle-learning-1.html)
3. [Gradle Guides](https://gradle.org/guides/)
## Groovy 相关知识
1. **以下示例内容来自[udacity](https://github.com/udacity/ud867)**
1. 基础知识
    ```java
    // 常见示例，示例来源: 
    task groovy {}
    println "Hello Groovy!"

    // class
    class JavaGreeter {
    public void sayHello() {
        System.out.println("Hello Java!");
        }
    }
    JavaGreeter greeter = new JavaGreeter()
    greeter.sayHello()

    // 类型推断
    def foo = 6.5
    // 获取变量值，其他脚本语言中也常见
    println "foo has value: $foo"
    // {}中为表达式
    println "Let's do some math. 5 + 6 = ${5 + 6}"
    // 内置属性，获取class 类型
    println "foo is of type: ${foo.class} and has value: $foo"
    foo = "a string"
    println "foo is now of type: ${foo.class} and has value: $foo"

    // 函数，无需书写return，默认返回最后一个表达式的值
    def doubleIt(n) {
        n + n 
    }
    foo = 5
    println "doubleIt($foo) = ${doubleIt(foo)}"
    foo = "foobar"
    println "doubleIt($foo) = ${doubleIt(foo)}"

    // 无括号调用函数
    def noArgs() {
        println "Called the no args function"
    }

    def oneArg(x) {
        println "Called the 1 arg function with $x"
        x
    }

    def twoArgs(x, y) {
        println "Called the 2 arg function with $x and $y"
        x + y
    }

    oneArg 500 // Look, Ma! No parentheses!
    twoArgs 200, 300
    noArgs()
    //noArgs // Doesn't work
    //twoArgs oneArg 500, 200 // Also doesn't work as it's ambiguous
    twoArgs oneArg(500), 200 // Fixed the ambiguity
    ```

2. 闭包和对象
    ```java
    task groovy {}

    def foo = "One million dollars"
    // 定义闭包
    def myClosure = {
        println "Hello from a closure"
        println "The value of foo is $foo"
    }
    // 闭包传递
    myClosure()
    def bar = myClosure
    def baz = bar
    baz()

    // lambda
    def doubleIt = { x -> x + x}
    def applyTwice(func, arg){
        func(func(arg))
    }
    foo = 5
    def fooDoubledTwice = applyTwice(doubleIt, foo)
    println "Applying doubleIt twice to $foo equals $fooDoubledTwice"
    
    // list示例
    def myList = ["Gradle", "Groovy", "Android"]
    def printItem = {item -> println "List item: $item"}
    myList.each(printItem)

    // class 和 对象
    class GroovyGreeter {
        String greeting = "Default greeting"
        def printGreeting(){println "Greeting: $greeting"}
    }
    def myGroovyGreeter = new GroovyGreeter()
    myGroovyGreeter.printGreeting()
    myGroovyGreeter.greeting = "My custom greeting"
    myGroovyGreeter.printGreeting()

    // 委托
    def greetingClosure = {
        greeting = "Setting the greeting from a closure"
        printGreeting()
    }
    // greetingClosure() // This doesn't work, because `greeting` isn't defined
    greetingClosure.delegate = myGroovyGreeter
    greetingClosure() // This works as `greeting` is a property of the delegate
    ```

## Task
1. 依赖关系
    * 基本关系：单依赖配置
        - dependsOn: 依赖于另外一个task, 被依赖的task需要先执行
            ```
            task putOnSocks {
                doLast {
                    println "Putting on Socks."
                }
            }

            task putOnShoes {
                dependsOn "putOnSocks"
                doLast {
                    println "Putting on Shoes."
                }
            }
            ```
        - finalizedBy: 一个任务执行完后，需要跟随执行的任务（终结/尾随任务）
            ```
            task eatBreakfast {
                finalizedBy "brushYourTeeth"
                doLast{
                    println "Om nom nom breakfast!"
                }
            }

            task brushYourTeeth {
                doLast {
                    println "Brushie Brushie Brushie."
                }
            }
            ```
        - shouldRunAfter: A shouldRunAfter B，意味着：
            + 只执行task A。 task B 不会执行
            + 如果taskA，B都需要执行，则需要先执行B，再执行A。
            ```
            task takeShower {
                doLast {
                    println "Taking a shower."
                }
            }

            task putOnFragrance {
                shouldRunAfter "takeShower"
                doLast{
                    println "Smellin' fresh!"
                }
            }
            ```
    * 多依赖配置
        ```
        task getReady {
            // Remember that when assigning a collection to a property, we need the
            // equals sign
            dependsOn = ["takeShower", "eatBreakfast", "putOnShoes"]
        }
        // 灵活配置
        putOnShoes.mustRunAfter takeShower
        task getEquipped {
            dependsOn tasks.matching{ task -> task.name.startsWith("putOn")}
            doLast {
                println "All geared up!"
            }
        }
        ```
2. 类型化任务
    * 特定操作的任务，如文件拷贝、删除、压缩，gradle提供了许多内置的限定类型的任务
        ```java
        // 拷贝
        task copyWeb(type: Copy) {
            from 'src/web'
            from('src/docs') {
                include '*.txt'
                into 'help'
            }
            into 'build/web'
        }
        // 压缩
        task bundleWeb(type: Zip, dependsOn: copyWeb) {
            baseName = 'web'
            destinationDir = file('build')
            
            from 'build/web'
            exclude 'images/**'
        }
        // 解压
        task unpackBundle(type: Copy, dependsOn: bundleWeb) {
            from zipTree('build/web.zip')
            into 'build/exploded'
        }
        // 删除
        task deleteHelp(type: Delete, dependsOn: copyWeb) {
            delete 'build/web/help'
        }
        ```
    * 自定义类型任务
        ```
        // 定义
        class HelloNameTask extends DefaultTask {
            String firstName

            // TaskAction注解说明从类中创建的任务操作
            @TaskAction
            void doAction() {
                println "Hello, $firstName"
            }
        }
        // 使用
        task helloName(type: HelloNameTask) {
            firstName = 'Jeremy'
        }

        ```
3. 增量构建
    * 自动增量构建，避免重复构建
    * Gradle 会跟踪每个任务的输入输出，任务执行前，会保存该任务使用的输入快照。如果一个任务没有任务快照或者快照发生改变，则Gradle会重新执行该任务。同理，如果输入没有发生改变，而且输出自上次任务之后也没被更改过，则Gradle则会跳过任务执行
4. 脚本参数传递，如下面的`task printGreeting`示例，如何将`greeting`参数值传递到脚本中
    ```
    task printGreeting {
        doLast {
           println greeting
        }
    }
    ```
    * `gradle.properties`文件中定义
        ```
         greeting = "Hello from a properties file"
        ```
    * 命令行直接传递：`gradle printGreeting -Pgreeting="Hello from the command line"`
    * 脚本内部定义
        ```
        ext {
            greeting = "Hello from inside the build script"
        }
        ```
    * 优先级：脚本内部定义 > 命令行 > `properties`文件
5. 故障排除和记录
    * 日志级别，从高到低
        - ERROR: Error messages, -
        - QUIET: -q, 包括程序打印信息
        - WARNING: Warning messages
        - LIFECYCLE: default, 从当前开始运行的任务构建话费的总时间以及是否构建成功
        - INFO: -i, 详细介绍每个任务所需的以及其他高级事件
        - DEBUG: -d, 会输出大量Gradle的内部信息
    * 输出程序执行stacktrace
        - gradle -s # 部分堆栈信息
        - gradle -S # 完全堆栈信息 
6. 构建生命周期
    * initialization: 多个项目的构建设置
    * configuration: 决定运行哪个task，以及task运行顺序
    * execution: 运行 

## 项目构建
1. 进行各种项目构建之前，优先考虑基础plugin，[官方文档](https://docs.gradle.org/4.0.1/userguide/userguide.html)
2. 依赖管理
    * 中央仓库配置：
        ```java
        // 本地仓库配置
        repositories {
            flatDir {
                dirs 'libs'
            }
        }
        // 最常见的配置方式，mavenCentral, mavenLocal, jcenter都是常用的代码库
        repositories {
            mavenCentral()
            mavenLocal()
            jcenter()
        }
        // maven，ivy 解析
        repositories {
            maven {
                url 'https://repo.foo.org/m2'
            }
        }
        repositories {
            ivy {
                url 'https://repo.foo.org/ivy'
                credentials {
                    username 'user'
                    password 'secret'
                }
            }
        }
        // 支持的协议包括：HTTP，HTTPS，SFTP 
        repositories {
            ivy {
                url 'file:///home/user/repo'
            }
        }
        ```
2. 依赖配置
    ```java
    dependencies {
        compile 'com.google.guava:guava:18.0'
        // 与上面的配置等价
        // compile group: 'com.google.guava', name: 'guava', version: '18.0'
        // 本地文件
        compile files('libs/foo.jar', 'libs/bar.jar')
        // 目录树，依赖多个文件
        compile fileTree(dir: 'libs', include: '*.jar')
    }
    ```
3. 依赖报告
    + `gradle dependencies` 查看项目依赖报告，包括传递依赖
    + `gradle dependencies --configuration runtime` 查看运行时间配置的依赖
    + `gradle dependencyInsight --dependency commons-logging` 查看项目中使用的特定依赖，可用于依赖冲突查看
4. 自定义依赖配置
    ```
    // 自定义依赖配置
    configurations {
        custom
    }
    dependencies {
        custom 'com.google.guava:guava:18.0'
    }

    // compile阶段依赖会在testCompile阶段继续使用，有种“继承”的感觉
    dependencies {
        compile 'commons-logging:commons-logging:1.1.3'
        testCompile 'junit:junit:4.12'
    }
    ```
5. Test
    + `testCompile 'junit:junit:4.12'` 添加合适的测试依赖
    + `gradle test` test构建，自动执行test代码
    + `build/reports/tests/index.html` 自动生成的test报告
6. [插件检索](https://plugins.gradle.org/)
7. wrapper 意味着Gradle版本也是版本控件，考证所有人的gradle版本是一致的
    + `gradle wrapper` 生成Gradle wrapper
    + wrapper使用
        * `./gradlew tasks`
    + wrapper 配置gradle版本
        ```
        wrapper {
            gradleVersion = '2.14.1'
        }
        ```

## 参考文献
1. [Gradle dsl(Domain-specific language)](https://docs.gradle.org/current/dsl/)

## TODO
1. 增量构建内部原理




