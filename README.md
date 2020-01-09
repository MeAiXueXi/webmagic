# webmagic
webmagic 爬取www.sczwfw.gov.cn
爬取的是四川政务网,保存事项信息


# 目录结构
![](https://img-blog.csdnimg.cn/2020010816214447.png)
#
entity : 网页字段对应的实体类 
main : 爬虫入口
utils : 工具类 
Admin : 对所有提取到的数据进行集中管理
UrlCache: 网页缓存的抽象

# 生命周期
![](https://img-blog.csdnimg.cn/20200108162414767.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9saXhkb25nLmJsb2cuY3Nkbi5uZXQ=,size_16,color_FFFFFF,t_70)

# 链接发现
这是爬虫的开始,对链接进行发现和更新

例如:已经抓取了500个成都的事项,但是现在需要更新500个事项为其他地区的

按照生命周期,你需要在这里重新更新链接

 

在这一步,我们需要确认要抓取的网页是哪些,并观察网页是否有对外暴露的查询接口,经过调试,找到了这个接口:

http://mss.sczwfw.gov.cn/app/powerDutyList/getThImplement
他支持的参数:

eventNam: 事项名称

areaCode : 区域码

eventType ： 行政类型 

打开PostMan测试接口,并查看返回参数:
![](https://img-blog.csdnimg.cn/20200108163134895.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9saXhkb25nLmJsb2cuY3Nkbi5uZXQ=,size_16,color_FFFFFF,t_70)
#
它返回了事项的基本信息,其中还有父级事项的名称
循环调用这个接口，然后将其返回的数据进行保存,具体请参考utils包下的InsertItems类,看我将其保存至数据库
