# Compile
FZU Compiling Principle

## 简介：
本项目是FZU编译原理实践合作项目代码，是以Pascal语言作为模板，根据课程要求使用Java语言实现的类Pascal语言编译器。其中语法分析使用SLR(1)分析法，含有二义性处理程序，并输出action表和goto表。

## 项目目录
该编译器包含以下内容：

```
│  action_goto.csv                  #自动机输出表
│  ERROR.txt                        #错误信息输出
│  grammar.txt                      #语法规则
│  pom.xml                          #项目依赖列表
│  SDT.txt                          #SDT输出表
│  ThreeAddressCode.txt             #三地址码输出表
│  代码*.txt                        #示例代码
└─src
   └─main
       ├─java
       │  │  Compiler.java
       │  ├─Core
       │  │      AnalyseList.java   #语法规则分析与自动机生成
       │  │      Semantic.java      #语义分析
       │  │      TextLex.java       #词法分析
       │  │      TextParse.java     #语法分析
       │  └─UI                      #UI代码
       └─resources                  #资源文件
```

