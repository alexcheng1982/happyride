# Kubernetes本地开发安装指南

## 安装前提

安装需要如下工具的支持：

* Kubernetes集群，可以使用Minikube或Docker Desktop

如果使用Minikube，确保至少有8G内存和4个CPU。如果资源不足，会出现各种错误。通过下面的命令来设置Minikube的资源。

```bash
$ minikube config set memory 8192
$ minikube config set cpus 4
```

更改Minikube的资源设置之后，需要删除已有的Minikube，再重新启动。

Minikube启用`metrics-server`组件：

```bash
$ minikube addons enable metrics-server
```

* [kubectl](https://kubernetes.io/docs/reference/kubectl/kubectl/)

```bash
$ brew install kubectl
```

* [Helm](https://helm.sh/) - 版本 `3.2.4_1`

```bash
$ brew install helm
```

* [helmfile](https://github.com/roboll/helmfile) - 版本 `0.120.0`

```bash
$ brew install helmfile
```

* [helm-diff](https://github.com/databus23/helm-diff)

```bash
$ helm plugin install https://github.com/databus23/helm-diff --version master
```

## 安装Istio

使用概要文件`demo`来安装Istio，包含了所需的全部组件。

```bash
$ istioctl install --set profile=demo
```

## 构建容器镜像

首先把开发环境中的Docker客户端指向Minikube中的Docker服务器。

```bash
$ eval $(minikube docker-env)
```

通过下面命令查看Docker容器，确认已切换成功。在下面命令的输出中，你应该看到的都是Kubernetes的容器。

```bash
$ docker ps
```

通过Maven来创建镜像。

```bash
$ mvn clean package -DskipTests
```

通过下面命令查看构建出来的Docker镜像，应用的镜像以`happyride/`开头。

```bash
$ docker images
```

相关的镜像如下所示。

```
happyride/happyride-passenger-service           1.0.0     21c513c6a18d        40 years ago        276MB
happyride/happyride-address-service             1.0.0     4fa5004470b3        40 years ago        257MB
happyride/happyride-passenger-web-api-graphql   1.0.0     566530883004        40 years ago        246MB
```

## 安装

创建名称空间`happyride`，

```sh
$ kubectl create ns happyride
```

启用Istio的自动注入，

```sh
$ kubectl label namespace happyride istio-injection=enabled
```

通过下面的命令来安装：

```bash
$ helmfile apply
```
