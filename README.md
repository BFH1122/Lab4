# se_lab1 哈工大软件工程实验1

主要任务：获取文本文件里的字符串进行生成单词节点邻接关系有向图
 
## 需求1：ReplaceStr

> main(String[] args)

换行符以及其他标点符号替换为空格，非字母字符删除，连续空格处理，选做：单词状态转换。

*注：更改为GUI框架内处理*

## 需求2：ShowGraph

> type createDirectedGraph(String filename);void showDirectedGraph(type G)

根据生成的节点以及邻接关系生成有向图（GUI部分），选做：将生成图像保存本地

## 需求3：QueryBridgeWords 

> String queryBridgeWords(type G, String word1, String word2)

查询桥接词，s1：存在桥接；s2：不存在桥接；s3：不在图中；s4：多个桥接词

## 需求4：CreatedByGraph 

> String generateNewText(type G, String inputText)

通过桥接图对新输入的文本进行插入桥接词

## 需求5：ShortPathOfWords 

> String calcShortestPath(type G, String word1, String word2)

输出两个词之间最短路径，并凸出标记图输出，选做：单源最短路径输出

## 需求6：String randomWalk(type G) 

> String randomWalk(type G)

随机节点出发随机遍历（重复边或者无节点可寻则停止），用户可手动停止遍历，输出构造文本

## 初步设计主界面原型

![设计原型](https://raw.githubusercontent.com/zhouchang29/se_lab1/master/%E4%B8%BB%E7%95%8C%E9%9D%A2%E8%AE%BE%E8%AE%A1%E5%8E%9F%E5%9E%8B.png)
