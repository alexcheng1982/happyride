# Happyride - 快乐出行

![build status](https://github.com/alexcheng1982/happyride/workflows/maven/badge.svg)

Happyride is a microservice application written using Spring Boot.

<a href="https://www.buymeacoffee.com/alexcheng1982" target="_blank"><img src="https://cdn.buymeacoffee.com/buttons/v2/default-yellow.png" alt="Buy Me A Coffee" style="height: 60px !important;width: 217px !important;" ></a>

---

与云原生和微服务相关的内容，会更新在[我的B站](https://space.bilibili.com/1094957548)。

除了B站之外，还可以关注：

* 我的微博[@alexcheng1982](https://weibo.com/alexcheng1982)
* 微信公众号『灵动代码』。
* 个人网站[vividcode.cc](https://vividcode.cc)

![公众号](gongzhonghao.jpg)

# 更新日志

* 2022-05-31 更新至 Spring Boot `2.7.0`
* 2022-05-04 更新至 Spring Boot `2.6.7`
* 2022-04-03 更新至 Spring Boot `2.6.6`
* 2022-02-08 调整项目结构，版本设置为`2.0.0`
* 2022-02-05 更新至 Spring Boot `2.6.3`
* 2021-10-24 更新至 Spring Boot `2.5.6`
* 2021-07-14 更新至 Spring Boot `2.5.2`，相关的依赖也进行了更新

# 本地开发

项目使用Maven构建，推荐使用IntelliJ IDEA开发。

本地开发需要Docker Compose的支持，在`devops/dev`目录下有开发所需的Docker Compose文件。

# 本地部署

请参考`devops/k8s`目录下的文档来部署到Kubernetes。

# 服务列表

下表是应用的服务及其说明。

| 服务名称  | Maven模块   |  API本地端口  |
|---|---|---|
| 乘客管理服务 |  `passenger-service`  |  `8500` |
| 行程管理服务 |  `trip-service`  |  `8501` |
| 地址管理服务  |  `address-service`  | `8502`  |
| 司机管理服务 |  `driver-service`  |  `8503` |
| 行程派发服务  |  `dispatch-service`  | 无  |
| 支付服务  |  `payment-service`  | `8504`  |
| 行程验证服务  |  `payment-service`  | `8505`  |
| 历史行程服务  |  `trip-history-service`  | `8506`  |
| 乘客管理界面的GraphQL服务  |  `passenger-web-api-graphql`  | `8610`  |

在Minikube中访问服务，首先显示乘客API服务的URL：

```sh
$ minikube service --url passenger-api-graphql -n happyride
```

再访问GraphQL提供的GraphiQL的界面 `<url>/graphiql`.