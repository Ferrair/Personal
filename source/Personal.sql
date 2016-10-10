-- MySQL dump 10.13  Distrib 5.7.12, for osx10.9 (x86_64)
--
-- Host: localhost    Database: personal
-- ------------------------------------------------------
-- Server version	5.7.15

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin` (
  `username` char(40) NOT NULL,
  `password` char(40) NOT NULL,
  `lastModified` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES ('1906362072@qq.com','ahgdldwqh123','2016-09-23 16:58:53');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blog`
--

DROP TABLE IF EXISTS `blog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `blog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` tinytext NOT NULL,
  `type` tinytext COMMENT '类型--原创，转载，',
  `abstractStr` varchar(200) DEFAULT NULL COMMENT '摘要',
  `content` text NOT NULL,
  `createdAt` datetime NOT NULL,
  `tagId` int(11) NOT NULL COMMENT '外键-到tag表',
  `times` int(11) NOT NULL COMMENT '浏览次数',
  PRIMARY KEY (`id`),
  KEY `BelongTag_idx` (`tagId`),
  CONSTRAINT `BelongTag` FOREIGN KEY (`tagId`) REFERENCES `tag` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blog`
--

LOCK TABLES `blog` WRITE;
/*!40000 ALTER TABLE `blog` DISABLE KEYS */;
INSERT INTO `blog` VALUES (1,'[Java GC]Algorithm For GC','原创','GC算法的一些描述，包扩Reference Counting,Stop_and_Copy,','大二学生党，最近在Java学习有所体会，但广度深度不够，敬请谅解。\n![这里写图片描述](http://img.blog.csdn.net/20160326004204838)\n首先看一下本文所讲述的几个内存的回收算法。只是算法的理论介绍，很枯燥啦（大部分是从维基百科看的，加上自我理解）\n\n一：基本知识\n------\n**1.什么是GC**：简单的说说\n\n> In computer science, garbage collection (GC) is a form of automatic\n> memory management. The garbage collector, or just collector, attempts\n> to reclaim garbage, or memory occupied by objects that are no longer\n> in use by the program.\n\n**2.什么样的Object可以被GC收集**（这里说的是什么是可达的对象 [Reachability of an object](https://en.wikipedia.org/wiki/Tracing_garbage_collection#Reachability_of_an_object)）：\n\n - 所有调用栈（call stack）上的对象，其中包括：所有的函数中的局部变量，参数；全局变量（包括静态变量）；存活的线程，\n - 所有引用上面这些对象的对象都是可达的（这是一个迭代的过程）\n\n```\nPerson p = new Person();\np.car = new Car(RED);\np.car.engine = new Engine();\np.car.horn = new AnnoyingHorn();\n```\n对于上面的代码，可以把看成一个树\n\n```\n     Person [p]\n        |\n     Car (red)\n   /           \\\nEngine    AnnoyingHorn\n```\n这些都是可以到达的。但是将代码改下：\n\n```\np.car = new Car(BLUE);\n```\n\n就变成了这样：\n\n```\n  Person [p]\n        |\n     Car (blue)       Car (red)\n                    /           \\\n                Engine    AnnoyingHorn\n```\n\n所以`Car(red)`就可以被回收了（p才是Root Set）\n\n也就是说，从Root Set出发，直接或间接所能到达的地方都可以成为reachable（或者成为存活的对象lived）.这里[StackOverFlow](http://stackoverflow.com/questions/6366211/what-are-the-roots)和[YourKit](https://www.yourkit.com/docs/java/help/gc_roots.jsp)说的很清楚\n\n**3.[Strong Refrence,Weak Refrence,Soft Refrence](https://en.wikipedia.org/wiki/Tracing_garbage_collection#Strong_and_weak_references)**\n\n - Strong Refrence：无论如何都不会被JVM回收，即时抛出`OutOfMeroryError`\n - Soft Reference：在GC时不会被回收，也就是说比Strong Refrence稍微弱一点。但内存耗尽的时候就会先收回SoftReference，软引用非常适合于创建缓存，可以用来存储图片缓存\n - Weak Reference：在GC时一定会被回收，也就是说比Soft Reference稍微弱一点，WeakHashMap来解决，集合的内存问题（集合只要有一个生命周期长的，所有的都不会回收）\n - 最后一个幽灵引用，我也不是很清楚。。（// Todo）\n看看Java代码实现（大家可以Run一下）：\n\n```\n/**\n * Created on 2016/3/17.\n *\n * @author 王启航\n * @version 1.0\n */\npublic class Reference {\n    public static void main(String args[]) {\n        WeakReferenceTest();\n        SoftReferenceTest();\n    }\n\n    //WeakReference在GC时一定会被回收\n//WeakHashMap来解决，集合的内存问题（集合只要有一个生命周期长的，所有的都不会回收）\n    static void WeakReferenceTest() {\n        String s = new String(\"WQH\");\n        WeakReference<String> wr = new WeakReference<>(s);\n        s = null;\n        while (wr.get() != null) {\n            System.out.println(\"WeakReference get :\" + wr.get());\n            System.gc();\n            System.out.println(\"System.gc() \" + wr.get());\n        }\n\n    }\n\n    //SoftReference在GC时不会被回收，但内存耗尽的时候就会先收回SoftReference\n	//软引用非常适合于创建缓存，可以用来存储图片缓存\n    static void SoftReferenceTest() {\n        String s = new String(\"WQH\"); //必须是new String()，String s = \"WQH\"是错误的\n        SoftReference<String> wr = new SoftReference<>(s);\n        s = null;\n        while (wr.get() != null) {\n            System.out.println(\"SoftReference get :\" + wr.get());\n            System.gc();\n            System.out.println(\"System.gc()\" + wr.get());\n        }\n    }\n}\n\n```\n\n\n\n二：原则与假设\n-------------\n\n**2.1Tri-Color**\n\n![Tri-Color](http://adamansky.bitbucket.org/slides/gc/img/gc-tricolor.png)\n\n - `White`：not-alive 或者 没有被collector访问过\n - `Black`：alive且自己被collector访问过，但是children not-alive 或者 没有被collector访问过\n - `Black`：自己和children都alive且被collector访问过\n\nTri-Color是一个原则（准确的说是abstraction），下面几个算法直接或间接的实现这个原则。这里是[维基百科的说明](https://en.wikipedia.org/wiki/Tracing_garbage_collection#Tri-color_marking)\n\n\n**2.2 infant mortality or the generational hypothesis：**弱代假设，即大多数对象都在年轻时候死亡\n\n三：Reference Counting\n--------------------\n\n> As a collection algorithm, reference counting tracks, for each object,\n> a count of the number of references to it held by other objects. If an\n> object\'s reference count reaches zero, the object has become\n> inaccessible, and can be destroyed.\n> \n> When an object is destroyed, any objects referenced by that object\n> also have their reference counts decreased. Because of this, removing\n> a single reference can potentially lead to a large number of objects\n> being freed. A common modification allows reference counting to be\n> made incremental: instead of destroying an object as soon as its\n> reference count becomes zero, it is added to a list of unreferenced\n> objects, and periodically (or as needed) one or more items from this\n> list are destroyed.\n> \n> Simple reference counts require frequent updates. Whenever a reference\n> is destroyed or overwritten, the reference count of the object it\n> references is decremented, and whenever one is created or copied, the\n> reference count of the object it references is incremented.\n> \n> Reference counting is also used in disk operating systems and\n> distributed systems, where full non-incremental tracing garbage\n> collection is too time consuming because of the size of the object\n> graph and slow access speed.\n\n\n建议先看英文原文，在看我自己的理解。\nReference counting（引用计数法）：对每个Object，都记录这个Object被其他Object所引用的次数，可以称为counter。当这个Object的被其他Object所引用的次数为0的时候，这个Object是可以被GC所摧毁的。当这个Object被摧毁时，所有被这个Object引用的这个Object对象的counter都会减一。\n这样一旦某个对象的counter == 0，这个对象就会被立即回收，有个解决办法：把counter == 0的对象放在一个list里面，用来存储未被其他Object所引用的对象。\n引用计数法可以被用在磁盘操作系统（disk operating systems），和分布式系统（distributed systems），而且在PHP的ZEND引擎用的也是引用计数法（好像把。。。）\n![RC1](http://img.blog.csdn.net/20160315125216097)\nB被A引用，D被C引用。所以 counterB == 1  ,counterD == 1。\n\n![RC2](http://img.blog.csdn.net/20160315125405021)\n现将A引用指针指向D，counterD == 2，counterB == 0 。所以B可以被GC回收。\n![RC3](http://img.blog.csdn.net/20160315125600071)\n现在将A回收（因为counterA == 0，所以可以被回收）则counterD == 1。（取消了A对D的引用）\n引用计数法就是这么一回事\n四：Stop-and-Copy\n---------------\nStop-and-Copy首先停止当前正在进行的程序（stop-the-world），进行垃圾收集。在垃圾收集的过程中，把可用的内存copy到另一块内存中，这样就可以把没用的内存回收。\n\n主要代表是[Cheney\'s algorithm](https://en.wikipedia.org/wiki/Cheney%27s_algorithm)，是semi-space collector的典型代表。\n\n> In this moving GC scheme, memory is partitioned into a \"from space\"\n> and \"to space\". Initially, objects are allocated into \"to space\" until\n> they become full and a collection is triggered. At the start of a\n> collection, the \"to space\" becomes the \"from space\", and vice versa.\n> The objects reachable from the root set are copied from the \"from\n> space\" to the \"to space\". These objects are scanned in turn, and all\n> objects that they point to are copied into \"to space\", until all\n> reachable objects have been copied into \"to space\". Once the program\n> continues execution, new objects are once again allocated in the \"to\n> space\" until it is once again full and the process is repeated. This\n> approach has the advantage of conceptual simplicity (the three object\n> color sets are implicitly constructed during the copying process), but\n> the disadvantage that a (possibly) very large contiguous region of\n> free memory is necessarily required on every collection cycle. This\n> technique is also known as stop-and-copy.\n\n[维基百科](https://en.wikipedia.org/wiki/Tracing_garbage_collection#Reachability_of_an_object)如上说。堆内存被分为2个区（to-space,from-space），新生成的对象被分配在一个区里面（to-space），当这个区里面的内存满了就会触发GC，具体步骤如下：\n\n - 交换2个区\n - 从root set开始遍历（采用DFS算法遍历），可以到达(reachable)的Object将从一个区被拷贝到另一个区\n\n还是比较简单的，有以下几篇文章参考：\n\n - [半区复制算法](http://www.jianshu.com/p/74659de07264)，写的不错，有图。\n - [从垃圾回收算法到Object Pool](http://qingbob.com/from-GC-algorithm-to-ObjectPool/)\n\n五：Mark-and-Sweep\n----------------\n\n> Tracing collectors are so called because they trace through the\n> working set of memory. These garbage collectors perform collection in\n> cycles. A cycle is started when the collector decides (or is notified)\n> that it needs to reclaim memory, which happens most often when the\n> system is low on memory[citation needed]. The original method involves\n> a naïve mark-and-sweep in which the entire memory set is touched\n> several times.\n> \n> \n> In the naive mark-and-sweep method, each object in memory has a flag\n> (typically a single bit) reserved for garbage collection use only.\n> This flag is always cleared, except during the collection cycle. The\n> first stage of collection does a tree traversal of the entire \'root\n> set\', marking each object that is pointed to as being \'in-use\'. All\n> objects that those objects point to, and so on, are marked as well, so\n> that every object that is ultimately pointed to from the root set is\n> marked. Finally, all memory is scanned from start to finish, examining\n> all free or used blocks; those with the in-use flag still cleared are\n> not reachable by any program or data, and their memory is freed. (For\n> objects which are marked in-use, the in-use flag is cleared again,\n> preparing for the next cycle.)\n> \n> This method has several disadvantages, the most notable being that the\n> entire system must be suspended during collection; no mutation of the\n> working set can be allowed. This will cause programs to \'freeze\'\n> periodically (and generally unpredictably), making real-time and\n> time-critical applications impossible. In addition, the entire working\n> memory must be examined, much of it twice, potentially causing\n> problems in paged memory systems.\n\n这个算法之所以叫Mark-and-Sweep，是因为collector遍历了内存集合。这种算法大部分是在内存低的时候调用。\n算法的运行过程如下：\n\n - 首先，内存中每个object都有一个标志位（flag），并且初始状态是清除所有标记的。\n - Mark阶段：从rootset开始遍历，对每个可以到达（reachable）的object，都去标记这个object。\n - 所有rootset直接或间接指向的对象，都被标记了。\n - Sweep阶段：GC对堆内存线性遍历，销毁所有没有被标记的对象（清除所有标记过的对象的标记，以便下次GC）\n\n就是这么简单粗暴，下面看看图（来自[这里](http://www.jianshu.com/p/b0f5d21fe031)）\n![这里写图片描述](http://www.processon.com/chart_image/530043e10cf2a3dc99dd9439.png)\n六：Mark-and-Compact\n------------------\n\nMark-and-sweep算法有如下的缺点：会产生许多的内存的碎片。为了解决这个问题，又有了一个新的算法，Mark阶段和前面的算法是一样的，但是在清除无用内存的之后，把live的内存都移到一起，这样就会有吧内存集中，减少了内存的碎片的产生。\n\n**6.1 Table-based compaction**\n\n> A table-based algorithm was first described by Haddon and Waite in\n> 1967.[1] It preserves the relative placement of the live objects in the heap, and requires only a constant amount of overhead.\n> \n> Compaction proceeds from the bottom of the heap (low addresses) to the\n> top (high addresses). As live (that is, marked) objects are\n> encountered, they are moved to the first available low address, and a\n> record is appended to a break table of relocation information. For\n> each live object, a record in the break table consists of the object\'s\n> original address before the compaction and the difference between the\n> original address and the new address after compaction. The break table\n> is stored in the heap that is being compacted, but in an area that are\n> marked as unused. To ensure that compaction will always succeed, the\n> minimum object size in the heap must be larger than or the same size\n> as a break table record.\n> \n> As compaction progresses, relocated objects are copied towards the\n> bottom of the heap. Eventually an object will need to be copied to the\n> space occupied by the break table, which now must be relocated\n> elsewhere. These movements of the break table, (called rolling the\n> table by the authors) cause the relocation records to become\n> disordered, requiring the break table to be sorted after the\n> compaction is complete. The cost of sorting the break table is O(n log\n> n), where n is the number of live objects that were found in the mark\n> stage of the algorithm.\n> \n> Finally, the break table relocation records are used to adjust pointer\n> fields inside the relocated objects. The live objects are examined for\n> pointers, which can be looked up in the sorted break table of size n\n> in O(log n) time if the break table is sorted, for a total running\n> time of O(n log n). Pointers are then adjusted by the amount specified\n> in the relocation table.\n\n对于table-based算法的实现如下：\n\n - 首先有一个指针，从heap的bottom(low addresses)遍历到top(high addresses)\n - 在遍历的过程中，当遇到一个live的对象，把这个对象移到heap的bottom端，并且在这个heap中创建一个**break table**，这个table存储2个内容，这个对象的长度，这个对象原来的位置和移动过后的位置的**差值**。\n - 最终所有的对象，都被移动到bottom端。由于移动之后，原来所有stack与heap的指向都是错误的，所以现在要将所有的指向给重行定位，得到最终结果，对于重新定位，当然要遍历整个break-table了。（注意，因为break table存储在heap中，所以 the minimum object size in the heap must be larger than or the same size as a break table record）\n![这里写图片描述](https://upload.wikimedia.org/wikipedia/en/0/03/Table-based_heap_compaction_flattened.svg)\n\n最后有一点注意：算法的时间复杂度为O(n log n)，where n is the number of live objects that were found in the mark stage of the algorithm.怎么算的呢？遍历break-table，所以得到了算法基础的排序：n个对象排序的时间复杂度咯？这里有[连接](https://en.wikipedia.org/wiki/Comparison_sort)\n\n**6.2 LISP2 Algorithm**\n\n> In order to avoid O(n log n) complexity, the LISP2 algorithm uses 3\n> different passes over the heap. In addition, heap objects must have a\n> separate forwarding pointer slot that is not used outside of garbage\n> collection.\n> \n> After standard marking, the algorithm proceeds in the following 3\n> passes:\n> \n> 1. Compute the forwarding location for live objects. \n> Keep track of a free and live pointer and initialize both to the start of heap. If the live\n> pointer points to a live object, update that object\'s forwarding\n> pointer to the current free pointer and increment the free pointer\n> according to the object\'s size. Move the live pointer to the next\n> object End when the live pointer reaches the end of heap. \n> 2.  Update all\n> pointers For each live object, update its pointers according to the\n> forwarding pointers of the objects they point to. \n> 3.  Move objects For\n> each live object, move its data to its forwarding location. This\n> algorithm is O(n) on the size of the heap; it has a better complexity\n> than the table-based approach, but the table-based approach\'s n is the\n> size of the used space only, not the entire heap space as in the LISP2\n> algorithm. However, the LISP2 algorithm is simpler to implement.\n\nLISP2算法利用三次遍历Heap来进行GC管理，三个步骤分别如下：\n\n - 首先初始化2个指针（free,live）并使这2个指针指向heap的首部（start）。然后让live指针开始遍历heap，当live指针遇到一个lived对象的时候，让这个对象的一个指针指向free指针的位置（在构造这个对象的时候就把这个指针分配好的）然后free指针向堆尾移动这个Object长度的距离。\n - 更新stack和heap所有Object的指向问题（和Mark-and-Sweep一样一样的）\n - 将所有的Object都移到heap的首部（紧紧的依靠在一起。。）\n\nLISP2的算法时间复杂度为O(n)，并且比较简单，用处较多。\n第一次遍历如下：\n![LISP2](http://img.blog.csdn.net/20160401202036076)\n第二次遍历就是更新所有的指向，这个比较简单，可以想象的出来；\n第三次就更简单了，把所有的对象全部左移。\n\n七：比较与总结\n-------\n更多资源[More](https://en.wikipedia.org/wiki/Tracing_garbage_collection#Implementation_strategies)\n\n**Moving vs. non-moving**\n对于non-moving：实现简单啊，只需要把没用的对象直接擦出就好了。\n对于Moving：看起来麻烦，既要计算移动后的位置，又要更新指针的指向，有大的内存对象的时候又很耗时。但是其实也有很多的优点：\n减少了内存的碎片化，对于整理后的内存，分配一块新的内存的时候，可以直接在空闲的地方的分配啦，就很快咯；下一次GC的时候，回收的时候将会很方便；同时如果2个对象位置远了，遍历的时候将会很慢。\n\n\n**Stop-the-world vs. concurrent**\nStopd-the-world：就是指GC的时候，当前程序会停止运行，\nconcurrent：就是并发进行处理，但是在GC时，运行的程序中声明一个对象要通知GC。\n','2016-03-23 01:17:36',1,0),(15,'LayoutInflater简单理解','原创','','在LayoutInflater的学习中有以下体会：\r\n\r\n先看几个例子\r\n```\r\n@Override\r\n    protected void onCreate(Bundle savedInstanceState) {\r\n        super.onCreate(savedInstanceState);\r\n        setContentView(R.layout.activity_main);\r\n        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.main_layout);\r\n\r\n        // Example1 出错\r\n        //View trueLayout = LayoutInflater.from(this).inflate(R.layout.button_layout, mainLayout, true);\r\n        //mainLayout.addView(trueLayout);\r\n\r\n        // Example2 Button的Params无效\r\n        //View trueLayout = LayoutInflater.from(this).inflate(R.layout.button_layout,null);\r\n        //mainLayout.addView(trueLayout);\r\n\r\n        // Example3\r\n        LayoutInflater.from(this).inflate(R.layout.button_layout, mainLayout, true);\r\n\r\n        // Example4\r\n        View falseLayout = LayoutInflater.from(this).inflate(R.layout.button_layout, mainLayout, false);\r\n        mainLayout.addView(falseLayout);\r\n    }\r\n```\r\n布局文件如下：\r\nactivity_main.xml\r\n```\r\n    <LinearLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\r\n        android:id=\"@+id/main_layout\"\r\n        android:layout_width=\"match_parent\"\r\n        android:layout_height=\"match_parent\"\r\n        android:orientation=\"horizontal\">\r\n    </LinearLayout>\r\n```\r\nbutton_layout.xml\r\n```\r\n    <Button xmlns:android=\"http://schemas.android.com/apk/res/android\"\r\n        android:layout_width=\"100dp\"\r\n        android:layout_height=\"wrap_content\"\r\n        android:text=\"Button\">\r\n    </Button>\r\n```\r\n\r\n\r\nExample1:\r\n会抛出异常\r\nExample2:\r\nButton的宽度没有100dp只是WRAP_CONTNET\r\nExample3:\r\n正常\r\nExample4:正常\r\n\r\n原因：\r\n首先进入`LayoutInflater#inflate()`源码看看：\r\n```\r\n    public View inflate(@LayoutRes int resource, @Nullable ViewGroup root, boolean attachToRoot) {\r\n        final Resources res = getContext().getResources();\r\n        if (DEBUG) {\r\n            Log.d(TAG, \"INFLATING from resource: \\\"\" + res.getResourceName(resource) + \"\\\" (\"\r\n                    + Integer.toHexString(resource) + \")\");\r\n        }\r\n\r\n        final XmlResourceParser parser = res.getLayout(resource);\r\n        try {\r\n            return inflate(parser, root, attachToRoot);\r\n        } finally {\r\n            parser.close();\r\n        }\r\n    }\r\n```\r\n然后进入`inflate(parser, root, attachToRoot);`\r\n```\r\n    public View inflate(XmlPullParser parser, @Nullable ViewGroup root, boolean attachToRoot) {\r\n        synchronized (mConstructorArgs) {\r\n            Trace.traceBegin(Trace.TRACE_TAG_VIEW, \"inflate\");\r\n\r\n            final Context inflaterContext = mContext;\r\n            final AttributeSet attrs = Xml.asAttributeSet(parser);\r\n            Context lastContext = (Context) mConstructorArgs[0];\r\n            mConstructorArgs[0] = inflaterContext;\r\n            View result = root;\r\n\r\n            try {\r\n                // Look for the root node.\r\n                int type;\r\n                while ((type = parser.next()) != XmlPullParser.START_TAG &&\r\n                        type != XmlPullParser.END_DOCUMENT) {\r\n                    // Empty\r\n                }\r\n\r\n				//找到xml文档的Start位置\r\n                if (type != XmlPullParser.START_TAG) {\r\n                    throw new InflateException(parser.getPositionDescription()\r\n                            + \": No start tag found!\");\r\n                }\r\n\r\n                final String name = parser.getName();\r\n                \r\n                if (DEBUG) {\r\n                    System.out.println(\"**************************\");\r\n                    System.out.println(\"Creating root view: \"\r\n                            + name);\r\n                    System.out.println(\"**************************\");\r\n                }\r\n\r\n                if (TAG_MERGE.equals(name)) {\r\n                    if (root == null || !attachToRoot) {\r\n                        throw new InflateException(\"<merge /> can be used only with a valid \"\r\n                                + \"ViewGroup root and attachToRoot=true\");\r\n                    }\r\n\r\n                    rInflate(parser, root, inflaterContext, attrs, false);\r\n                } else {\r\n	                //重点1：Temp节点是要解析的xml文档的root节点\r\n                    // Temp is the root view that was found in the xml\r\n                    final View temp = createViewFromTag(root, name, inflaterContext, attrs);\r\n					//重点2：parms到底是temp节点还是root的LayoutParams\r\n                    ViewGroup.LayoutParams params = null;\r\n\r\n                    if (root != null) {\r\n                        if (DEBUG) {\r\n                            System.out.println(\"Creating params from root: \" +\r\n                                    root);\r\n                        }\r\n                        // Create layout params that match root, if supplied\r\n                        // 创建params\r\n                        params = root.generateLayoutParams(attrs);\r\n                        if (!attachToRoot) {\r\n                            // Set the layout params for temp if we are not\r\n                            // attaching. (If we are, we use addView, below)\r\n                            // 重点3：当attachToRoot == false 时,调用下面函数\r\n                            temp.setLayoutParams(params);\r\n                        }\r\n                    }\r\n\r\n                    if (DEBUG) {\r\n                        System.out.println(\"-----> start inflating children\");\r\n                    }\r\n\r\n                    // Inflate all children under temp against its context.\r\n                    rInflateChildren(parser, temp, attrs, true);\r\n\r\n                    if (DEBUG) {\r\n                        System.out.println(\"-----> done inflating children\");\r\n                    }\r\n\r\n                    // We are supposed to attach all the views we found (int temp)\r\n                    // to root. Do that now.\r\n                    if (root != null && attachToRoot) {\r\n                        // 重点4：当attachToRoot == true时,调用下面函数\r\n                        root.addView(temp, params);\r\n                    }\r\n\r\n                    // Decide whether to return the root that was passed in or the\r\n                    // top view found in xml.\r\n                    if (root == null || !attachToRoot) {\r\n                        result = temp;\r\n                    }\r\n                }\r\n\r\n            } catch (XmlPullParserException e) {\r\n                InflateException ex = new InflateException(e.getMessage());\r\n                ex.initCause(e);\r\n                throw ex;\r\n            } catch (Exception e) {\r\n                InflateException ex = new InflateException(\r\n                        parser.getPositionDescription()\r\n                                + \": \" + e.getMessage());\r\n                ex.initCause(e);\r\n                throw ex;\r\n            } finally {\r\n                // Don\'t retain static reference on context.\r\n                mConstructorArgs[0] = lastContext;\r\n                mConstructorArgs[1] = null;\r\n            }\r\n\r\n            Trace.traceEnd(Trace.TRACE_TAG_VIEW);\r\n\r\n            return result;\r\n        }\r\n    }\r\n```\r\n\r\n - 解析xml文档采用的[PULL](http://www.cnblogs.com/JerryWang1991/archive/2012/02/24/2365507.html)方式\r\n - 区分`root`与`temp`：`root`为函数参数`ViewGroup`,`temp`为xml文档的根节点\r\n - 当`attachToRoot == true`会调用`root.addView(temp, params);`，所以在一开始的Example1就会出错（不能重复添加啊），Example3就对了。当`attachToRoot == fasle`调用`temp.setLayoutParams(params);`所以在Example4要调用`mainLayout.addView(falseLayout);`不然就看不到`Button`了。\r\n - 对于`params`变量（我一开始想了好久），`params = root.generateLayoutParams(attrs);`这句代码\r\n```\r\n    /**\r\n     * Returns a new set of layout parameters based on the supplied attributes set.\r\n     *\r\n     * @param attrs the attributes to build the layout parameters from\r\n     *\r\n     * @return an instance of {@link android.view.ViewGroup.LayoutParams} or one\r\n     *         of its descendants\r\n     */\r\n    public LayoutParams generateLayoutParams(AttributeSet attrs) {\r\n        return new LayoutParams(getContext(), attrs);\r\n    }\r\n```\r\n 返回一个`LayoutParams`基于给的`AttributeSet`参数，那么`AttributeSet`从哪里来？\r\n \r\n\r\n```\r\nfinal AttributeSet attrs = Xml.asAttributeSet(parser);\r\n```\r\n`parser`为xml文档的`parser`，所以`params`变量为temp的`LayoutParams`（xml文档里面设置啦）。\r\n - 当`attachToRoot == true`，`inflate()`函数返回`root`；当`attachToRoot == true`，`inflate()`函数返回`temp`。\r\n\r\n\r\n最后说一点：\r\n\r\n 1. `layout_height`与`layout_width`不是`View`的真实高度，它是根据`Parent View`与当前`View`决定的。\r\n 2. Example2，里面由于`root`为空，默认`attachToRoot`为`true`，`params = root.generateLayoutParams(attrs);`就不会调用，`params`就为空。还有一点就是：`root.addView(temp, params);`不会调用。\r\n','2016-05-27 00:00:00',1,0);
/*!40000 ALTER TABLE `blog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(200) NOT NULL COMMENT '评论内容',
  `createdBy` int(11) NOT NULL COMMENT '创建者',
  `createdAt` datetime NOT NULL COMMENT '创建日期',
  `belongTo` int(11) NOT NULL COMMENT '所评论文章的ID',
  `replyTo` int(11) DEFAULT NULL COMMENT '被回复的评论的ID',
  PRIMARY KEY (`id`),
  KEY `BelongTo_idx` (`belongTo`),
  KEY `CreatedBy_idx` (`createdBy`),
  KEY `replyTo_idx` (`replyTo`),
  CONSTRAINT `BelongTo` FOREIGN KEY (`belongTo`) REFERENCES `blog` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `CreatedBy` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `replyTo` FOREIGN KEY (`replyTo`) REFERENCES `comment` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (1,'中文是不是真的可以了 ？？？',1,'2016-01-12 03:27:30',1,NULL),(2,'这篇文章写的好好好。',1,'2016-05-04 07:15:21',1,NULL),(3,'I Love You!!!!',2,'2016-05-04 07:15:21',1,NULL),(4,'Hello World',2,'2016-05-12 20:00:30',1,NULL),(5,'测试                                                      测试                               嚄嚄，有这么长吗？  ',1,'2016-07-21 20:11:14',1,NULL),(6,'断桥是否下过雪，我望着湖面。水中残月如雪，指尖轻点如雪。断桥是否下过雪，我望着湖面。若是无缘再见，白堤流连垂泪好几遍。',3,'2016-07-21 20:19:46',1,NULL),(7,'王启航',1,'2016-07-21 20:22:51',1,NULL),(8,'电子科技大学',1,'2016-07-21 20:24:54',1,NULL),(9,'我是回复，你相信吗？',2,'2016-07-23 14:52:54',1,1),(10,'Helvr',1,'2016-07-24 14:36:16',1,NULL),(11,'æå«çå¯èª',8,'2016-08-11 17:01:01',1,NULL),(12,'æå«çå¯èª',8,'2016-08-11 17:02:26',1,NULL),(13,'æå«çå¯èª',8,'2016-08-11 17:04:08',1,NULL),(14,'乱码咯',8,'2016-08-11 17:08:18',1,NULL),(15,'????????????��??',8,'2016-08-11 17:13:13',1,NULL),(16,'????????????è??',8,'2016-08-11 17:13:53',1,NULL),(17,'Comment From PostMan I Love You.!!!!',1,'2016-09-17 14:18:04',1,NULL),(18,'Comment From PostMan I Love You.!!!!',1,'2016-09-17 14:29:48',1,NULL),(20,'brtfnrtnrtnrthbrthnjrtnjr',10,'2016-09-17 14:39:13',15,NULL),(21,'IHJVOIce fespjgbr ',10,'2016-09-17 14:45:22',1,NULL),(22,'? vver  ',11,'2016-09-19 16:35:21',1,NULL),(23,'vewrvgwerberb',11,'2016-09-19 16:42:48',1,NULL),(24,'vwevbwebwberberbner',11,'2016-09-19 19:49:17',1,NULL),(25,'Hello React!',11,'2016-09-19 19:49:41',1,NULL),(27,'I am Reply from React. //@null:Comment From PostMan I Love You.!!!!',11,'2016-09-21 16:43:35',1,17),(28,'Token',11,'2016-09-21 17:08:46',1,NULL),(29,'',11,'2016-09-21 17:11:13',1,NULL),(36,'ferwgb',11,'2016-09-22 17:53:35',15,NULL),(38,'654',11,'2016-09-22 17:56:33',15,NULL),(39,'gh677534345 h654',11,'2016-09-22 17:58:05',15,NULL),(48,'436457',11,'2016-09-22 18:19:25',15,NULL),(53,'tr346345',11,'2016-09-22 19:16:21',15,NULL),(54,'smgui //@fff:brtfnrtnrtnrthbrthnjrtnjr',11,'2016-09-22 19:20:32',15,20);
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tag`
--

DROP TABLE IF EXISTS `tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(40) NOT NULL,
  `description` varchar(40) NOT NULL COMMENT '当前Tag的描述',
  `url` varchar(40) NOT NULL COMMENT '图片的URL',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tag`
--

LOCK TABLES `tag` WRITE;
/*!40000 ALTER TABLE `tag` DISABLE KEYS */;
INSERT INTO `tag` VALUES (1,'Java','hhhhhhhhh','hhhhhhhhhhhhhhhhh'),(2,'Android','hhhhhhhhhh','hhhhhhhhhhhhhhh');
/*!40000 ALTER TABLE `tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(40) NOT NULL,
  `email` varchar(40) DEFAULT NULL,
  `password` varchar(40) NOT NULL,
  `avatarUri` varchar(200) DEFAULT NULL COMMENT '用户头像的URL',
  `token` varchar(100) DEFAULT NULL,
  `lastModified` datetime NOT NULL COMMENT '上次登录时间',
  `coverUri` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'王启航','1906362072@qq.com','1','1\\Avatar.jpg','536D24D79E9BABBAB77D4B4263F9E161','2016-05-12 22:44:24',''),(2,'wqh','1906362072@qq.com','1','','81EBEC89930EB76407AE2F6D69A7D1C0','2016-09-22 19:47:23',NULL),(3,'ZH','hellowangqihang@gamil.com','1','',NULL,'2016-05-19 22:04:42',NULL),(8,'1234567','1234567','1234567','','489DBEA93943CC81D6D190F1A4F3B842','2016-08-11 16:53:19',''),(10,'fff',NULL,'123456','','D9A434DD7E0887D96C22CFB6B77DAB21','2016-09-17 14:45:14',''),(11,'qwe',NULL,'123456','http://ww4.sinaimg.cn/large/610dc034jw1f820oxtdzzj20hs0hsdhl.jpg','D835ADF4AB07B099D779F1F783837D70','2016-09-22 17:18:52',''),(12,'1',NULL,'1','','2796AF129C44027D99A28A6E7F8DB58B','2016-09-27 15:03:02','');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `work`
--

DROP TABLE IF EXISTS `work`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `work` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(40) NOT NULL,
  `description` varchar(100) NOT NULL,
  `fileName` varchar(40) NOT NULL COMMENT '文件名例如：Chatting.apk',
  `logoUrl` varchar(100) NOT NULL COMMENT 'logo的URL地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `work`
--

LOCK TABLES `work` WRITE;
/*!40000 ALTER TABLE `work` DISABLE KEYS */;
INSERT INTO `work` VALUES (1,'饭点','学生与食堂交流的平台咯。。。','Eatting.apk','http://www.wangqihang.cn/Blog/work/download?fileName=Eatting.apk'),(2,'指尖','广袤世界，在你指尖。指尖为一款即时聊天APP.','Chatting.apk','http://www.wangqihang.cn/Blog/work/download?fileName=Chatting.apk'),(3,'Blog','个人博客系统啦.','Blog.apk','http://www.wangqihang.cn/Blog/work/download?fileName=Blog.apk');
/*!40000 ALTER TABLE `work` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-10-10 21:21:24
