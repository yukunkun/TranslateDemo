### 调用有道翻译和百度翻译的接口的demo
#### FloatWindow的一个demo,模仿360的内存清理的火箭发射,这个demo来自于网络
#### m3u8文件的手动解析,这里主要由于播放解密,手动解析m3u8文件.
## 本地播放m3u8
#### 本地播放m3u8需要在app本地建立一个服务,访问本地的m3u8文件
[NanoHTTPD](https://github.com/NanoHttpd/nanohttpd)一个很稳定的本地服务,具体的使用请参照[这里](https://github.com/NanoHttpd/nanohttpd)
#### 在播放之前必须了解来自APPLE公司的m3u8文件的组成,playlist ,ts片等概念
#### ts片的无缝链接,保证了播放的连续性
#### 由于m3u8的的不同组合,导致每次的解析都会不一样,字符串的截取和拼接,需要根据不同的头文件来判断
#### 当中有很多的坑,只有一次一次的踩了才会知道,拼接playlist的时间是有要求的的,可以用播放器去测试,多久进行ts片的读取
#### 找到时间差值,作为每次ts的拼接时间,否则播放会卡顿(这是一个无形的坑)
#### 东西太多,只能慢慢研究...
####
#### 用到的第三方:
#### [okhttputils:2.6.2](https://github.com/hongyangAndroid/okhttputils),[eventbus:3.0.0](https://github.com/greenrobot/EventBus),[android-async-http:1.4.9](https://github.com/loopj/android-async-http)