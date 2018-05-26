## Chrome插件针对搜索结果有用的页做标记生成树形结构
* 释放无穷无尽的Lisp原力!!!

## TODO
* 可以进行任何网页的Google搜索的回归: 某个搜索词,对应某个答案,是直接的答案还是间接的有用信息 => 便于生成自己的知识库

## 开发

```shell
# 编译 option page 的 JS
lein with-profile dev-option do clean, figwheel
# 编译 background page 的 JS
lein with-profile +dev-background do clean, figwheel
# 编译 content script
lein with-profile +dev-content do clean, cljsbuild auto
```
使用上面三个命令生成三份 JS 文件后，在 Chrome 的 `chrome://extensions/` 页面勾选 Developer Mode，然后点击「Load unpacked extension...」，这时会弹出文件选择窗，选择本项目的 resources/dev 就可以了。

## 发布

```shell
lein with-profile release do clean, cljsbuild once option background content && \
rm -rf resources/release/background/js/out resources/release/option/js/out resources/release/content/js/out && \
zip -r hello_world.zip resources/release/*
```

## 测试

```shell
lein with-profile test do clean, doo phantom test
```
