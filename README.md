BUG汇总：

- Android resource linking failed
   - 本来项目为29的apiversion，改成了27的就报这个错误，改回去就好了
   - 应该是版本不同对应的glide配置不同
- Android Studio上进行真机调试报install failed
  - 将安装好的APP删除，因为之前拒绝了验证
- java.lang.NullPointerException Attempt to read from field 'int android.view.View.mViewFlags
  - 根据提示不知道具体哪个代码出错了，排查应该是holder建立或者是recyclerview封装哪里有问题
  - onCreateViewHolder的返回值自动生成了null，因为在方法前注解了@Nonnull就没注意自动返回的方法没改，导致返回的罪过罪过
- Gilde图片onbind方法不起作用，无法加载图片
  - 实现了onbind接口，但忘记回调，罪过罪过
- 有关权限操作闪退
  - 最开始我用29的API模拟器是可以的，但是在27API的情况下直接闪退，我就纳闷了，向下兼容的android为什么会出现高版本不兼容低版本的情况，后发现有关本地数据读取的时候就会崩溃，而因为之前加入了权限设置，第一时间没向着方面考虑，还需向用户动态请求权限
  - java.lang.SecurityException: Permission Denial 这个一样的，需权限效验
- java.lang.IllegalStateException: Couldn't read row 0, col -1 from CursorWindow. Make sure the Cursor
  - 数据请求格式有误
- java.lang.NullPointerException: Attempt to invoke virtual method 'XXX' on a null object reference
   - 空指针异常，按理说是不会出现的错误，因为两个ID重名，而需要的ID未设置，则导致获取的View未空，而到下面调用方法时才报错