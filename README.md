# LockPatternView

Android 九宫格图案解锁（手势解锁）自定义视图

## 简介

示例应用打开后，先进入 `WelcomeActivity` 页面，然后判断 `SharePreference` 中是否存有设置了的手势密码。

 - 若未设置手势密码，则进入 `SetLockActivity` 页面设置手势密码，密码设置成功后进入 `MainActivity` 页面。
 - 否已设置手势密码，则进入 `UnlockActivity` 页面进行解锁，如果输入的手势密码与 `SharePreference` 中的相同，进入 `MainActivity` 页面。
 
 
## 效果图

| 设置手势密码 | 解开手势密码
| - | -
| ![](http://img.blog.csdn.net/20151115175310962) | ![](http://img.blog.csdn.net/20151115175357456)


## 示例代码分析

| 代码文件结构 | 相关图片资源
| - | -
| ![](http://img.blog.csdn.net/20151115180017855) | ![](http://img.blog.csdn.net/20151115180033050)

**WelcomeActivity.java**

```java
public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String passwordStr = PreferenceUtil.getGesturePassword(WelcomeActivity.this);
                Intent intent;
                if (passwordStr == "") {
                    intent = new Intent(WelcomeActivity.this, SetLockActivity.class);
                } else {
                    intent = new Intent(WelcomeActivity.this, UnlockActivity.class);
                }
                startActivity(intent);
                WelcomeActivity.this.finish();
            }
        }, 2000);
    }
}
```


**MainActivity.java** 

```java
public class MainActivity extends AppCompatActivity {

    private int mBackKeyPressedTimes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onBackPressed() {
        // 双击两次 Back 键退出程序
        if (mBackKeyPressedTimes == 0) {
            Toast.makeText(this, "再按一次退出程序 ", Toast.LENGTH_SHORT).show();
            mBackKeyPressedTimes = 1;
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        mBackKeyPressedTimes = 0;
                    }
                }
            }.start();
            return;
        } else {
            MainActivity.this.finish();
        }
        super.onBackPressed();
    }
}
```

**图案解锁的原理**

「九宫格图案解锁」其实是一个自定义的 `View`：
* 主要是通过重写 `OnDraw()` 和 `OnTouchEvent()` 方法来实现的。
* 暴露了一个接口，可由 `Activity` 去实现它。

> 示例中的 `SetLockActivity` 和 `UnlockActivity` 都实现了 `OnLockListener` 接口。

```java
private OnLockListener mListener;

public interface OnLockListener {

    public void getStringPassword(String password);

    public boolean isPassword();
}

public void setLockListener(OnLockListener listener) {
    this.mListener = listener;
}
```

详细内容可参考源代码。

