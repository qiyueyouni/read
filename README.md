# 轻阅读
一个全移动端可用的阅读app,不管是手机还是大屏设备都适配了ui，这个仓库是阅读app服务端源码，可以在全端同步进度。开源阅读不方便同步阅读进度，并且开源阅读过于复杂，移植多端难度较大，通过同一个后端的方法，可以做到多端体验基本一致。


# 后台
http://ip:8080/admin(阅读后台)

http://ip:8080/(web阅读)

web端不支持cookie的保存，不支持朗读功能，但支持听书类型，有些cdn需要验证的可能会出错，app版才支持cookie的保存

# cookies
web端不支持webview所以不支持保存cookies，windows端用的js获取的cookies所以不支持httponly的cookie

# 推荐书源
[晴天](https://api.qingtian618.com/)

# 书源权限
书源可以直接调用java代码，所以给予书源权限时一定要小心！

# 未支持函数
以下函数还未支持，有些调用了安卓接口无法支持，有些我觉得用不上，有些我也不知道是干啥，如果有阅读3支持的书源，轻阅读不支持，可以在issues里发给我。
````
fun webViewGetSource(html: String?, url: String?, js: String?, sourceRegex: String): String?
fun webViewGetOverrideUrl(
    html: String?,
    url: String?,
    js: String?,
    overrideUrlRegex: String
): String?
fun un7zFile(zipPath: String): String 
fun unrarFile(zipPath: String): String
fun unArchiveFile(zipPath: String): String
fun  getRarStringContent(url: String, path: String): String
fun  getRarStringContent(url: String, path: String, charsetName: String): String
fun get7zStringContent(url: String, path: String): String
fun get7zStringContent(url: String, path: String, charsetName: String): String 
fun getRarByteArrayContent(url: String, path: String): ByteArray?
fun get7zByteArrayContent(url: String, path: String): ByteArray? 
fun openUrl(url: String) 
fun getGlideUrl(): GlideUrl
fun getMediaItem(): MediaItem 
````
# 热键(web端和windows支持)
esc键返回 左右键翻页
F11 无边框 F12恢复边框(仅windows支持)

# 暂不支持
暂不支持除后台外的任何地方修改添加书源，暂不支持web端朗读，web端不支持修改部分header头，所以web端涉及带修改header的都由后端代理。
目前flutter多数插件不支持鸿蒙，等以后flutter鸿蒙版出正式版了再编译鸿蒙应用。

# 快速部署
将后端文件（下载web*.zip）上传到root目录，确保root/read/read.jar存在，确保root/read/conf.yml存在（conf.yml中可修改后台管理的账号密码），然后安装docker,
网上有一键安装脚本可自行百度，docker一键部署命令如下，如需用mysql数据库请自行修改配置文件。
````
docker run -tid  -e TZ=Asia/Shanghai --name read  -v /root/read:/app -p 8080:8080 --restart=always docker-0.unsee.tech/bitnami/java java -jar /app/read.jar
````
如需使用其他端口可修改第一个8080为想要的端口。当然/root/read可以换成其他路径，只要确保这个路径下有read.jar和conf.yml。

# 已知问题
RSA加密，RSA加密安卓端和JAVA端加密标准不同，所以有可能安卓版本阅读能解密的web版本不能解密，如果安卓端能解密的web不行请修改代码
````
Cipher.getInstance("RSA",BouncyCastleProvider());
````
web端如果想要和安卓端解密一样一定要修改代码，BouncyCastleProvider在org.bouncycastle.jce.provider.BouncyCastleProvider
示范一个完整版的RSA解码，这段代码在已测试通过的书源中摘抄出来的
````
var javaImport = new JavaImporter();
javaImport.importPackage(
Packages.java.lang,
Packages.java.security,
Packages.org.bouncycastle.jce.provider,
Packages.java.security.interfaces,
Packages.java.security.spec,
Packages.java.io,      
Packages.javax.crypto,
Packages.javax.crypto.spec
);
with(javaImport){
function Decode(str){
       jmkey="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjYFYoMbA0uW8by6+YIghxxsvibS9YW4yKVSulykAzZZwZ/+dNTkZ4inY7Pj08aksm6RCGKS6+WfvVQo/EdkcS5p2LY2/76qVzapyHsyQf/Pud6ATPKnwxNt/DaqjL35Z9K0NI/RF9x732RdIEOTKXppfRdzCa/1Ctm/5ZFilY8UmZsppkjDd3XkuPr3n3wVC8WFvqmdJ1N55prRlnaRaO+mIOXo3OsOzIxE5EdcE0TLT9OFZ3Wlbi3E0iI0v/ZsrWoL57YvLwo7BsARp7BansDCx8NZg6ObGQN/tNrE/nKqQTXeJjnFWXdLfhI7xivPPphkj5fNpiufIsIUEd7eXBwIDAQAB";
       publicKey = 	KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.decode(jmkey, 0)));

       instance = Cipher.getInstance("RSA",new BouncyCastleProvider());
        instance.init(2, publicKey);
        decode = Base64.decode(str,0);
        blockSize = instance.getBlockSize();
        byteArrayOutputStream = new ByteArrayOutputStream(64);
         i = 0;
        while (true) {
             i2 = i * blockSize;
            if (decode.length - i2 > 0) {
                byteArrayOutputStream.write(instance.doFinal(decode, i2, blockSize));
                i++;
            } else {
                decode = byteArrayOutputStream.toByteArray();
                byteArrayOutputStream.close();
                return new String(decode);
            }
        }
    }
}
````

ios端如果需要过一些盾如cf盾需要设置ua为ios
````
Mozilla/5.0 (iPhone; CPU iPhone OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.0 Mobile/15E148 Safari/604.1
````


# mysql数据库
后端支持修改为mysql数据库，部署好数据库后参考mysqlconf.yml文件来修改conf.yml，默认的sqlite文件数据库性能弱不适合高强度使用。

# 参考项目
[reader3](https://github.com/hectorqin/reader)

![Image text](https://github.com/autobcb/read/blob/main/png/home.png?raw=true)
![Image text](https://github.com/autobcb/read/blob/main/png/book.png?raw=true)
![Image text](https://github.com/autobcb/read/blob/main/png/login.png?raw=true)
![Image text](https://github.com/autobcb/read/blob/main/png/home2.png?raw=true)
![Image text](https://github.com/autobcb/read/blob/main/png/faxian.png?raw=true)
![Image text](https://github.com/autobcb/read/blob/main/png/search.png?raw=true)
![Image text](https://github.com/autobcb/read/blob/main/png/read.png?raw=true)

![Image text](https://github.com/autobcb/read/blob/main/png/home3.jpg?raw=true)
![Image text](https://github.com/autobcb/read/blob/main/png/found.jpg?raw=true)
![Image text](https://github.com/autobcb/read/blob/main/png/my.jpg?raw=true)
![Image text](https://github.com/autobcb/read/blob/main/png/editsource.jpg?raw=true)
![Image text](https://github.com/autobcb/read/blob/main/png/source.jpg?raw=true)
![Image text](https://github.com/autobcb/read/blob/main/png/sourcelogin.jpg?raw=true)