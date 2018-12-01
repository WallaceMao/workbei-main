# 功倍后台api

基于日事清api后台原有功能扩展的接口，主要扩展的接口有两类：

1. 日事清api后台未实现的功能
2. 日事清api后台性能有问题的接口

# 分层设计

## 参数传递

1. VO：客户端自动装箱的对象，对应于页面传值的json，字段一般与客户端请求过来的参数保持一致

2. DO：service层与dao层进行数据传递的对象，字段一般与数据库表记录严格保持一致

## 分层
1. controller（listener）层：

- controller层接收客户端发送过来的数据，自动封装成VO
- controller层调用service层时，通过VO进行参数传递。
- controller层返回VO对象，并自动转换为json格式返回给客户端

2. service层

- service接收上层传递的VO参数，进行业务逻辑的判断
- service调用dao层时，通过DO进行参数传递
- 返回上层只能返回VO

3. dao层

- dao层进行数据库操作时，通过DO进行

**注意**
1. 功倍扩展api使用的是springMVC + myBatis的架构，不同于日事清api中grails（springMVC + Hibernate）的架构，因此在日事清api后台中不能开启二级缓存，防止数据库的直接更改不能获取到。

# 测试
## JUnit
使用jUnit4作为基本的测试框架。需要的注释有：
- `@RunWith(SpringJUnit4ClassRunner.class)`：注明使用的Runner
- `@ContextConfiguration(locations = {"classpath:spring-context.xml"})`：注明使用的xml路径
- `@Transactional(transactionManager = "transactionManager")`：注明测试时基于事务的
- `@Rollback`：注明测试完成后，不保存数据库，而直接回滚数据

其中，可以直接将Test继承`BaseUnitTest`，这样就继承了`@RunWith(SpringJUnit4ClassRunner.class)`和`@ContextConfiguration(locations = {"classpath:spring-context.xml"})`

## AssertJ
作为语句框架

## Mockito
一般情况下，可以直接对数据库操作，不需要mock，如果遇到需要mock的情况，那么就使用Mockito