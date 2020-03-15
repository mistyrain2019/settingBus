SettingBus 是一个 kv 设置的管理框架  
它可以根据用户在方法和接口上的注解来自动实现用户定义的接口  
定义接口的实现类通过 JSR-269 注解处理技术在编译期生成  
在运行时根据接口类的 Class 信息通过 反射查找和获取实现类  
它支持以自定义频率通过网络等方式更新来自远端的 setting 配置（用户自己实现更新接口）  
也支持以文件或者用户自定义的方式持久化本地 setting 配置信息

common module下是 settingBus 的基本组件和API  
processor module下是 settingBus 的注解处理器  
demo module下是 settingBus 的一个简单使用案例