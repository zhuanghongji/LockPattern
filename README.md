# LockPattern
Android 九宫格图案解锁源码解析，"程序锁"模拟场景使用。

###一、简介
该实例应用一打开，首先进入"欢迎页" WelcomeActivity，然后判断SharePreference中“是否设置了密码”。

 - 若未设置密码，进入SetLockActivity进行密码的设置，密码设置成功后进入MainActivity 。
 - 否则，进入UnlockActivity进行解锁 ,如果密码与SharePreference中相符，就直接进入MainActivity 。
 
###二、 效果图
![设置密码](http://img.blog.csdn.net/20151115175310962 "设置密码")  （左图）设置密码，进行解锁（右图）  ![进行解锁](http://img.blog.csdn.net/20151115175357456 "进行解锁")

### 三、代码分析
####1.代码文件结构和图片资源
![这里写图片描述](http://img.blog.csdn.net/20151115180017855 "代码文件结构图") ![这里写图片描述](http://img.blog.csdn.net/20151115180033050 "用到的图片资源")

####2. WelcomeActivity.java 
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

####3. MainActivity.java 

```java
public class MainActivity extends AppCompatActivity {

    private int mBackKeyPressedTimes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // 双击两次Back键盘退出程序
    @Override
    public void onBackPressed() {
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

####4. 图案解锁的原理
“九宫格图案解锁”其实是一个自定义的View，主要重写了OnDraw()和OnTouchEvent()方法。

另外提供了一个“接口”让Activity去实现，在这里SetLockActivity和UnlockActivity都实现了OnLockListener接口。

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
详细内容请参考源码。

源码下载：https://github.com/zhuanghongji/LockPattern 

