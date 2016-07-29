## Zookeeper中的常用概念（目前只是了解了一遍）
#### 一、角色
**在Zookeeper概述中也提到过，Zookeeper的Follower在接到客户端请求之后会把请求转发到Leader，这里提到的Follower和Leader就是ZK中的角色，ZK中有以下角色：**

###### 1、领导者（leader）
> 负责进行投票的发起和决议，更新系统状态。为客户端提供读和写服务。
###### 2、跟随者（follower）
> 用于接受客户端请求并想客户端返回结果，在选主过程中参与投票。为客户端提供读服务。
###### 3、观察者（observer）
> 可以接受客户端连接，将写请求转发给leader，但observer不参加投票过程，只同步leader的状态，observer的目的是为了扩展系统，提高读取速度
###### 4、客户端（client）
> 请求发起方

#### 二、数据模型


```
graph TB
A((/))-->B((/app_1))
A((/))-->C((/app_2))
B-->D((/app_1/a_1))
B-->E((/app_1/a_2))
B-->F((/app_1/a_3))
```

> ZK中数据是以目录结构的形式存储的。其中的每一个存储数据的节点都叫做Znode，每个Znode都有一个唯一的路径标识。和目录结构类似，每一个节点都可以可有子节点（临时节点除外）。节点中可以存储数据和状态信息，每个Znode上可以配置监视器（watcher），用于监听节点中的数据变化。节点不支持部分读写，而是一次性完整读写。

#### 三、节点

**Znode有四种类型，PERSISTENT（持久节点）、PERSISTENT_SEQUENTIAL（持久的连续节点）、EPHEMERAL（临时节点）、EPHEMERAL_SEQUENTIAL（临时的连续节点）**
> Znode的类型在创建时确定并且之后不能再修改

###### 1、临时节点

> 临时节点的生命周期和客户端会话绑定。也就是说，如果客户端会话失效，那么这个节点就会自动被清除掉。


```java
String root = "/ephemeral";
String createdPath = zk.create(root, root.getBytes(),
          Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
System.out.println("createdPath = " + createdPath);

String path = "/ephemeral/test01" ; 
createdPath = zk.create(path, path.getBytes(),
            Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
System.out.println("createdPath = " + createdPath);
Thread.sleep(1000 * 20); // 等待20秒关闭ZooKeeper连接
zk.close(); // 关闭连接后创建的临时节点将自动删除
```
> 临时节点不能有子节点

###### 2、持久节点

> 所谓持久节点，是指在节点创建后，就一直存在，直到有删除操作来主动清除这个节点——不会因为创建该节点的客户端会话失效而消失。

```java
String root = "/computer";
String createdPath = zk.create(root, root.getBytes(),
       Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
System.out.println("createdPath = " + createdPath);
```

###### 3、临时顺序节点

> 临时节点的生命周期和客户端会话绑定。也就是说，如果客户端会话失效，那么这个节点就会自动被清除掉。注意创建的节点会自动加上编号。


```java
String root = "/ephemeral";
String createdPath = zk.create(root, root.getBytes(),
          Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
System.out.println("createdPath = " + createdPath);

String path = "/ephemeral/test01" ; 
createdPath = zk.create(path, path.getBytes(),
            Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
System.out.println("createdPath = " + createdPath);
Thread.sleep(1000 * 20); // 等待20秒关闭ZooKeeper连接
zk.close(); // 关闭连接后创建的临时节点将自动删除
```
>输出结果：


```java
type = None
createdPath = /ephemeral/test0000000003
createdPath = /ephemeral/test0000000004
createdPath = /ephemeral/test0000000005
createdPath = /ephemeral/test0000000006
```

###### 4、持久顺序节点

> 这类节点的基本特性和持久节点类型是一致的。额外的特性是，在ZooKeeper中，每个父节点会为他的第一级子节点维护一份时序，会记录每个子节点创建的先后顺序。基于这个特性，在创建子节点的时候，可以设置这个属性，那么在创建节点过程中，ZooKeeper会自动为给定节点名加上一个数字后缀，作为新的节点名。这个数字后缀的范围是整型的最大值。


```java
String root = "/computer";
String createdPath = zk.create(root, root.getBytes(),
       Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
System.out.println("createdPath = " + createdPath);
for (int i=0; i<5; i++) {
   String path = "/computer/node";
   String createdPath = zk.create(path, path.getBytes(),
       Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
   System.out.println("createdPath = " + createdPath);
}
zk.close();
```

> 运行结果：


```java
createdPath = /computer
createdPath = /computer/node0000000000
createdPath = /computer/node0000000001
createdPath = /computer/node0000000002
createdPath = /computer/node0000000003
createdPath = /computer/node0000000004
结果中的0000000000~0000000004都是自动添加的序列号
```
> 节点中除了可以存储数据，还包含状态信息。

#### 四、ACL
>每个znode被创建时都会带有一个ACL列表，用于决定谁可以对它执行何种操作。

#### 五、观察（watcher）

> Watcher 在 ZooKeeper 是一个核心功能，Watcher 可以监控目录节点的数据变化以及子目录的变化，一旦这些状态发生变化，服务器就会通知所有设置在这个目录节点上的 Watcher，从而每个客户端都很快知道它所关注的目录节点的状态发生变化，而做出相应的反应

> 可以设置观察的操作：**exists,getChildren,getData**

> 可以触发观察的操作：**create,delete,setData**

> znode以某种方式发生变化时，“观察”（watch）机制可以让客户端得到通知。可以针对ZooKeeper服务的“操作”来设置观察，该服务的其他 操作可以触发观察。比如，客户端可以对某个客户端调用exists操作，同时在它上面设置一个观察，如果此时这个znode不存在，则exists返回 false，如果一段时间之后，这个znode被其他客户端创建，则这个观察会被触发，之前的那个客户端就会得到通知。

---

#### ZooKeeper基本介绍：

#### 一、==**背景**==
###### 1、分布式

> 互联网技术的发展，导致大型网站需要的计算能力和存储能力越来越高。网站架构逐渐从集中式转变成分布式。

###### 2、什么是分布式

> 把一个计算任务分解为若干个计算单元，并分派到若干个不同的计算机中去执行，然后再汇总计算结果。
分布式的工作方式有点类似于团队合作。当有一项任务分配到某个团队之后，团队内部的成员开始各司其职，然后把工作结果统一汇总给团队主管，由团队主管再整理团队的工作成果汇报给公司。


###### 3、分布式存在的问题

> 虽然分布式和集中式系统相比有很多优势，比如能提供更强的计算、存储能力，避免单点故障等问题。但是由于采用分布式部署的方式，就经常会出现网络故障等问题，并且如何在分布式系统中保证数据的一致性和可用性也是一个比较关键的问题。
    比如在集中式系统中，有一些关键的配置信息，可以直接保存在服务器的内存中，但是在分布式系统中，如何保存这些配置信息，又如何保证所有机器上的配置信息都保持一致，又如何保证修改一个配置能够把这次修改同步到所有机器中呢？
    再比如，在集中式系统中，进行一个同步操作要写同一个数据的时候，可以直接使用事务+锁来管理保证数据的ACID。但是，在分布式系统中如何保证多台机器不会同时写同一条数据呢？

---

#### 二、==**概述**==
###### 1、什么是Zookeeper

> Zookeeper是一个开放源码的分布式服务协调组件，是Google Chubby的开源实现。是一个高性能的分布式数据一致性解决方案。他将那些复杂的、容易出错的分布式一致性服务封装起来，构成一个高效可靠的原语集，并提供一系列简单易用的接口给用户使用。

###### 2、Zookeeper提供了哪些特性

> 他解决的分布式数据一致性问题，提供了顺序一致性、原子性、单一视图、可靠性、实时性等。

```
1.顺序一致性：客户端的更新顺序与他们被发送的顺序相一致；
2.原子性：更新操作要么全部成功，要么全部失败；
3.单一试图：无论客户端连接到哪一个服务器，都可以看到相同的ZooKeeper视图；
4.可靠性：一旦一个更新操作被应用，那么在客户端再次更新它之前，其值将不会被改变；
5.实时性：在特定的一段时间内，系统的任何变更都将被客户端检测到；
```
###### 3、Zookeeper工作过程

> 在整个集群刚刚启动的时候，会进行Leader选举，当Leader确定之后，其他机器自动成为Follower，并和Leader建立长连接，用于数据同步和请求转发等。当有客户端机器的写请求落到follower机器上的时候，follower机器会把请求转发给Leader，由Leader处理该请求，比如数据的写操作，在请求处理完之后再把数据同步给所有的follower。

###### 4、CAP理论

> 在分布式领域，有一个著名的理论——CAP理论。CAP理论的核心观点是任何软件系统都无法同时满足一致性、可用性以及分区容错性。

> 值得一提的是，作为一个分布式系统，分区容错性是一个必须要考虑的关键点。一个分布式系统一旦丧失了分区容错性，也就表示放弃了扩展性。因为在分布式系统中，网络故障是经常出现的，一旦出现在这种问题就会导致整个系统不可用是绝对不能容忍的。所以，大部分分布式系统都会在保证分区容错性的前提下在一致性和可用性之间做权衡。

> 在CAP这三个关键的性质中，同时满足CA两点的是著名的数据库中ACID、同时满足AP两点的是注明的BASE理论。

##### 5、Zookeeper和CAP的关系

> 上面介绍过，没有任何一个分布式系统可以同时满足CAP，Zookeeper一般以集群的形式对外提供服务，那么Zookeeper在CAP中是如何取舍的呢？

> ZooKeeper是个CP（一致性+分区容错性）的，即任何时刻对ZooKeeper的访问请求能得到一致的数据结果，同时系统对网络分割具备容错性;但是它不能保证每次服务请求的可用性(注：也就是在极端环境下，ZooKeeper可能会丢弃一些请求，消费者程序需要重新请求才能获得结果)。但是别忘了，ZooKeeper是分布式协调服务，它的 职责是保证数据(注：配置数据，状态数据)在其管辖下的所有服务之间保持同步、一致;所以就不难理解为什么ZooKeeper被设计成CP而不是AP特性的了，如果是AP的，那么将会带来恐怖的后果(注：ZooKeeper就像交叉路口的信号灯一样，你能想象在交通要道突然信号灯失灵的情况吗?)。而且， 作为ZooKeeper的核心实现算法 Zab，就是解决了分布式系统下数据如何在多个服务之间保持同步问题的。

> 如果 ZooKeeper下所有节点都断开了，或者集群中出现了网络分割的故障(注：由于交换机故障导致交换机底下的子网间不能互访);那么ZooKeeper 会将它们都从自己管理范围中剔除出去，外界就不能访问到这些节点了，即便这些节点本身是“健康”的，可以正常提供服务的;所以导致到达这些节点的服务请求 被丢失了。

---

#### 三、==Zookeeper安装==（可以看看）
**Zookeeper有三种安装方式：单机模式、集群模式、伪集群模式**

###### 1、单机模式

> 单机模式表示只运行在一台服务器上，适合测试环境； 安装步骤如下：


```
一、下载ZooKeeper

二、解压

三、在conf目录下创建一个配置文件zoo.cfg

tickTime=2000 dataDir=/Users/zdandljb/zookeeper/data dataLogDir=/Users/zdandljb/zookeeper/dataLog            
clientPort=2181

四、启动ZooKeeper的Server

sh bin/zkServer.sh start, 如果想要关闭，输入：zkServer.sh stop

五、检查是否启动成功

启动后使用命令echo ruok | nc localhost 2181检查 Zookeeper 是否已经在服务。如果正常启动将输出imok
```

###### 2、集群模式

> Zookeeper 不仅可以单机提供服务，同时也支持多机组成集群来提供服务。
安装步骤和单机模式类似，只是配置不太一样。这里以三台机器为例


```
一、分别在不同的机器上下载并解压Zookeeper

二、创建myid文件

在每台机器上都创建一个myid文件。文件的内容只有一个数字。

server1机器的内容为：1 server2机器的内容为：2 server3机器的内容为：3

三、在conf目录下创建一个配置文件zoo.cfg

tickTime=2000 dataDir=/Users/zdandljb/zookeeper/data dataLogDir=/Users/zdandljb/zookeeper/dataLog               
clientPort=2181                      
initLimit=5                         
syncLimit=2                                 
server.1=server1:2888:3888                      
server.2=server2:2888:3888                      
server.3=server3:2888:3888
```

###### 3、伪集群模式

> 实际上 Zookeeper 还支持另外一种伪集群的方式，也就是可以在一台物理机上运行多个 Zookeeper 实例

```
一、安装Zookeeper

建了3个文件夹，server1 server2 server3，然后每个文件夹里面解压一个zookeeper的下载包

二、创建myid文件

进入data目录，创建一个myid的文件，里面写入一个数字，server1,就写一个1，server2对应myid文件就写入2，server3对应myid文件就写个3

三、在conf目录下创建一个配置文件zoo.cfg

tickTime=2000  dataDir=/Users/zdandljb/zookeeper/data dataLogDir=xxx/zookeeper/server1/         
clientPort=2181                              
initLimit=5                           
syncLimit=2                                 
server.1=server1:2888:3888                      
server.2=server2:2889:3889                                     
server.3=server3:2890:3890
为了防止端口冲突，要配置不同的端口号。
```


###### 4、常用参数

> dataDir：用于存放内存数据库快照的文件夹，同时用于集群的myid文件也存在这个文件夹里。

> dataLogDir：用于单独设置transaction log的目录，transaction log分离可以避免和普通log还有快照的竞争。

> tickTime：心跳时间，为了确保client-server连接存在的，以毫秒为单位，最小超时时间为两个心跳时间。

> clientPort：客户端监听端口。

> initLimit：初始化连接时最长能忍受多少个心跳时间间隔数

> syncLimit：这个配置项标识 Leader 与 Follower 之间发送消息，请求和应答时间长度，最长不能超过多少个 tickTime 的时间长度

> server.x=[hostname]:nnnnn[:nnnnn]

```
配置集群里面的主机信息，其中：
　　1.server.x：server.x的x要写在myid文件中，决定当前机器的id，
　　2.第一个port用于连接leader，
　　3.第二个用于leader选举。
　　4.如果electionAlg为0，则不需要第二个port。
　　5.hostname也可以填ip。
```
　　
> 伪集群模式安转时，后面连着的2个端口3个server都不要一样，否则端口冲突。

> electionAlg

```
用于选举的实现的参数：
　　1：LeaderElection
　　2：AuthFastLeaderElection
　　3：FastLeaderElection
```
**zookeeper默认使用FastLeaderElection进行Leader选举**

