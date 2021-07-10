[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Fragmentation-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/5937)
[![Build Status](https://travis-ci.org/YoKeyword/Fragmentation.svg?branch=master)](https://travis-ci.org/YoKeyword/Fragmentation)
[![Download](https://jitpack.io/v/Z-P-J/Fragmentation.svg)](https://jitpack.io/#Z-P-J/Fragmentation)
[![Hex.pm](https://img.shields.io/hexpm/l/plug.svg)](https://www.apache.org/licenses/LICENSE-2.0)

# ZFragmentation
A powerful library that manage Fragment for Android!
本项目fork于[Fragmentation框架](https://github.com/YoKeyword/Fragmentation)，综合了几个module的代码，修改了包名，优化滑动返回，解决某些情况下的打开fragment延迟问题。除此之外，还新增了postOnEnterAnimationEnd和postOnSupportVisible方法，让你提前加载数据，待fragment动画结束或可见时显示数据，解决加载数据卡顿问题。

### [English README.md](https://github.com/Z-P-J/Fragmentation/blob/master/README_EN.md)

为"单Activity ＋ 多Fragment","多模块Activity + 多Fragment"架构而生，简化开发，轻松解决动画、嵌套、事务相关等问题。

![](/gif/logo.png)


为了更好的使用和了解该库，推荐阅读下面的文章:

[Fragment全解析系列（一）：那些年踩过的坑](http://www.jianshu.com/p/d9143a92ad94)

[Fragment全解析系列（二）：正确的使用姿势](http://www.jianshu.com/p/fd71d65f0ec6)


## Demo演示：
均为单Activity + 多Fragment，第一个为简单流式demo，第二个为仿微信交互的demo(全页面支持滑动退出)，第三个为仿知乎交互的复杂嵌套demo

### [下载APK](https://www.pgyer.com/fragmentation)


<img src="/gif/demo1.gif" width="280px"/> <img src="/gif/demo2.gif" width="280px"/>
 <img src="/gif/demo3.gif" width="280px"/>

## 特性

1、**悬浮球／摇一摇实时查看Fragment的栈视图，降低开发难度**

2、**内部队列机制 解决Fragment多点触控、事务高频次提交异常等问题**

3、**增加启动模式、startForResult等类Activity方法**

4、**类Android事件分发机制的Fragment BACK键机制：onBackPressedSupport()**

5、**提供onSupportVisible()、懒加载onLazyInitView()等生命周期方法，简化嵌套Fragment的开发过程**

6、**提供 Fragment转场动画 系列解决方案，动态改变动画**

7、**提供Activity作用域的EventBus辅助类，Fragment通信更简单、独立**

8、**支持SwipeBack滑动边缘退出**

<img src="/gif/stack.png" width="150px"/> <img src="/gif/log.png" width="300px"/>     <img src="/gif/SwipeBack.png" width="150px"/>

## 如何使用

**1. 项目下app的build.gradle中依赖：**
````gradle
// appcompat-v7包是必须的
implementation 'com.github.Z-P-J:Fragmentation:1.0.0'
````

**2. Activity `extends` SupportActivity或者 `implements` ISupportActivity：(实现方式可参考[MySupportActivity](https://github.com/Z-P-J/Fragmentation/blob/master/demo/src/main/java/com/zpj/fragmentation/demo/demo_flow/base/MySupportActivity.java))**
````java
// v1.0.0开始，不强制继承SupportActivity，可使用接口＋委托形式来实现自己的SupportActivity
public class MainActivity extends SupportActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(...);
        // 建议在Application里初始化
        Fragmentation.builder()
             // 显示悬浮球 ; 其他Mode:SHAKE: 摇一摇唤出   NONE：隐藏
             .stackViewMode(Fragmentation.BUBBLE)
             .debug(BuildConfig.DEBUG)
             ... // 更多查看wiki或demo
             .install();

        if (findFragment(HomeFragment.class) == null) {
            loadRootFragment(R.id.fl_container, HomeFragment.newInstance());  // 加载根Fragment
        }
    }
````

**3. Fragment `extends` SupportFragment或者 `implements` ISupportFragment：(实现方式可参考[MySupportFragment](https://github.com/Z-P-J/Fragmentation/blob/master/demo/src/main/java/com/zpj/fragmentation/demo/demo_flow/base/MySupportFragment.java))：**
````java
// v1.0.0开始，不强制继承SupportFragment，可使用接口＋委托形式来实现自己的SupportFragment
public class HomeFragment extends SupportFragment {

    private void xxx() {
        // 启动新的Fragment, 另有start(fragment,SINGTASK)、startForResult、startWithPop等启动方法
        start(DetailFragment.newInstance(HomeBean));
        // ... 其他pop, find, 设置动画等等API, 请自行查看WIKI
    }
}
````

**4. Fragment `extends` SupportFragment或者 `implements` ISupportFragment：(实现方式可参考[MySupportFragment](https://github.com/Z-P-J/Fragmentation/blob/master/demo/src/main/java/com/zpj/fragmentation/demo/demo_flow/base/MySupportFragment.java))：**
````java
// v1.0.0开始，不强制继承SupportFragment，可使用接口＋委托形式来实现自己的SupportFragment
public class HomeFragment extends SupportFragment {

    private void xxx() {
        // 启动新的Fragment, 另有start(fragment,SINGTASK)、startForResult、startWithPop等启动方法
        start(DetailFragment.newInstance(HomeBean));
        // ... 其他pop, find, 设置动画等等API, 请自行查看WIKI
    }
}
````

### [进一步使用、ChangeLog，查看wiki](https://github.com/Z-P-J/Fragmentation/wiki)

### LICENSE
````
Copyright 2016 YoKey

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
````
