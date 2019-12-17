## IM开发 -- Documentation

1. **相关技术支持**
   1. implementation by yourself based on IM protocols
   2. 网易云信 SDK，UIKIT
   3. WebSocket, Socket.IO
      1. 长链接，
   4. **推送方案 (Socket , Netty)** 

2. 推送平台

   + 友盟，腾讯，**个推**

3. 存储平台

   + **阿里OSS**

   

---

## 项目资料

1. **推送相关账户** -- 使用了**个推**

   + **官网**：https://dev.getui.com/dev/#/overview
   + **package name:** com.ethan.imapp
   + **appId:** Ywh3jX3Mp69ljalCAOKeS5
   + **appSecret**：0OdpDpvVYI6IExJKKH2Vr7

   - **appKey：**TCc38iOkNS6e0pV4UOM4J6
   - **masterSecret:**【个推】nZtcAAayeZ74uLQftUeg19

2. **阿里云相关账户**

   +  [20191216173505.csv](/Users/Ethan/Desktop/APK/20191216173505.csv) 

   + | AccessKeyID：LTAI4FcJvJqKBATQSHtdBWLQ | AccessKeySecret：Q6ocdnZ211qGhyrSJq2c5jMEMugoor |
     | ------------------------------------- | ----------------------------------------------- |
     |                                       |                                                 |



## 环境搭建

-  **java**
   -  **java8**
-  **Gradle** 
-  **Mysql**
-  **Tomcat**

---

### Step 1. Tomcat Configuration

1. Tomcat服务器是一个免费的开放源代码的Web 应用服务器，属于轻量级应用服务器，在中小型系统和并发访问用户不是很多的场合下被普遍使用，是开发和调试JSP 程序的首选。
2. version: 9.0.30

### Step 2: MySql Configuration

1. installed version: **MySQL v.8.0.18**
2. 几个需要注意的点:
   + Class.forName**("com.mysql.cj.jdbc.Driver");**
   +  **url = "jdbc:mysql://localhost:3306/<font color=purple>database_name</font>?<font color=red>useSSL=false</font>&<font color=red>serverTimezone=GMT</font>","root","password");**
3. **database_name** here is **iTalker** 
   + mysql> CREATE DATABASE iTalker;

### Step 3: Hibernate Configuration

1. JDBC: Java Database Connectivity
   + providing a set of Java API for accessing the relational databases from Java program
   + These Java APIs enables Java programs to execute SQL statements and interact with any SQL compliant database.
2. Hibernate is an **O**bject-**R**elational **M**apping (ORM) solution for JAVA.

