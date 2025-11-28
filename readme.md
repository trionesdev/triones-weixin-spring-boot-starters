# 微信集成Spring Boot 自动装配
> 对微信SDK进行Spring Boot 的自动装配 
---
## 支持
[微信公众号](triones-weixin-offiaccount-spring-boot-starter)

[微信小程序](triones-weixin-miniprogram-spring-boot-starter)

[微信网页](triones-weixin-web-spring-boot-starter)

## 使用
添加依赖
```xml
    <dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.trionesdev.weixin</groupId>
            <artifactId>weixin-spring-boot-dependencies</artifactId>
            <version>${project.version}</version>
            <type>pom</type>
            <scope>import</scope>
            <optional>true</optional>
        </dependency>
    </dependencies>
</dependencyManagement>
```

---
### 关注我们，一起交流
> 留言回复不及时，可以通过关注公众号联系我们
<div style="text-align: center">
<img src="images/shuque_wx.jpg" width="200px" alt="">
</div>