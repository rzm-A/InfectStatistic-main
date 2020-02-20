# InfectStatistic

- 项目

  &emsp;命令行（win+r cmd）cd到项目src下，之后输入命令：

  ```
  $ java InfectStatistic list -date 2020-01-22 -log D:/log/ -out D:/output.txt
  ```

  会读取D:/log/下的所有日志，然后处理日志和命令，在D盘下生成ouput.txt文件列出2020-01-22全国和所有省的情况（全国总是排第一个，别的省按拼音先后排序）

- 运行

  &emsp;进入项目所在的目录：cd D:/log

  &emsp;编译项目.java文件：Javac -encoding UTF-8 InfectStatistic.java Lib.java

  &emsp;运行项目：java InfectStatistic list -date 2020-01-22 -log D:/log/ -out D:/output.txt

- 功能简介

  list命令 支持以下命令行参数：

  - `-log` 指定日志目录的位置，该项**必会附带**，请直接使用传入的路径，而不是自己设置路径
  - `-out` 指定输出文件路径和文件名，该项**必会附带**，请直接使用传入的路径，而不是自己设置路径
  - `-date` 指定日期，不设置则默认为所提供日志最新的一天。你需要确保你处理了指定日期以及之前的所有log文件
  - `-type` 可选择[ip： infection patients 感染患者，sp： suspected patients 疑似患者，cure：治愈 ，dead：死亡患者]，使用缩写选择，如 `-type ip` 表示只列出感染患者的情况，`-type sp cure`则会按顺序【sp, cure】列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况。
  - `-province` 指定列出的省，如`-province 福建`，则只列出福建，`-province 全国 浙江`则只会列出全国、浙江

  注：java InfectStatistic表示执行主类InfectStatistic，list为**命令**，-date代表该命令附带的**参数**，-date后边跟着具体的**参数值**，如`2020-01-22`。-type 的多个参数值会用空格分离，每个命令参数都在上方给出了描述，每个命令都会携带一到多个命令参数

- list命令 支持以下命令行参数：

  - `-log` 指定日志目录的位置，该项**必会附带**，请直接使用传入的路径，而不是自己设置路径
  - `-out` 指定输出文件路径和文件名，该项**必会附带**，请直接使用传入的路径，而不是自己设置路径
  - `-date` 指定日期，不设置则默认为所提供日志最新的一天。你需要确保你处理了指定日期以及之前的所有log文件
  - `-type` 可选择[ip： infection patients 感染患者，sp： suspected patients 疑似患者，cure：治愈 ，dead：死亡患者]，使用缩写选择，如 `-type ip` 表示只列出感染患者的情况，`-type sp cure`则会按顺序【sp, cure】列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况。
  - `-province` 指定列出的省，如`-province 福建`，则只列出福建，`-province 全国 浙江`则只会列出全国、浙江

  注：java InfectStatistic表示执行主类InfectStatistic，list为**命令**，-date代表该命令附带的**参数**，-date后边跟着具体的**参数值**，如`2020-01-22`。-type 的多个参数值会用空格分离，每个命令参数都在上方给出了描述，每个命令都会携带一到多个命令参数

- 作业链接

  <https://edu.cnblogs.com/campus/fzu/2020SPRINGS/homework/10287>

- 博客链接

  <https://www.cnblogs.com/dongbo-221701313/p/12287364.html>

- 作业完成流程


1. fork仓库，在根目录下新建目录，目录名为学号
2. 复制example下的目录结构到新建的目录下
3. 语言：使用Java开发

4. example/result下提供了三个测试用例的标准输出，对应的命令在文件尾部提供了，即对example/log下的日志，输入对应的命令应该会是相应的输出。
5. 为了使测试文件和输出文件不产生诱导性，在日志文件/输出文件末尾加上`// 该文档并非真实数据，仅供测试使用`，读取日志时直接忽略以`//`开始的行
6. 除了示例仓库的给出的文件，其它自己产生的文件都应该在`.gitignore`忽略，如编译器生成的项目文件、输出文件、class、jar包、exe等
7. 代码每有更新就可以进行commit签入，然后push到github，至少进行10次以上的commit签入，并将最终程序以pr的方式提交到该仓库