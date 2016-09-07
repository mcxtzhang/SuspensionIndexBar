# ItemDecorationIndexBar
本项目是两篇博文的合体代码：

一【Android 仿微信通讯录 导航分组列表-上】使用ItemDecoration为RecyclerView打造带悬停头部的分组列表
链接：http://blog.csdn.net/zxt0601/article/details/52355199


网上关于实现带悬停分组头部的列表的方法有很多，像我看过有主席的自定义ExpandListView实现的，也看过有人用一个额外的父布局里面套 RecyclerView/ListView+一个头部View（位置固定在父布局上方）实现的。
对于以上解决方案，有以下几点个人觉得不好的地方：

1. 现在RecyclerView是主流

2. 在RecyclerView外套一个父布局总归是**增加布局层级，容易overdraw**，显得不够优雅。

3. item布局实现带这种分类头部的方法有两种，一种是把分类头部当做一种itemViewtype（麻烦），另一种是每个Item布局都包含了分类头部的布局，代码里根据postion等信息动态Visible，Gone头部（布局冗余，item效率降低）。

况且Google为我们提供了**ItemDecoration**，它本身就是用来修饰RecyclerView里的Item的，它的```getItemOffsets()  onDraw()```方法用于为Item分类头部留出空间和绘制（解决缺点3），它的```onDrawOver()```方法用于绘制悬停的头部View（解决缺点2）。

而且更重要的是，**ItemDecoration出来这么久了，你还不用它**？

本文就利用ItemDecoration 打造 分组列表，并配有悬停头部功能。


亮点预览：**添加多个ItemDecoration、它们的执行顺序、ItemDecoration方法执行顺序、ItemDecoration和RecyclerView的绘制顺序**


二【Android 仿微信通讯录 导航分组列表-下】自定义View为RecyclerView打造右侧索引导航栏IndexBar
链接： http://blog.csdn.net/zxt0601/article/details/52420706
在上篇文章里
，我们用ItemDecoration为RecyclerView打造了带悬停头部的分组列表。其实Android版微信的通讯录界面，它的分组title也不是悬停的，我们已经领先了微信一小步（认真脸）~
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
 
 最终版预览：
![这里写图片描述](http://img.blog.csdn.net/20160905233755209)
