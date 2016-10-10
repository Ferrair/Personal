# 简介
该项目是一个博客系统的后端，运用的框架是`JFinal`。向前段或者移动端提供了一些API。大家可以clone这个仓库，进行`JFinal`或者`JavaWEB`知识的学习。

# Get Started
- `git clone https://github.com/Ferrair/Personal.git`
- 配置数据库（该项目数据库配置文件在`/src/Jdbc.properties`）
- 假数据，将`source`中的`personal.sql`拷贝到`~`目录下
- 进入你的`mysql`命令界面`mysql -uroot -p`
- 创建数据库`create database personal;`
- 进入表中`use personal;`
- 导入数据`source ~/personal.sql`
- 可以将`./out/artifacts/Personal_war_wxploded/Personal_war_wxploded.war`拷贝到`Tomcat`下的`webapps`目录下运行即可

# 架构
 - `Interceptor`：拦截器，或者叫做`AOP`,其作用就是在所有层之前，拦截用户请求（一般来说就是用户的身份验证(`Authentication`)之类）
 - `Router`：路由层，将前段发起的请求进行路由，路由到相应的`Controller`.
 - `Controller`:控制器，业务逻辑的主要部分。其向上接受路由器的请求，向下分发请求（要求下面的层来请求数据库）
 - `Service`:操作数据库。
 - `DAO`:数据存储层，其直接操作数据库。（其实`DAO`与`Service`分的没有那么细致，都是关于数据存储的，我的理解是这样的）

# 一些讲解

1.请求过程
----
前段的一个请求是怎么来的？是怎么请求数据库的呢？怎么返回数据的呢？下面我们就一步一步的看看。

 (1)例如一个请求 `/api/blog/appendComment`,请求携带参数`belongTo`,`content`,`createdBy`.
 
 (2)该请求会被路由到这里
 
 ```
 //# BlogController
 @Before({PostIntercept.class, UserIntercept.class})
    @ActionKey("/api/blog/appendComment")
    public void appendComment() {
        Integer belongTo = getParaToInt("belongTo"); //MUST
        String content = getPara("content");         //MUST
        String createdBy = getPara("createdBy");     //MUST
        if (belongTo == null || content == null || createdBy == null) {
            mResult.fail(102);
            renderJson(mResult);
            return;
        }
        mResult.success(mCommentService.publish(createdBy, belongTo, content));
        renderJson(mResult);
    }
 ```
 在上面的`@ActionKey("/api/blog/appendComment")`就是进行路由部分。
 
 (3)拦截
 `@Before({PostIntercept.class, UserIntercept.class})`
 
 这部分代码，表示请求在处理之前，要被这2个拦截器拦截。
 
 ```
 //# PostIntercept
 // 过滤不是POST的请求
 public class PostIntercept implements Interceptor {
    private Result mResult = new Result();

    @Override
    public void intercept(Invocation inv) {

        Controller mController = inv.getController();
        if (!mController.getRequest().getMethod().equals("POST")) {
            mResult.fail(104);
            mController.renderJson(mResult);
        } else {
            inv.invoke();
        }
    }
}
 ```
 
 ```
 //# UserIntercept
 // 用户必须携带token
 
 public class UserIntercept implements Interceptor {
    private Result mResult = new Result();
    private Controller mController;

    @Override
    public void intercept(Invocation inv) {
        try {
            mController = inv.getController();
            UserService mUserService = ServiceAbs.getInstance(UserService.class, mController);

            // Header Param.
            Integer userIDHeader = Integer.valueOf(mController.getRequest().getHeader("userID"));
            String preToken = mController.getRequest().getHeader("token");

            Boolean userIDSession = mController.getSessionAttr(String.valueOf(userIDHeader));

            // Login by Token
            if (userIDHeader != null && preToken != null) {
                User aUser = mUserService.queryById(userIDHeader);
                if (aUser == null) {
                    mResult.fail(101);
                    mController.renderJson(mResult);
                    return;
                }

                if (aUser.get("token").equals(preToken)) {
                    inv.invoke();
                    return;
                } else {
                    mResult.fail(106);
                    mController.renderJson(mResult);
                    return;
                }
            }
            // Session is out of time.
            if (userIDSession == null || !userIDSession) {
                mResult.fail(105);
                mController.renderJson(mResult);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            mResult.fail(105);
            mController.renderJson(mResult);
        }
    }
}

 ```
 
 
 (4) 处理请求
 将请求的参数传给`Service`，进行数据库的读取
 
 ```
 //# CommentService
 public List<Comment> publish(String createdBy, int belongTo, String content) {
        Comment aComment = new Comment();
        aComment.set("createdBy", createdBy);
        aComment.set("content", content);
        aComment.set("belongTo", belongTo);
        aComment.set("createdAt", TimeUtil.getDateTime(System.currentTimeMillis()));
        if (aComment.save()) {
            Integer id = aComment.get("id");
            return Comment.dao.find("SELECT comment.*,user.username AS creatorName,user.avatarUri AS creatorAvatarUri FROM comment,blog,user WHERE comment.belongTo = blog.id AND comment.createdBy = user.id AND comment.belongTo = ? AND comment.id = ?", belongTo, id);
        } else return null;
    }
 ```
 
 (5) 上述请求中读取数据之后，就可以返回给前段了
 
 ```
 renderJson(mResult);
 ```
 
 
 2.返回数据的格式
 -----
 很重要的一部分就是返回的格式是怎么样子的。
 
 ```
 {
 	"Msg":"OK",
 	"Code":100,
 	"Result":
 		[
 			{
 				//data
 			}
 		]
 }
 ```
 就是上面的格式.但是要注意`Result`是一个`JSONArray`(不管数据有多少个，都是一个数组)
 为什么呢？
 
 ```
 //# Result
 // 在Result.java中将转化为List数组类型的
 public <DataType> void success(DataType aData) {
        put("Code", 100);
        put("Msg", code.get(100));
        put("Result", CollectionUtil.of(aData));
    }


    public <DataType> void success(List<DataType> aList) {
        put("Code", 100);
        put("Msg", code.get(100));
        put("Result", aList);
    }
    
 ```

```
//# CollectionUtil
@SafeVarargs
    public static <T> List<T> of(T... data) {
        return Stream.of(data).collect(Collectors.toList());
    }
    
```

3.`Service`处理
----

所有的`Service`都是通过`ServiceAbs.getInstance()`来实例化的。

```
//# ServiceAbs.java
/**
 * Created on 2016/3/12.
 * <p>
 * This class is the base-abstract class for all <code>Service<code/> in this package.
 * It can be used to generate a <code>ServiceImpl<code/> which do the work belong to <code>Controller<code/> before.
 * So decrease the number of the work of <code>Controller<code/>
 *
 * @author 王启航
 * @version 1.0
 */
public abstract class ServiceAbs {
    protected Controller mController;
    private static Map<Class<? extends ServiceAbs>, ServiceAbs> serviceMap = new HashMap<>();

    /**
     * @param clazz      The Service that want to be instanced
     * @param controller The Controller that can control the Service
     */
    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public static <Service extends ServiceAbs> Service getInstance(Class<Service> clazz, Controller controller) {
        Service mService = (Service) serviceMap.get(clazz);
        if (mService == null) {
            try {
                mService = clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            serviceMap.put(clazz, mService);
        }
        mService.mController = controller;
        return mService;

    }
}

```
利用了反射来进行实例化子类，这样做就不要每个子类写一个构造函数，在初始化的时候可以节约大量的代码，同时作为工厂模式，将使得代码具有更好的拓展性。

# Todo List
 - 没有采用RESTful 的结构，因为当时太年轻了，还不知道有这种东西。。。。。
 - 中文乱码，这个弄了好久，依然不行，编码格式也设置了还是不行
 - 多线程，不知道请求来了，是否要重新给其分配一个线程。不然脏读等问题怎么解决。
 
 
# 联系我
`QQ : 1906362072`

`Mail : hellowangqihang@gmail.com`


