# Kubernetes上应用安装指南

## 安装前提

安装需要如下工具的支持：

* Kubernetes集群，可以使用Minikube或Docker Desktop

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

## 安装

通过下面的命令来安装：

```bash
$ helmfile apply
```
