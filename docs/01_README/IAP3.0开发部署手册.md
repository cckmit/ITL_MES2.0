# IAP3.0使用手册

## 前言

**IAP3.0**需要环境如下:

> - Java: 1.8
>- Maven: 3.3+
> - MySQL: 5.7+
>   - 不支持MySQL8.0+
> - Redis: 4.0+
  - Nacos: 1.2.1+
>- Sentinel: 1.2.1+
> - Node.js: 10.15.0+
>- Npm: 5.6.0+

## 所需插件

>Lombok Plugin

## 推荐开发工具

> - 后端使用：IntelliJ IDEA
> - 前端使用： VsCode、WebStorm

## 一、项目下载

1. 安装git，下载地址是[git官网][https://git-scm.com/downloads]([点击下载][https://git-scm.com/download/win])

2. 新建文件夹，最好为英文名project

3. 进入文件夹，空白处右键，选择gitbash here,键入git init会生成.git文件

4. 配置本地仓库的账号邮箱git

   ```git config --global user.name "用户名"```

   ``` git config --global user.email "电子邮件" ```

5. 复制项目地址，用`git clone`指令克隆GitLab上的项目

   `git clone http://120.25.218.127:6088/iap/iap`
## 二、数据库

1. 打开Navicat（此处可以选择其他的客户端），新建一个数据库iap
2. 找到Iap工程doc->02_DB文件夹下的sql脚本 ,按编号顺序进行执行
3. 执行脚本(根据文件名称的顺序执行脚本)

## 三、导入项目

好了现在我们已经把基础环境都搞定，下面开始用IntelliJ IDEA导入工程：

- 打开IntelliJ IDEA后，点击<kbd>Open or Import</kbd>会出现电脑磁盘目录，找到刚才从git克隆的项目所在目录后点击<kbd>ok</kbd>即可打开项目

如果刚才用git拉取代码失败，也可以用IntelliJ IDEA里的git来拉取代码：

1. 进入GitLab项目首页
2. 点击<kbd>Clone</kbd>，在出现的弹窗中复制<kbd>Clone with HTTP</kbd>下的项目地址
3. 打开IntelliJ IDEA，依次选择：<kbd>File</kbd>-><kbd>Project from Version Control</kbd>-><kbd>Git</kbd>
4. 若右下角弹出如下提示，则点击<kbd>Add as Maven Project</kbd>，等待依赖jar包下载完毕
5. 若右下角弹出如下提示，则点击<kbd>Show run configurations in Run Dashboard</kbd>

两种方式任选其一即可。

## 四、环境配置

### nacos配置

1. 下载nacos-server-1.2.1

   > 1. 点击链接下载    [nacos-server-1.2.1](https://github.com/alibaba/nacos)
   >
   > 2. 解压至安装目录

2. nacos 数据持久化配置

   > 修改配置nacos-server-1.2.1\nacos\conf,文件信息如下所示

   ```yml
   #1.打开 spring.datasource.platform、db.num的注释
   #2.修改数据库连接、用户名、密码
   #*************** Config Module Related Configurations ***************#
   ### If user MySQL as datasource:
   spring.datasource.platform=mysql
   
   ### Count of DB:
   db.num=1
   
   ### Connect URL of DB:
   db.url.0=jdbc:mysql://127.0.0.1:3306/iap?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true
   db.user=root
   db.password=tj123456
   ```

3. 初始化IAP3.0配置信息

   > 初始化脚本，在IAP3.0项目工程里，路径为：\docs\db\01_nacos-mysql.sql
   >
   > 使用Navicat for MySQL，运行SQL文件。
   >
   > 确保数据初始化完成，表信息如下所示：
   >
   > config_info,
   >
   > config_info_aggr,
   >
   > config_info_beta,
   >
   >  config_info_tag, 
   >
   >  config_tags_relation, 
   >
   > group_capacity, 
   >
   > his_config_info, 
   >
   > roles,
   >
   > tenant_capacity,
   >
   > tenant_info, 
   >
   >  users
   
4. 运行

   1. 进入bin目录，打开cmd，输入startup.cmd

   2. 访问
      1. 访问地址:     http://127.0.0.1:8848/nacos/

      2. 账号/密码：nacos/iap3#0

5. 默认密码修改

   ```java
   /**
    * nacos重置用户密码
    *
    * @author 汤俊
    * @date 2020-5-29 15:32
    * @since 1.0.0
    */
   public class Test {
       public static void main(String[] args) {
           System.out.println("psw = " + new BCryptPasswordEncoder().encode("iap3#0"));
       }
   }
   ```

6. 在项目中使用nacos，配置信息如下所示：

   ```yam
   server:
     port: 11112
   spring:
     application:
       name: iap-feignprovider #设置项目名称
     #profiles:
     #active: dev
     cloud:
       nacos:
         discovery:
           server-addr: ${NACOS_HOST:localhost:8848} #Nacos服务接口(不能加http前缀)，直接访问localhost:8848/nacos可以进入管理页面
           namespace: ${NAME_SPACE:dev}
         config:
           server-addr: ${NACOS_HOST:localhost:8848}
           file-extension: yml #配置文件的格式，默认为properties
           namespace: ${NAME_SPACE:dev}
           group: iap
   
   #shared-dataids: mysql.yml #需要使用的配置文件
   #refreshable-dataids: mysql.yml #需要实时刷新的配置文件
   ```


### Sentinel配置

1. 下载Sentinel的Jar文件

2. 运行Sentinel

   > 参数说明:
   >
   > -Dserver.port=9901 sentinel控制台端口。
   >
   > -Dcsp.sentinel.dashboard.server=localhost:9901 控制台地址，指定控制台后客户端会自动向该地址发送心跳包。
   >
   > -Dproject.name=sentinel-dashboard 指定Sentinel控制台程序的名称。
   >
   > 注：如果启动报错，请检查9901端口是否被占用！


   ```java
   java -Dserver.port=9901 -Dcsp.sentinel.dashboard.server=localhost:9901 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard-1.7.2.jar
   ```

3. 访问sentinel的dashboard页面，地址：http://127.0.0.1:8080

4. 默认账号/密码: sentinel/sentinel

5. 在代码中使用sentinel

   ```java
       @GetMapping("/getUserId/{userId}")
       @SentinelResource("provider1")
       public String getUserId(@PathVariable("userId") String userId) {
           return "provider-01: " + userId;
       }
   ```

   配置信息：

   ```yml
   spring: 
     cloud:
       sentinel:
           transport:
             port: 8719
             dashboard: ${SENTINEL_HOST:localhost:8080}
   
   management:
     endpoints:
       web:
         exposure:
           include: '*'
   
   feign:
     sentinel:
       enabled: true
   ```
   
6. SentinelResource配置：

| 属性                      | 作用                                                         | 是否必须  |
| :------------------------ | :----------------------------------------------------------- | :-------- |
| value                     | 资源名称                                                     | 是        |
| entryType                 | entry类型，标记流量的方向，取值IN/OUT，默认是OUT             | 否        |
| blockHandler              | 处理BlockException的函数名称。函数要求： 1. 必须是 `public` 2.返回类型与原方法一致 3. 参数类型需要和原方法相匹配，**并在最后加 `BlockException` 类型的参数**。 4. 默认需和原方法在同一个类中。若希望使用其他类的函数，可配置 `blockHandlerClass` ，并指定blockHandlerClass里面的方法。 | 否        |
| blockHandlerClass         | 存放blockHandler的类。对应的处理函数必须static修饰，否则无法解析，其他要求：同blockHandler。 | 否        |
| fallback                  | 用于在抛出异常的时候提供fallback处理逻辑。fallback函数可以针对所有类型的异常（除了 `exceptionsToIgnore` 里面排除掉的异常类型）进行处理。函数要求： 1. 返回类型与原方法一致 2. 参数类型需要和原方法相匹配，**Sentinel 1.6开始，也可在方法最后**加 `Throwable` 类型的参数。 3.默认需和原方法在同一个类中。若希望使用其他类的函数，可配置 `fallbackClass` ，并指定fallbackClass里面的方法。 | 否        |
| fallbackClass【1.6】      | 存放fallback的类。对应的处理函数必须static修饰，否则无法解析，其他要求：同fallback。 | 否        |
| defaultFallback【1.6】    | 用于通用的 fallback 逻辑。默认fallback函数可以针对所有类型的异常（除了 `exceptionsToIgnore` 里面排除掉的异常类型）进行处理。若同时配置了 fallback 和 defaultFallback，以fallback为准。函数要求： 1. 返回类型与原方法一致 2. 方法参数列表为空，**或者有一个** `Throwable` 类型的参数。 3. 默认需要和原方法在同一个类中。若希望使用其他类的函数，可配置 `fallbackClass` ，并指定 `fallbackClass` 里面的方法。 | 否        |
| exceptionsToIgnore【1.6】 | 指定排除掉哪些异常。排除的异常不会计入异常统计，也不会进入fallback逻辑，而是原样抛出。 | 否        |
| exceptionsToTrace         | 需要trace的异常                                              | Throwable |

## 五、启动项目

1. 先启动`redis`、`nacos`、`sentinel`等基础服务
    - redis启动:
        1. 进入redis文件目录
        2. 打开Redis-x64-3.2.100目录
        3. 双击运行redis-server.exe文件
    - nacos启动:
        1. 进入nacos文件目录
        2. 打开bin文件夹
        3. 双击运行startup.cmd文件
    - sentinel启动:
        1. 打开sentinel-dashboard-1.7.2.jar文件所在目录
        2. 在windows资源管理器导航栏输入`cmd`即可打开命令提示符
        3. 输入`java -jar sentinel-dashboard-1.7.2.jar`
2. 再启动`IapSystemFeignProvider`、`IapGatewayApplication`、`IapAuthProvider`基础服务模块，其他模块根据需要启动。

## 六、项目结构

项目模块结构如下：

``` xml
├── iap-auth -- 授权服务提供
├── iap-common -- 常用工具封装包
├── iap-example -- DEMO模块
├── iap-base -- 模块依赖
├── iap-gateway -- Spring Cloud 网关
├── iap-ops -- 运维中心
├── iap-service -- 业务模块
├    ├── iap-workflow -- 工作流模块 
├    ├── iap-system -- 系统模块 
└──  └── itl-notice -- 消息中心模块 
```

## 七、验证服务是否启动成功

- 打开<http://localhost:8848/nacos>，若出现如
![nacos](http://120.25.218.127:6088/iap/iap/raw/feature/system/docs/picture/nacos.png)
图片所示，则服务启动成功

## 八、服务调用测试

1. 打开接口文档系统，http://192.168.8.141:11119/swagger-ui.html#/login-controller
2. 选择`/login`接口进行测试

至此，项目启动成功，api调用成功！
