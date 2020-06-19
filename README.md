# XLLetterView
字母侧边栏

常用的应用场景：城市搜索的侧边栏，通讯录的侧边栏

参考博客：[自定义View-XLLetterView](https://fanandjiu.com/%E8%87%AA%E5%AE%9A%E4%B9%89View-XLLetterView/#more)

运行效果：
![](https://android-1300729795.cos.ap-chengdu.myqcloud.com/project/Self_View/XLLetterView/xlletter.gif)

### 1. 添加依赖

Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:
~~~
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
~~~

Step 2. Add the dependency
~~~
dependencies {
    implementation 'com.github.xiaoshitounen:XLLetterView:1.0.0'
}
~~~

### 2. Xml文件中静态添加使用

~~~xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <swu.xl.xlletterview.XLLetterView
        android:id="@+id/letter"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:background="@color/colorPrimaryDark"
        android:layout_centerInParent="true"
        app:text_size="12"
        />

</RelativeLayout>
~~~

#### ① 属性

- text_color：字母正常状态的颜色
- select_text_color：字母选中状态的颜色
- text_size：字体大小
- space_hor：水平左右间距
- space_ver：垂直间距

#### ② 触摸事件回调

~~~java
XLLetterView letterView = findViewById(R.id.letter);

letterView.setLetterChangeListener(new XLLetterView.LetterChangeListener() {
    @Override
    public void currentLetter(String letter) {
        Toast.makeText(MainActivity.this, "当前的字母："+letter, Toast.LENGTH_SHORT).show();
    }
});
~~~
