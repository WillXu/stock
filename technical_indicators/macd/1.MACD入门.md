概念
------

MACD称为指数平滑异同平均线，是从双指数移动平均线发展而来的，由快的指数移动平均线（EMA）减去慢的指数移动平均线，MACD的意义和双移动平均线基本相同，但阅读起来更方便。当MACD从负数转向正数，是买的信号。当MACD从正数转向负数，是卖的信号。当MACD以大角度变化，表示快的移动平均线和慢的移动平均线的差距非常迅速的拉开，代表了一个市场大趋势的转变。

计算步骤
------

1. 12日EMA(移动平均值)：

	EMA12 = 前一日EMA12 X 11/13 + 今日收盘 X 2/13
2. 26日EMA(移动平均值)：

	EMA26 = 前一日EMA26 X 25/27 + 今日收盘 X 2/27
3. 差离值（DIF，同花顺中macd中的白线）的计算：

	DIF = EMA12 - EMA26
4. 根据差离值计算其9日的EMA，即离差平均值，是所求的MACD值

	DEA = （前一日DEA X 8/10 + 今日DIF X 2/10）
5. 计算出的DIF与DEA为正或负值，因而形成在0轴上下移动的两条快速与慢速线。为了方便判断，用DIF减去DEA，用以绘制柱状图。

缺点
------

1. 由于MACD是一项中、长线指标，买进点、卖出点和最低价、最高价之间的价差较大。当行情忽上忽下幅度太小或盘整时，按照信号进场后随即又要出场，买卖之间可能没有利润，也许还要赔点价差或手续费。可以看低级别的k线。
2. 一两天内涨跌幅度特别大时，MACD来不及反应，因为MACD的移动相当缓和，比较行情的移动有一定的时间差，所以一旦行情迅速大幅涨跌，MACD不会立即产生信号，此时，MACD无法发生作用。

基本用法
------

1. MACD 金叉：DIFF 由下向上突破 DEA,为买入信号。
2. MACD 死叉：DIFF 由上向下突破 DEA,为卖出信号。
3. MACD 绿转红：MACD 值由负变正,市场由空头转为多头。
4. MACD 红转绿：MACD 值由正变负,市场由多头转为空头。
5. DIFF 与 DEA 均为正值,即都在零轴线以上时,大势属多头市场,DIFF 向上突破 DEA,可作买。
6. DIFF 与 DEA 均为负值,即都在零轴线以下时,大势属空头市场,DIFF 向下跌破 DEA,可作卖。
7. 当 DEA 线与 K 线趋势发生背离时为反转信号。
8. DEA 在盘整局面时失误率较高,但如果配合 RSI 及 KD 指标可适当弥补缺点。
9. MACD 柱线变红前在0轴下方整理的时间越长，未来股价上涨的空间就越大
10. macd 柱线与股价顶背离是看跌信号，当两者连续出现三次背离时，背离形态已经完成

概念回顾
------

* EMA：移动平均线，macd计算默认12日快速移动平均线和26日慢速移动平均线
* DIF：差离值
* DEA：离差平均值
* MACD红绿柱：（DIF-DEA）× 2

实战技术
------
![](http://www.net767.com/gupiao/UploadFiles_2010/200903/2009032414121672.gif)

![](http://www.net767.com/gupiao/UploadFiles_2010/201004/2010042017300828.gif)

![](http://www.net767.com/gupiao/UploadFiles_2010/201004/2010042017302017.gif)

![](http://www.net767.com/gupiao/UploadFiles_2010/201004/2010042017310879.gif)

![](http://www.net767.com/gupiao/UploadFiles_2010/201004/2010042017313352.gif)

![](http://www.net767.com/gupiao/UploadFiles_2010/201004/2010042017314927.gif)

![](http://www.net767.com/gupiao/UploadFiles_2010/201004/2010042017322379.gif)

![](http://www.net767.com/gupiao/UploadFiles_2010/200903/2009031018390175.gif)

![](http://www.net767.com/gupiao/UploadFiles_2010/200903/2009031018383283.gif)

### 二次金叉
![](http://www.net767.com/book/UploadFiles_8829/201404/2014040113314370.gif)

1. 第二次金叉离第一次金叉距离越近越好；
2. MACD第二次金叉的位置以高于第一次金叉为好；
3. MACD第二次金叉时结合K线形态上的攻击形态研判（如：多方炮、平台突破等）,则可增加成功率。

最佳买点

1. 激进型玩家,可根据量能配合情况,于MACD第二次金叉的当天介入；
2. 稳健型玩家,可于金叉的次日,寻找时机介入。

案例

![](http://www.net767.com/book/UploadFiles_8829/201404/2014040113315624.gif)
![](http://www.net767.com/book/UploadFiles_8829/201404/2014040113321373.gif)
![](http://www.net767.com/book/UploadFiles_8829/201404/2014040113322753.gif)

这三个图的横盘震荡macd回归零轴


参考
------

http://baike.baidu.com/link?url=gEJVyIJz88isnmcBDOIkd3hvqvjvRud2T9khvFblQJnj_6rszvHfJFKsnxrHdbd3wpUwGnAXZ788EsBBP0ZB-gkoR3adPfzSi4RT82GufEebfw9jWDV6I3w0cB2fzatiyWpZkGdujhTNg7J9TqAbIa
