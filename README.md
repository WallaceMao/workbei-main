# 功倍后台api

基于日事清api后台原有功能扩展的接口，主要扩展的接口有两类：

1. 日事清api后台未实现的功能
2. 日事清api后台性能有问题的接口

**注意**
1. 功倍扩展api使用的是springMVC + myBatis的架构，不同于日事清api中grails（springMVC + Hibernate）的架构，因此在日事清api后台中不能开启二级缓存，防止数据库的直接更改不能获取到。