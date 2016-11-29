# SuspensionIndexBar
相关博文：

从0实现：
http://blog.csdn.net/zxt0601/article/details/52355199

http://blog.csdn.net/zxt0601/article/details/52420706

使用说明 and 封装：
http://gold.xitu.io/post/583c133eac502e006c23cc81

喜欢随手点个star 多谢 

##  在哪里找到我：
我的github：

https://github.com/mcxtzhang

我的CSDN博客：

http://blog.csdn.net/zxt0601

我的稀土掘金：

http://gold.xitu.io/user/56de210b816dfa0052e66495

我的简书：

http://www.jianshu.com/users/8e91ff99b072/timeline

# 效果一览：
配合我另一个库组装的效果(SuspensionIndexBar + SwipeMenuLayout)：

(SwipeDelMenuLayout : https://github.com/mcxtzhang/SwipeDelMenuLayout)

SwipeDelMenuActivity:
![image](https://github.com/mcxtzhang/ItemDecorationIndexBar/blob/master/gif/ItemDecorationIndexBar_SwipeDel.gif)

高仿美团选择城市界面（MeituanSelectCityActivity）:

![image](https://github.com/mcxtzhang/ItemDecorationIndexBar/blob/master/gif/meituan.gif)

高仿微信通讯录界面（WeChatActivity）:

![image](https://github.com/mcxtzhang/ItemDecorationIndexBar/blob/master/gif/weixin.gif)

普通城市列表界面(MainActivity):

![image](https://github.com/mcxtzhang/ItemDecorationIndexBar/blob/master/gif/citylist)

# 引入
Step 1. Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Step 2. Add the dependency
```
	dependencies {
	        compile 'com.github.mcxtzhang:SuspensionIndexBar:V1.0.0'
	}
```

# 使用说明：
http://gold.xitu.io/post/583c133eac502e006c23cc81


# 更新记录：
2016 11 29 :
* 重构悬停分组，将TitleItemDecoration更名为SuspensionDecoration，数据源依赖ISuspensionInterface接口。
* 重构索引导航，将IndexBar对数据源的操作，如排序，转拼音等分离出去，以接口IIndexBarDataHelper通信。
* 有N多兄弟给我留言、加QQ问的：如何实现美团选择城市列表页面，
* 添加一个不带悬停分组的HeaderView（微信通讯录界面）

2016 11 10 :
1 IndexBar也考虑了HeaderView不需要索引的情况：
```
        mIndexBar.setmPressedShowTextView(mTvSideBarHint)//设置HintTextView
                .setNeedRealIndex(true)//设置需要真实的索引
                .setmLayoutManager(mManager)//设置RecyclerView的LayoutManager
                .setmSourceDatas(mDatas)//设置数据
                .setHeaderViewCount(mWrapperAdapter.getHeaderViewCount());//设置HeaderView数量
```

2016 11 07 ：
1 考虑了HeaderView不需要索引的情况，TitleItemDecoration增加了一个headerView的字段和方法：
```
new TitleItemDecoration(this, mDatas).setHeaderViewCount(mWrapperAdapter.getHeaderViewCount())
```
2 暂时抽离TitleItemDecoration至lib包，并且以 **接口形式接收数据** 。


# to do list
1 扩展 SuspensionDecoration 支持传入layout or View。


# 闲言碎语

### 一
网上关于实现带悬停分组头部的列表的方法有很多，像我看过有主席的自定义ExpandListView实现的，也看过有人用一个额外的父布局里面套 RecyclerView/ListView+一个头部View（位置固定在父布局上方）实现的。
对于以上解决方案，有以下几点个人觉得不好的地方：

1. 现在RecyclerView是主流

2. 在RecyclerView外套一个父布局总归是**增加布局层级，容易overdraw**，显得不够优雅。

3. item布局实现带这种分类头部的方法有两种，一种是把分类头部当做一种itemViewtype（麻烦），另一种是每个Item布局都包含了分类头部的布局，代码里根据postion等信息动态Visible，Gone头部（布局冗余，item效率降低）。

况且Google为我们提供了**ItemDecoration**，它本身就是用来修饰RecyclerView里的Item的，它的```getItemOffsets()  onDraw()```方法用于为Item分类头部留出空间和绘制（解决缺点3），它的```onDrawOver()```方法用于绘制悬停的头部View（解决缺点2）。

而且更重要的是，**ItemDecoration出来这么久了，你还不用它**？

本文就利用ItemDecoration 打造 分组列表，并配有悬停头部功能。


亮点预览：**添加多个ItemDecoration、它们的执行顺序、ItemDecoration方法执行顺序、ItemDecoration和RecyclerView的绘制顺序**

（http://blog.csdn.net/zxt0601/article/details/52355199）

### 二

我们用ItemDecoration为RecyclerView打造了带悬停头部的分组列表。其实Android版微信的通讯录界面，它的分组title也不是悬停的，我们已经领先了微信一小步（认真脸）~
再看看市面上常见的分组列表（例如饿了么点餐商品列表），不仅有悬停头部，悬停头部在切换时，还会伴有**切换动画**。
关于ItemDecoration还有一个问题，简单布局还好，我们可以draw出来，如果是复杂的头部呢？能否写个xml，inflate进来，这样使用起来才简单，即另一种**简单使用onDraw和onDrawOver**的姿势。
so，本文开头我们就先用两节完善一下我们的ItemDecoration。然后进入正题：自定义View实现右侧索引导航栏IndexBar，对数据源的排序字段按照拼音排序，最后将RecyclerView和IndexBar联动起来，触摸IndexBar上相应字母，RecyclerView滚动到相应位置。(在屏幕中间显示的其实就是一个TextView，我们set个体IndexBar即可)
由于大部分使用右侧索引导航栏的场景，都需要这几个固定步骤，对数据源排序，set给IndexBar，和RecyclerView联动等，所以最后再将其封装一把，成一个高度封装，因此扩展性不太高的控件，更方便使用，如果需要扩展的话，反正看完本文再其基础上修改应该很简单~。

本文摘要:
 1. 用ItemDecoration实现悬停头部**切换动画**
 2. 另一种**简单使用onDraw()和onDrawOver()**的姿势
 3. 自定义View实现右侧**索引导航栏**IndexBar
 4. 使用TinyPinyin对数据源排序
 5. 联动IndexBar和RecyclerView。
 6. 封装重复步骤，方便二次使用，并可**定制导航数据源**。

（http://blog.csdn.net/zxt0601/article/details/52420706）

### 三

本文是这个系列的第三篇，不出意外也是终结篇。因为使用经过重构后的控件已经可以快速实现市面上带 索引导航、悬停分组的列表界面了。
在前两篇里，我们从0开始，一步一步实现了仿微信通讯录、饿了么选餐界面。
（第一篇戳我 第二篇戳我）
这篇文章作为终结篇，和前文相比，主要涉及以下内容：

* 重构悬停分组，将TitleItemDecoration更名为SuspensionDecoration，数据源依赖ISuspensionInterface接口。
* 重构索引导航，将IndexBar对数据源的操作，如排序，转拼音等分离出去，以接口IIndexBarDataHelper通信。
* 有N多兄弟给我留言、加QQ问的：如何实现美团选择城市列表页面，
* 添加一个不带悬停分组的HeaderView（微信通讯录界面）

http://gold.xitu.io/post/583c133eac502e006c23cc81