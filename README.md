# 四川政务网 事项抓取
www.sczwfw.gov.cn  
其他地区也同理  
代码你不一定能用,重要的是思路


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
![](https://img-blog.csdnimg.cn/20200108163650628.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9saXhkb25nLmJsb2cuY3Nkbi5uZXQ=,size_16,color_FFFFFF,t_70)

# OOSpider
当数据准备就绪,需要使用OOSpider类进行网页数据抓取  
OOSpider是注解式爬虫的入口，有这些方法  
![](https://img-blog.csdnimg.cn/20200108164220556.png)

# 方法
etCollectorPipeline():  
看了下源码，这个和AfterExtractor接口的功能一样,都是在运行时返回数据抽取结果。建议使用AfterExtractor接口,面向接口编程嘛  
两个create方法: 对构造方法的简单封装,因为构造方法是protected!  
addPageModel(): [文档](http://webmagic.io/docs/zh/posts/ch6-custom-componenet/pipeline.html)  
setIsExtractLinks(): 是否提取网页链接
这个需要搭配TargetUrl与HelpUrl注解使用,本质就是链接发现.我们已经发现过了，所以设置为false
具体的你可以看ModelPageProcessor源码
TargetUrl和HelpUrl注解 [文档](http://webmagic.io/docs/zh/posts/ch5-annotation/targeturl.html)

# 入参
ModelPageProcessor : 注解网页对象  在网页抓取章节里详解介绍  
PageProcessor: 普通网页对象  这个是给传统方式使用的,看 [这里](http://webmagic.io/docs/zh/posts/ch4-basic-page-processor/pageprocessor.html)  
Site : http请求对象 , http嘛肯定是配置cookies、header、超时时间、重试次数等等...乱七八糟参数的地方  
PageModelPipeline : 看 [文档](http://webmagic.io/docs/zh/posts/ch6-custom-componenet/pipeline.html) 

# 网页抓取
下面我分成两个部分讲述  
1. 创建OOSpider时 发生了什么  
2.如何设计映射关系(实体类)  

先看一段代码:  
public class GithubRepo {  
    @ExtractBy("//div[@id='readme']/tidyText()")  
    private String readme;  
   
    public static void main(String[] args) {  
        OOSpider.create(Site.me().setSleepTime(1000)  
                , GithubRepo.class)  
                .addUrl("https://github.com/code4craft").thread(5).run();  
    }  
}  
这段代码描述了：

1 . GithubRepo 拥有的字段readme 被@ExtractBy 注释,它的抽取规则为//div[@id='readme']/tidyText()

2.创建一个爬虫  OOSpider.create()

3. 爬虫传入了两个类 : Site类 和 拥有抽取规则的类

4.使用addUrl()，规定了抓取的页面

5.创建线程并运行

 

代码内部过程:

1.将Url pull 到 Scheduler 接口

2.初始化组件：

创建HttpClient、创建线程池、通过Scheduler获取Request集合

3.请求页面

4.抽取数据 ，被@ExtractBy注释的字段都会被PageModelExtractor类，注入对应数据

5.存放数据

存放的数据有二种方式获取

一。Pipeline 接口   可在线程运行和结束时可获取数据

二。AfterExtractor接口    单个线程结束时 可获取数据，我使用这种方式保存数据

 

回到我们代码本身,肯定不能将需要抓取的字段和接口实现放置到一起,但是数据要统一管理

Admin类正是为此而设计  
![](https://img-blog.csdnimg.cn/20200108165712745.png)  
它继承了拥有@ExtractBy注解的实体类,同时要求实现类需要实现AfterExtractor类的方法

Laws类图： 

![](https://img-blog.csdnimg.cn/20200108170150195.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9saXhkb25nLmJsb2cuY3Nkbi5uZXQ=,size_16,color_FFFFFF,t_70)  
在程序入口，需要继承Admin类，实现afterProcess方法  
![](https://img-blog.csdnimg.cn/2020010817044661.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9saXhkb25nLmJsb2cuY3Nkbi5uZXQ=,size_16,color_FFFFFF,t_70)  



