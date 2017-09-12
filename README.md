# se_lab1 哈工大软件工程实验1

主要任务：获取文本文件里的字符串进行生成单词节点邻接关系有向图
 
## 需求1：ReplaceStr √

> main(String[] args)

换行符以及其他标点符号替换为空格，非字母字符删除，连续空格处理，选做：单词状态转换。

*注：更改为GUI框架内处理*

## 需求2：ShowGraph √

> type createDirectedGraph(String filename);void showDirectedGraph(type G)

根据生成的节点以及邻接关系生成有向图（GUI部分），选做：将生成图像保存本地

## 需求3：QueryBridgeWords √

> String queryBridgeWords(type G, String word1, String word2)

查询桥接词，s1：存在桥接；s2：不存在桥接；s3：不在图中；s4：多个桥接词

## 需求4：CreatedByGraph √

> String generateNewText(type G, String inputText)

通过桥接图对新输入的文本进行插入桥接词

## 需求5：ShortPathOfWords √

> String calcShortestPath(type G, String word1, String word2)

输出两个词之间最短路径，并凸出标记图输出，选做：单源最短路径输出

## 需求6：String randomWalk(type G) √

> String randomWalk(type G)

随机节点出发随机遍历（重复边或者无节点可寻则停止），用户可手动停止遍历，输出构造文本

## 初步设计主界面原型

![设计原型](https://raw.githubusercontent.com/zhouchang29/se_lab1/master/%E4%B8%BB%E7%95%8C%E9%9D%A2%E8%AE%BE%E8%AE%A1%E5%8E%9F%E5%9E%8B.png)

## 项目进度记录 

### 2017/09/07

在master分支已经做好了后端数据架构的情况下，应对图形化界面的应用情况，分出了三个分支：

+ GraphvizVersion：Java外部调用命令行软件Graphviz进行图形化的生成
+ JGraphTVersion：拟采用JGraphT图形库生成可交互动态图形
+ Graphics2DVersion：当以上两个版本遇到不可抗力的因素而无法达到要求的最后选择，即用Java图形库建立自定义绘图库来生成图形

同时完成了部分原型的设计部分

如果没有遇到困难，GraphvizVersion分支预期在本周末完成项目需求90%

Note Created by Yumi

### 2017/09/09

考虑到JGraphT库实现有向图可视化过于困难而删除掉JGraphTVersion branch

master分支完成所有需求的后端数据部分，包括桥接词、路径和随机游走数据。

工作方向转向前端设计部分

Note Created by Yumi

### 2017/09/11

所有需求大致完成，确定以Graphviz为master分支方向，当前仍需解决的问题有：

1. 窗口布局优化
2. 生成图放大缩小(按钮)功能
3. 多条最短路径显示的适配
4. 在随机游走中优化显示效率以及修复不能停止游走的缺陷
5. 完成部分可选的功能

Note Created by Yumi

### 2017/09/12

所有基本需求已经完成，合并到master分支，完成软件迭代周期。

Note Created by Yumi
![设计实现](https://raw.githubusercontent.com/zhouchang29/se_lab1/master/%E7%AC%AC%E4%B8%80%E6%AC%A1%E8%BF%AD%E4%BB%A3.png)
