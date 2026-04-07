## 本项目完整源码是收费的  接毕业设计和论文

### 作者微信：grapro666 QQ：3642795578 (支持部署调试、支持代做毕设)

### 接javaweb、python、小程序、H5、APP、各种管理系统、单片机、嵌入式等开发

### 选题+开题报告+任务书+中期报告+程序定制+安装调试+论文+答辩ppt

**博客地址：
[https://blog.csdn.net/2303_76227485/article/details/159416757](https://blog.csdn.net/2303_76227485/article/details/159416757)**

**视频演示：
[https://www.bilibili.com/video/BV1DLPTzFEnE/](https://www.bilibili.com/video/BV1DLPTzFEnE/)**

**毕业设计所有选题地址：
[https://github.com/ynwynw/allProject](https://github.com/ynwynw/allProject)**

## 基于Springboot+Vue的教务管理系统(源代码+数据库)
**项目编号：268**
## 一、系统介绍
本项目前后端分离，分为用户、教师、管理员3种角色。
### 1、用户：
- 登录、已修学分、平均绩点、通过课程、成绩分布环状图、各科成绩柱状图、学期绩点趋势折线图、公告查看
- 选课、我的选课、退选、我的成绩、评教、我的信息
### 2、教师：
- 登录、公告查看、我的课程查看、查看我的评价、成绩录入、教学资料上传、个人信息
### 3、管理员：
- 系统管理：学院管理、专业管理、班级管理(学生导入导出)、角色管理、操作日志
- 公告查看、学生管理、教师管理、课程管理、授课分配、选课配置
- 成绩管理(成绩导出)、成绩统计、成绩权重配置、公告管理、教学资料管理、评教管理、评教安全配置
### 4、亮点：
- 使用redis缓存验证码和token，既简化业务开发，又能支撑高并发、高可用的系统需求
- 后台首页大屏使用echarts图表统计，更直观的看出系统的运行数据
## 二、所用技术
后端技术栈：
- Springboot
- Mybatis
- Redis
- Mysql
- Maven

前端技术栈：
- Vue
- Vue-router
- axios
- elementUi
- echarts

## 三、环境介绍
基础环境 :IDEA/eclipse, JDK17或以上, Mysql5.7及以上, Maven3.6, node14, navicat, redis5

所有项目以及源代码本人均调试运行无问题 可支持远程调试运行

## 四、页面截图
### 1、用户：
![contents](./picture/picture1.png)
![contents](./picture/picture2.png)
![contents](./picture/picture3.png)
![contents](./picture/picture4.png)
![contents](./picture/picture5.png)
![contents](./picture/picture6.png)
![contents](./picture/picture7.png)
![contents](./picture/picture8.png)
### 2、教师：
![contents](./picture/picture9.png)
![contents](./picture/picture10.png)
![contents](./picture/picture11.png)
![contents](./picture/picture12.png)
![contents](./picture/picture13.png)
![contents](./picture/picture14.png)
![contents](./picture/picture15.png)
![contents](./picture/picture16.png)
![contents](./picture/picture17.png)
### 3、管理员：
![contents](./picture/picture18.png)
![contents](./picture/picture19.png)
![contents](./picture/picture20.png)
![contents](./picture/picture21.png)
![contents](./picture/picture22.png)
![contents](./picture/picture23.png)
![contents](./picture/picture24.png)
![contents](./picture/picture25.png)
![contents](./picture/picture26.png)
![contents](./picture/picture27.png)
![contents](./picture/picture28.png)
![contents](./picture/picture29.png)
![contents](./picture/picture30.png)
![contents](./picture/picture31.png)
![contents](./picture/picture32.png)
![contents](./picture/picture33.png)
![contents](./picture/picture34.png)
![contents](./picture/picture35.png)
![contents](./picture/picture36.png)
![contents](./picture/picture37.png)
![contents](./picture/picture38.png)
![contents](./picture/picture39.png)
![contents](./picture/picture40.png)
![contents](./picture/picture41.png)
![contents](./picture/picture42.png)
![contents](./picture/picture43.png)
![contents](./picture/picture44.png)
![contents](./picture/picture45.png)
![contents](./picture/picture46.png)
![contents](./picture/picture47.png)
![contents](./picture/picture48.png)
![contents](./picture/picture49.png)
![contents](./picture/picture50.png)
![contents](./picture/picture51.png)
## 五、浏览地址
前台地址：http://localhost:8800

- 用户账号密码：2024001066/123456

- 教师账号密码：T2020001/123456

- 管理员账户密码：admin/123456

## 六、部署教程
1. 使用Navicat或者其它工具，在mysql中创建对应名称的数据库，并执行项目的sql文件

2. 使用IDEA/Eclipse导入university-education项目，若为maven项目请选择maven，等待依赖下载完成

3. 修改application.yml里面的数据库配置和redis配置, src/main/java/com/edu/EduApplication.java启动后端项目

4. vscode或idea打开university-education-vue项目

5. 在编译器中打开terminal，执行npm install 依赖下载完成后执行 npm run dev,执行成功后会显示访问地址
