# Android 和 Java 通用件库

## ViewFindHelper

1. 代替findViewById，简化布局对象的获取；
2. 可用于activity fragment dialog 等任何类；
3. 避免黄油刀造成的成员变量泛滥。

##  DialogFragment 的封装

1. 可以像使用 AlertDialog 一样使用 DialogFragment；
2. 防止屏幕旋转后页面重建导致监听器失效；
3. 监听器回调提供 tag 判断；

## ToastUtil

1. 任意线程调用；
2. 使用 applicationContext 上下文；

